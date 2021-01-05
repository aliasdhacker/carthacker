package org.acarr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class CartHacker {

    private static final int URL_PRODUCT_FILE_ERROR_CODE = -99;

    private static String urlFileName = "ProductPageList.txt";

    private static String purchaseTrackingFile = "PurchaseTrackingFile.txt";

    private static ArrayList<URL> productURLs = new ArrayList<>();

    private static final Logger logger = LoggerFactory.getLogger(CartHacker.class);

    public static void main(String[] args) {

        logger.info("Loading product URLs from file named {}", urlFileName);

        loadProductURLsFromFile();

        logger.info("Loaded {} product URLs from file.", productURLs.size());

        logger.info("Testing purchase tracking file...");

        if (hasPurchaseBeenMade()) {
            logger.info("We've already got a card, lets not do this now!");
            System.exit(0);
        }

        // Start the process
        processMinder();
    }

    public static void processMinder() {
        while (true) {
            for (URL theURL : productURLs) {
                logger.info("Checking if product in stock for URL: {}", theURL);
                ProductStockChecker productStockChecker = new ProductStockChecker();
                try {
                    if (productStockChecker.isProductInStockForURL(theURL)) {
                        if (productStockChecker.isBHPhoto()) {
                            BHPhotoCartBuyer bhPhotoCartBuyer = new BHPhotoCartBuyer(theURL);

                            bhPhotoCartBuyer.attemptToPurchase();
                        } else if (productStockChecker.isASUS()) {
                            AsusCartBuyer buyer = new AsusCartBuyer(theURL);

                            buyer.attemptToPurchase();
                        }
                    }
                } catch (IOException e) {
                    logger.error("Error checking product stock, error is: " + e);
                }

                try {
                    Thread.sleep(Utility.getRandomTimeLengthForSleep(10000));
                } catch (InterruptedException e) {
                    logger.error("Error sleeping.", e);
                }


            }
        }
    }

    public static boolean hasPurchaseBeenMade() {
        BufferedReader bufferedReader = null;
        String line = "";
        try {
            bufferedReader = new BufferedReader(new FileReader(purchaseTrackingFile));
            line = bufferedReader.readLine();
            if (line != null)
                line = line.trim();

            if (line == null || line.length() == 0) {
                logger.debug("No purchase has been made according to purchase tracking file.");
                return false;
            }
        } catch (Exception e) {
            logger.error("Error checking purchase file.", e);
        }

        logger.debug("We have made a purchase, and the value is: " + line);
        return true;
    }

    private static void loadProductURLsFromFile() {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(urlFileName));
        } catch (Exception e) {
            logger.error("Error reading the file that contains product page URLS.", e);
            System.exit(URL_PRODUCT_FILE_ERROR_CODE);
        }
        String line = "";

        try {
            while ((line = bufferedReader.readLine()) != null) {
                if (line != null && line.trim() != "" && line.toLowerCase().contains("http"))
                    productURLs.add(new URL(line));
            }
        } catch (Exception e) {
            logger.error("Error decoding the file that contains the product page URLS.", e);
            System.exit(URL_PRODUCT_FILE_ERROR_CODE);
        }

    }

}
