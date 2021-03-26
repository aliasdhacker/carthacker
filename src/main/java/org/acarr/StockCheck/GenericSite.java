package org.acarr.StockCheck;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.acarr.CartHacker;
import org.acarr.Receipt;
import org.acarr.SMS.SMSUtility;
import org.acarr.ThreadManager;
import org.acarr.Tracking.GenericProduct;
import org.acarr.Tracking.Product;
import org.acarr.Utility;
import org.acarr.swing.TableMaker;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.util.Random;

public class GenericSite implements BaseSiteWithStock {


    private static final Logger log = LoggerFactory.getLogger(GenericSite.class);

    private Receipt receipt;

    private URL url;

    private long sleepTimer;

    private String rawProduct;
    private long difference;

    private long lastRun;

    private boolean isInStock;

    private int arrayIndex;

    private long lastInStock;

    public GenericSite(URL url) {
        this.url = url;
        sleepTimer = new Random(System.currentTimeMillis()).nextInt(5000);
    }

    @Override
    public Boolean call() throws Exception {
        long lasthit = ThreadManager.lasthit.get(getSitePrettyName());

        do {
            Thread.sleep(10);
        } while (System.currentTimeMillis() - lasthit < CartHacker.THREAD_WAIT_MINIMUM_BETWEEN_HITS);
        return checkIfInStock();
    }

    StockUtility stockUtility;

    @Override
    public boolean checkIfInStock() {
        tableMaker.setRaw(getArrayIndex(), "CHECKING NOW...");

        difference = System.currentTimeMillis() - lastRun;
        if (difference > sleepTimer) {
            if (System.currentTimeMillis() - lastInStock > CartHacker.THREAD_WAIT_AFTER_FOUND_PRIOR_TO_CHECKING_AGAIN) {
                stockUtility = new StockUtility();
                isInStock = false;
                stockUtility.logHitServerTime(Instant.now(), getSitePrettyName());
                loadAndCheckPage();
                loadPurchase();
                lastRun = System.currentTimeMillis();
            } else {
                tableMaker.setRaw(getArrayIndex(), "Sleeping... product was found.");
            }
        } else {
            tableMaker.setRaw(getArrayIndex(), "Sleeping, SPAM/DOS PROT...");
        }

        return isInStock;
    }

    private void loadPurchase() {

//            try {
//
//                page = webClient.getPage(url);
//
////                attemptToPurchase(page, page.getElementById("item_add_cart"), webClient);
//                List<Object> buttons = page.getByXPath("div[contains(@class, 'btn')]");
//                for (Object button : buttons) {
//                    if (button.toString().toLowerCase().contains("add to cart")) {
//                        log.info("ADD TO CART BUTTON FOUND ON PAGE: " + url);
//                        isInStock = true;
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
    }

    private void loadAndCheckPage() {
        try {
            ThreadManager.lasthit.put(getSitePrettyName(), System.currentTimeMillis());
            Document doc = Jsoup.connect(String.valueOf(url)).get();
            Elements buttons = doc.select("button");
            Elements anchors = doc.select("a");
            boolean logged = false;
            for (Element element : buttons) {
                String buttonString = element.toString().toLowerCase();
                if (buttonString.contains("add to cart") || buttonString.contains("buy now")) {
                    if (!logged)
                        log.info("FOUND A PRODUCT!!! >>> {}", url.toString());
                    this.isInStock = true;
                    lastInStock = System.currentTimeMillis();
                    logged = true;
                }
            }
            for (Element element : anchors) {
                String anchor = element.toString().toLowerCase();
                if (anchor.contains("add to cart") || anchor.contains("buy now")) {
                    if (!logged)
                        log.info("FOUND A PRODUCT!!! >>> {}", url.toString());
                    logged = true;
                    this.isInStock = true;
                    lastInStock = System.currentTimeMillis();
                }
            }
            stockUtility.stockUpdate(this);
        } catch (
                Exception e) {
            stockUtility.stockUpdate(this);
            tableMaker.setRaw(getArrayIndex(), "Exception: " + e.getMessage());
        }

        if (isInStock) {
            GenericProduct prod = new GenericProduct();
            prod.setProductName("--");
            prod.setLink(this.url);
            SMSUtility.sendMessages(prod);
        }
    }

    @Override
    public Receipt attemptToPurchase(HtmlPage page, DomElement element, WebClient webClient) throws IOException {
        return null;
    }

    @Override
    public void setSleepTimer(long sleep) {
        this.sleepTimer = sleep;
    }

    @Override
    public URL getURL() {
        return url;
    }

    @Override
    public void logCurrentTimeVsSleepTimer() {
        log.info("Current time difference: {}\nSleepTime: {}", difference, sleepTimer);
    }

    @Override
    public String getSitePrettyName() {
        return Utility.getPrettyName(url);
    }

    @Override
    public String getRawProduct() {
        return rawProduct;
    }

    public void setRawProduct(String rawProduct) {
        this.rawProduct = rawProduct;
    }

    private int group;

    @Override
    public void setGroup(int i) {
        this.group = i;
    }

    @Override
    public boolean isInStock() {
        return isInStock;
    }

    @Override
    public void setArrayIndex(int arrayIndex) {
        this.arrayIndex = arrayIndex;
    }

    @Override
    public int getArrayIndex() {
        return arrayIndex;
    }

    public long getLastInStock() {
        return lastInStock;
    }

    public void setLastInStock(long lastInStock) {
        this.lastInStock = lastInStock;
    }

    private TableMaker tableMaker;

    @Override
    public void setTableMaker(TableMaker tableMaker) {
        this.tableMaker = tableMaker;
    }

    public TableMaker getTableMaker() {
        return tableMaker;
    }

    public void updateTableColumnOne(int index) {
        TableMaker.getData()[index][0] = getURL();
    }
}
