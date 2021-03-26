package org.acarr;

import org.acarr.StockCheck.BaseSiteWithStock;
import org.acarr.StockCheck.BestBuySite;
import org.acarr.StockCheck.GenericSite;
import org.acarr.Tracking.Product;
import org.acarr.Web.CustomServlet;
import org.acarr.swing.Frame;
import org.acarr.swing.TableMaker;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

public class Utility {

    private static final Logger logger = LoggerFactory.getLogger(Utility.class);

    private static String urlFileName = "ProductPageList.txt";

    private static String purchaseTrackingFile = "PurchaseTrackingFile.txt";

    private static String productTrackingFile = "ProductTrackingFile.bin";

    private static final int URL_PRODUCT_FILE_ERROR_CODE = -99;

    private static ArrayList<URL> productList;

    public static boolean firstRun = true;

    public static long getRandomTimeLengthForSleep(long startingValue) {
        long finishingValue = startingValue;
        int additionalTime = 0;

        Random random = new Random(System.currentTimeMillis());

        additionalTime = random.nextInt(100);

        additionalTime *= 100;

        finishingValue += additionalTime;

//        logger.info("Generated random amount of time. {} ", finishingValue);

        return finishingValue;
    }

    public static ArrayList<URL> loadProductURLsFromFile() {
        Utility.productList = new ArrayList<>();
        BufferedReader bufferedReader = null;
        try {
            File file = new File(urlFileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            bufferedReader = new BufferedReader(new FileReader(urlFileName));
        } catch (Exception e) {
            logger.error("Error reading the file that contains product page URLS.", e);
            System.exit(URL_PRODUCT_FILE_ERROR_CODE);
        }
        String line = "";

        try {
            while ((line = bufferedReader.readLine()) != null) {
                if (line != null && line.trim() != "" && line.toLowerCase().contains("http")) {
                    if (!line.startsWith("#") && !line.startsWith("//") && !line.toLowerCase().contains("bestbuy"))
                        Utility.productList.add(new URL(line));
                }
            }
        } catch (Exception e) {
            logger.error("Error decoding the file that contains the product page URLS.", e);
            System.exit(URL_PRODUCT_FILE_ERROR_CODE);
        }

        return Utility.productList;
    }

    public static boolean hasPurchaseBeenMade() {
        BufferedReader bufferedReader = null;
        String line = "";
        try {
            File file = new File(purchaseTrackingFile);
            if (!file.exists())
                file.createNewFile();
            bufferedReader = new BufferedReader(new FileReader(Utility.purchaseTrackingFile));
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


    public static String getPurchaseTrackingFile() {
        return purchaseTrackingFile;
    }

    public static void setPurchaseTrackingFile(String purchaseTrackingFile) {
        Utility.purchaseTrackingFile = purchaseTrackingFile;
    }

    public static String getUrlFileName() {
        return urlFileName;
    }

    public static void setUrlFileName(String urlFileName) {
        Utility.urlFileName = urlFileName;
    }

    public static ArrayList<URL> getProductList() {
        loadProductURLsFromFile();

        return Utility.productList;
    }

    public static boolean isBestBuy(BaseSiteWithStock site) {
        if (site.getURL().toString().toLowerCase().contains("bestbuy")) {
            return true;
        }

        return false;
    }

    public static String getPrettyName(URL url) {
        String pretty = "";
        String[] urlsplit = url.toString().split("\\/\\/");
        pretty = urlsplit[1];
        pretty = pretty.split("\\/")[0];

        return pretty;
    }

    public static void sleep300() {
        sleep(300);
    }

    public static void sleep1000() {
        sleep(1000);
    }

    public static void sleep(long sleepTimer) {
        try {
            Thread.sleep(sleepTimer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sleepWithRandomMaxMilliseconds(int i, Random random) {
        sleep(random.nextInt(i));
    }

    public static ArrayList<BaseSiteWithStock> buildSites(ArrayList<BaseSiteWithStock> sites) {
        ArrayList<URL> list = getProductList();
        if (TableMaker.getData() != null && list.size() == TableMaker.getData().length && !firstRun)
            return sites;

        TableMaker tableMaker = new TableMaker();
        sites = new ArrayList<>();
        int index = 0;

        TableMaker.setData(new Object[list.size()][3]);
        CartHacker.frame = new Frame();

        if (!CartHacker.headlessMode) {
            Frame tframe = (Frame) CartHacker.frame;
            ((Frame) tframe).setjTable1(tableMaker.getTable());
            ((Frame) tframe).initComponents();
            ((Frame) tframe).setVisible(true);
        }

        for (URL theURL : list) {
            BaseSiteWithStock site = null;
            if (Utility.isBestBuy(new GenericSite(theURL))) {
                site = new BestBuySite(theURL);
            } else {
                site = new GenericSite(theURL);
            }

            site.setArrayIndex(index);
            site.setTableMaker(tableMaker);
            TableMaker.getData()[index][0] = site.getURL();

            sites.add(site);
            index++;
        }

        firstRun = false;

        return sites;
    }

    public static void saveProduct(Product product) {
        File file = new File(productTrackingFile);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ArrayList<Product> products = loadProducts();
        products.add(product);
        FileOutputStream fout = null;
        ObjectOutputStream oos = null;
        try {
            fout = new FileOutputStream(file);
            oos = new ObjectOutputStream(fout);
            oos.writeObject(products);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Product> loadProducts() {
        File file = new File(productTrackingFile);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ArrayList<Product> products = new ArrayList<>();
        FileInputStream fin = null;
        ObjectInputStream ois = null;
        try {
            fin = new FileInputStream(file);
            ois = new ObjectInputStream(fin);
            products = (ArrayList<Product>) ois.readObject();
            ois.close();
        } catch (Exception e) {
            logger.info("Unable to load products from file, prob empty file: {}", e.getMessage());
//            if (logger.isDebugEnabled())
//                logger.debug("Exception : ", e);
        }

        return products;
    }
}
