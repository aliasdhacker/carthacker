package org.acarr;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CartHacker {

    public static final int GLOBAL_TIMEOUT = 20000;

    public static final String WEBHOOK_ENDPOINT = "webhook/";

    public static final String HTML_ENDPOINT_TO_PREVENT_LOSING_OUR_COLLECTIVE_ASSES = "omfgdonotsendanymoretextmessagesfortheloveofgoditscostingwaytoomuchfuckingmoney";

    private static final String SID = "ACc00a86b87f3c9913cb49514eb8c9e496";

    private static final String TOKEN= "9bf144d99df2375bd6ee5075bc22eaed";

    private static final String PHONE_ID_1 = "+14152125169";

    public static final int THREAD_WAIT_MINIMUM_BETWEEN_HITS = 3000;

    public static final int THREAD_WAIT_MAX_WAIT_BETWEEN = 15000;

    public static final long THREAD_WAIT_AFTER_FOUND_PRIOR_TO_CHECKING_AGAIN = 60000;

    public static final long MINIMUM_LOOP_TIME_BEST_BUY = 3300;

    public static double BEST_BUY_FILTER_CARDS_BELOW_THIS_DOLLAR_VALUE = 175;

    private static final Logger logger = LoggerFactory.getLogger(CartHacker.class);

    public static final int DEFAULT_FONT_SIZE = 12;

    public static boolean headlessMode = false;

    public static boolean trackOtherSites = false;

    private static Thread t800;

    private static Thread t1000;

    public static Object frame;

    public static boolean useBestBuyAPI = false;

    public static void main(String[] args) throws LifecycleException {
        System.setProperty("webdriver.gecko.driver", "C:\\Program Files\\Mozilla Firefox\\firefox.exe");

        logger.info(" HELP:     --headless for nogui mode");
        logger.info("     --NOSSL ");
        logger.info("    --sslport for sslport");
        logger.info("    --port  for server nonsecure port");
        logger.info("    --keystorefile for ssl keystore");
        logger.info("    --keyalias for keystore cert alias");
        if (args.length != 0 && args[0].contains("--help")) {
            System.exit(0);
        } else if (args.length != 0) {
            for (int i = 0; i < args.length; i++) {
                switch (args[i]) {
                    case "--NOSSL":
                        TomcatUtility.setSsl(false);
                        break;
                    case "--headless":
                        headlessMode = true;
                        logger.info("Loading headless mode.");
                        break;
                    case "--port":
                        String serverport = args[i + 1];
                        logger.info("Loading server insecure port from cmdline: {}", serverport);
                        TomcatUtility.setServerPort(serverport);
                        break;
                    case "--sslport":
                        String sslport = args[i + 1];
                        logger.info("Loading sslport from cmdline: {}", sslport);
                        TomcatUtility.setServerSSLPort(sslport);
                        break;
                    case "--keystorefile":
                        String file = args[i + 1];
                        logger.info("Loading keystorefile from cmdline: {}", file);
                        TomcatUtility.setKeystoreFilename(file);
                        break;
                    case "--keyalias":
                        String alias = args[i + 1];
                        logger.info("Loading key alias from cmdline: {}", alias);
                        TomcatUtility.setKeystoreAlias(alias);
                        break;
                    case "--keystorepass":
                        String pass = args[i + 1];
                        logger.info("Loading pass from cmdline. xxx");
                        TomcatUtility.setKeystorePassword(pass);
                        break;
                    default:
                        break;
                }
            }
        }

        ExecutorService es = Executors.newCachedThreadPool();

        if (TomcatUtility.keystoreNotFound()) {
            TomcatUtility.setSsl(false);
        }

        logger.info("Testing purchase tracking file...");
        if (Utility.hasPurchaseBeenMade()) {
            logger.info("We've already got a card, lets not do this now!");
            System.exit(0);
        }


        Tomcat tomcat = TomcatUtility.createTomcatServer();
        tomcat.start();

//        List<BaseSiteWithStock> sites = Utility.buildSites();

        ThreadManager tm = new ThreadManager();
        ThreadManager tm2 = new ThreadManager();
        t800 = new Thread(new Runnable() {
            @Override
            public void run() {
                tm2.monitorBestBuyVIAAPI();
            }
        });

        t1000 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    tm.monitorStockAtSitesViaHTTP();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        es.submit(t1000);
        es.submit(t800);

    }

    public static Thread getT800() {
        return t800;
    }

    public static Thread getT1000() {
        return t1000;
    }

    public static String getSID() {
        return SID;
    }

    public static String getTOKEN() {
        return TOKEN;
    }

    public static String getPhoneId1() {
        return PHONE_ID_1;
    }
}
