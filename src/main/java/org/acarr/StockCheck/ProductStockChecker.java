package org.acarr.StockCheck;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class ProductStockChecker {

    private static final Logger logger = LoggerFactory.getLogger(ProductStockChecker.class);

    private boolean isBHPhoto = false;

    private boolean isASUS = false;

    public boolean isProductInStockForURL(URL url) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(String.valueOf(url));
        HttpResponse httpresponse = httpclient.execute(httpget);
        Scanner sc = new Scanner(httpresponse.getEntity().getContent());

        if (goodStatusCode(httpresponse)) {
            boolean notifyAvailableFound = false;
            StringBuilder responseRaw = new StringBuilder();

            if (String.valueOf(url).contains("bhphotovideo")) {
                isBHPhoto = true;
                while (sc.hasNext()) {
                    String line = sc.nextLine();
                    if(line.toLowerCase().contains("add to cart")){
                    logger.info("Found Product in STOCK!~!@#@!#! URL: {}", url);
                    return true;}

                    if (line.toLowerCase().contains("notify when available")) {
                        notifyAvailableFound = true;
                    }
                }
            } else if(String.valueOf(url).toLowerCase().contains("asus")){
                isASUS = true;
                while (sc.hasNext()) {
                    String line = sc.nextLine();
                    if(line.toLowerCase().contains("add to cart") && line.toLowerCase().contains("button")) {
                        // Product in stock
                        logger.info("Found Product in STOCK!~!@#@!#! URL: {}", url);
                        return true;
                    }
                }
            }


            logger.info("Product not in stock at: {}", url);

            if (notifyAvailableFound) {
                logger.info("This was a BHPhoto URL, we did see the Notify when Available link.");
            }

        } else {
            logger.error("Error: received bad status code for URL - {} ", url);
        }

        return false;
    }

    private boolean goodStatusCode(HttpResponse httpresponse) {
        logger.debug("Testing for good status code, actual status is: {}", httpresponse.getStatusLine());
        return (httpresponse.getStatusLine() != null && httpresponse.getStatusLine().toString().contains("200"));
    }

    public boolean isBHPhoto() {
        return isBHPhoto;
    }

    public void setBHPhoto(boolean BHPhoto) {
        isBHPhoto = BHPhoto;
    }

    public boolean isASUS() {
        return isASUS;
    }

    public void setASUS(boolean ASUS) {
        isASUS = ASUS;
    }
}
