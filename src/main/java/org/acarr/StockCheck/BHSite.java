package org.acarr.StockCheck;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;
import org.acarr.Receipt;
import org.acarr.swing.TableMaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

public class BHSite implements BaseSiteWithStock {

    private static final Logger logger = LoggerFactory.getLogger(BHSite.class);

    private URL url;

    private static final String bhPhotoCartURL = "https://www.bhphotovideo.com/find/cart.jsp";

    private Receipt receipt;

    public BHSite(URL url) {
        this.url = url;
    }

    @Override
    public boolean checkIfInStock() {
        boolean inStock = false;
        //        try (final WebClient webClient = new WebClient()) {
//            CookieManager cookieManager = new CookieManager();
//            cookieManager = webClient.getCookieManager();
//            cookieManager.setCookiesEnabled(true);
//            final HtmlPage page;
//            try {
//
//                page = webClient.getPage(url);
//
//                for (DomElement element : page.getElementsByTagName("button")) {
//                    if (element.toString().toLowerCase().contains("addtocart")
//                            || element.toString().toLowerCase().contains("add to cart")) {
//                        addToCartBHPhoto(page, element, webClient);
//                    }
//                }
//
//                System.exit(3);
//
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

        return inStock;
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
        HtmlButton button = ((HtmlButton) element);
        HtmlPage page2 = webClient.getPage(button.click().getUrl());
        HtmlPage page3 = webClient.getPage(bhPhotoCartURL);
        WebResponse resp = page3.getWebResponse();
        String content = resp.getContentAsString();
        FileWriter fw = new FileWriter("test3.html");
        fw.write(content);
        fw.flush();
        fw.close();
        DomElement checkoutAsGuestButton = null;
        for (DomElement element2 : page.getElementsByTagName("button")) {
            if (element2.toString().toLowerCase().contains("check out as a guest")) {
                checkoutAsGuestButton = element2;
                break;
            }
        }

        if (checkoutAsGuestButton != null) {
            HtmlPage cartForm1 = checkoutAsGuestButton.click();
            // REady To Rock And Roll!!
            logger.info("The Cart PAge: {}", cartForm1);
        }

        return null;
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

    @Override
    public void setGroup(int i) {

    }

    @Override
    public void setTableMaker(TableMaker tableMaker) {
    }

    @Override
    public TableMaker getTableMaker() {
        return null;
    }

    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
    }

    public Receipt getReceipt() {
        return receipt;
    }

    @Override
    public Boolean call() throws Exception {
        return null;
    }

    public void updateTableColumnOne(int index){
        TableMaker.getData()[index][0] = getURL();
    }
}
