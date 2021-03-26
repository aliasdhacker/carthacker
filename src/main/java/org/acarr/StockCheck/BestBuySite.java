package org.acarr.StockCheck;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.acarr.CartHacker;
import org.acarr.Receipt;
import org.acarr.ThreadManager;
import org.acarr.Utility;
import org.acarr.swing.TableMaker;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.time.Instant;

public class BestBuySite implements BaseSiteWithStock {

    private static final Logger log = LoggerFactory.getLogger(BestBuySite.class);

    private Receipt receipt;

    private long sleepTimer;

    private String rawProduct;

    private long difference;

    private long lastRun;

    private boolean isInStock;

    private int arrayIndex;

    private long lastInStock;

    private URL url;
    private boolean aBoolean;

    public BestBuySite(URL url) {
        this.url = url;
    }

    @Override
    public Boolean call() throws Exception {
        long lasthit = ThreadManager.lasthit.get(getSitePrettyName());
        do {
            Thread.sleep(10);
        } while (System.currentTimeMillis() - lasthit < CartHacker.THREAD_WAIT_MINIMUM_BETWEEN_HITS);

        return checkIfInStock();
    }

    StockUtility stockUtil = null;

    @Override
    public boolean checkIfInStock() {
        getTableMaker().setRaw(getArrayIndex(), "CHECKING NOW...");

        stockUtil = new StockUtility();

        difference = System.currentTimeMillis() - lastRun;
        if (difference > sleepTimer) {
            if (System.currentTimeMillis() - lastInStock > CartHacker.THREAD_WAIT_AFTER_FOUND_PRIOR_TO_CHECKING_AGAIN) {
                isInStock = false;
                stockUtil.logHitServerTime(Instant.now(), "BestBuy");
                loadPageAndParse();
                lastRun = System.currentTimeMillis();
            }else {
                tableMaker.setRaw(getArrayIndex(), "Sleeping... product was found.");
            }
        } else {
            tableMaker.setRaw(getArrayIndex(), "Sleeping, SPAM/DOS PROT...");
        }

        return isInStock;
    }

    private void loadPageAndParse() {
        try {
            long lasthit = ThreadManager.lasthit.put(getSitePrettyName(), System.currentTimeMillis());

            Document doc = Jsoup.parse(url, CartHacker.GLOBAL_TIMEOUT);
            Elements elements = doc.select(".btn-primary");
            for (Element element : elements) {
                if (element.toString().toLowerCase().contains("add-to-cart")) {
                    log.info("FOUND A PRODUCT!!! >>> {}", url);
                    setRawProduct(element.toString());
                    this.isInStock = true;
                    lastInStock = System.currentTimeMillis();
                }
            }

            stockUtil.stockUpdate(this);
        } catch (IOException e) {
            log.error("Error fetching item page, {}", e.getMessage());
            if (log.isDebugEnabled()) {
                log.debug("error: ", e);
            }
            stockUtil.stockUpdate(this);
            getTableMaker().setRaw(getArrayIndex(), "Exception: " + e.getMessage());
        }
    }


    @Override
    public int getArrayIndex() {
        return arrayIndex;
    }

    @Override
    public void setArrayIndex(int i) {
        arrayIndex = i;
    }

    @Override
    public String getRawProduct() {
        return rawProduct;
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
        log.info("CurrentvsSleep:{} - {}", System.currentTimeMillis(), sleepTimer);
    }

    @Override
    public String getSitePrettyName() {
        return Utility.getPrettyName(url);
    }

    @Override
    public boolean isInStock() {
        return isInStock;
    }

    @Override
    public long getLastInStock() {
        return lastInStock;
    }

    @Override
    public void setLastInStock(long lastInStock) {
        this.lastInStock = lastInStock;
    }

    @Override
    public void setRawProduct(String string) {
        this.rawProduct = string;
    }

    private int group;

    @Override
    public void setGroup(int i) {
        this.group = i;
    }

    private TableMaker tableMaker;

    @Override
    public void setTableMaker(TableMaker tableMaker) {
        this.tableMaker = tableMaker;
    }

    public TableMaker getTableMaker() {
        return tableMaker;
    }

    public void updateTableColumnOne(int index){
        TableMaker.getData()[index][0] = getURL();
    }
}
