package org.acarr.StockCheck;

import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.acarr.Receipt;
import org.acarr.swing.TableMaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;

public class AsusSite implements BaseSiteWithStock {

    private static final Logger logger = LoggerFactory.getLogger(AsusSite.class);
    public static final String CHECKOUT_PAGE_NAME = "z_asuscheckoutpage.html";

    private final URL theURL;

    private Receipt receipt;

    public AsusSite(URL theURL) {
        this.theURL = theURL;
    }

    @Override
    public Boolean call() throws Exception {
        return null;
    }

    @Override
    public boolean checkIfInStock() {
        boolean isInStock = false;
        try (final WebClient webClient = new WebClient()) {
            CookieManager cookieManager = new CookieManager();
            cookieManager = webClient.getCookieManager();
            cookieManager.setCookiesEnabled(true);
            final HtmlPage page;
            try {

                page = webClient.getPage(theURL);

                attemptToPurchase(page, page.getElementById("item_add_cart"), webClient);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return isInStock;
    }

    @Override
    public int getArrayIndex() {
        return 0;
    }

    @Override
    public void setArrayIndex(int i) {

    }

    @Override
    public String getRawProduct() {
        return null;
    }

    @Override
    public Receipt attemptToPurchase(HtmlPage page, DomElement element, WebClient webClient) throws IOException {
        Receipt receipt = new Receipt();
        // Handle ASUS buy
        // Asus buy has an "Add to Cart Button" that does nothing, then you must click the "Checkout" button to go cart
//        HtmlPage page2 = element.click();
//        //add a sleep, because the page takes a min of 5 seconds, we probably need to make this smarter.
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException ie) {
//            // ignore
//            logger.info("Did not sleep after clicking on 'Add To Cart' on ASUS page.");
//        }
//
//        for (DomElement theAnchor : page2.getElementsByTagName("a")) {
//            if (theAnchor.toString().toLowerCase().contains("shopping") && theAnchor.toString().toLowerCase().contains("cart")) {
//                HtmlPage checkoutPage = ((HtmlAnchor) theAnchor).click();
//                File file = new File(CHECKOUT_PAGE_NAME);
//                if (file.exists())
//                    file.delete();
//                FileWriter fw = new FileWriter(CHECKOUT_PAGE_NAME);
//                fw.write(checkoutPage.getWebResponse().getContentAsString());
//                fw.flush();
//                fw.close();
//
//                break;
//            }
//        }

        setReceipt(receipt);
        return receipt;
    }

    @Override
    public void setSleepTimer(long sleep) {

    }

    @Override
    public URL getURL() {
        return null;
    }

    @Override
    public void logCurrentTimeVsSleepTimer() {

    }

    @Override
    public String getSitePrettyName() {
        return null;
    }

    @Override
    public boolean isInStock() {
        return false;
    }

    @Override
    public long getLastInStock() {
        return 0;
    }

    @Override
    public void setLastInStock(long lastInStock) {

    }

    @Override
    public void setRawProduct(String string) {

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

    public Receipt getReceipt() {
        return receipt;
    }

    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
    }

    public void updateTableColumnOne(int index){
        TableMaker.getData()[getArrayIndex()][0] = getURL();
    }
}
