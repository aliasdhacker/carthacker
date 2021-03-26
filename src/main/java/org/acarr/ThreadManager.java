package org.acarr;

import org.acarr.BestBuy.API;
import org.acarr.BestBuy.GraphicsCard;
import org.acarr.SMS.*;
import org.acarr.StockCheck.BaseSiteWithStock;
import org.acarr.Tracking.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class ThreadManager {
    ExecutorService executorService = Executors.newFixedThreadPool(223);
    ArrayList<Future> futures = new ArrayList<>();
    ArrayList<BaseSiteWithStock> sites = new ArrayList<>();
    HashMap<String, ArrayList<BaseSiteWithStock>> poppers = new HashMap<>();
    public static HashMap<String, Long> lasthit = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(ThreadManager.class);
    public static Boolean BBUY_IS_ALIVE = false;


    public void monitorBestBuyVIAAPI() {
        long lastBestBuyHit = 0;
        ExecutorService bestBuyThreads = Executors.newFixedThreadPool(100);
        while (true) {
            if (!CartHacker.useBestBuyAPI) {
                try {
                    log.info("BBAPI DISABLED!");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                API api = new API(lastBestBuyHit);
                // no cards below 180$
                api.setFilterPriceAmount(CartHacker.BEST_BUY_FILTER_CARDS_BELOW_THIS_DOLLAR_VALUE);
                Future f = bestBuyThreads.submit(api);
                BBUY_IS_ALIVE = true;
                try {
                    ArrayList<Product> products = (ArrayList<Product>) f.get();
                    lastBestBuyHit = System.currentTimeMillis();
                    for (Product card : products) {
                        try {
                            SMSUtility.sendMessages(card);
                            Utility.saveProduct(card);
                        } catch (Exception e) {
                            BBUY_IS_ALIVE = false;
                            log.error("Error saving bbuy products:", e);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    BBUY_IS_ALIVE = false;
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    BBUY_IS_ALIVE = false;
                }
                api = null;
            }
        }
//
//        log.error("BBUY THREAD DIED!!!");
//        executorService.submit(new Runnable() {
//            @Override
//            public void run() {
//                monitorBestBuyVIAAPI();
//            }
//        });
    }

    public void monitorStockAtSitesViaHTTP() throws InterruptedException {
        setupSplits(sites);
        try {
            do {
                if (!CartHacker.trackOtherSites) {
                    try {
                        log.info("Other Sites DISABLED!");
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    for (ArrayList list : poppers.values()) {
                        futures.add(executorService.submit(launchGroup(list)));
                    }
                    for (Future f : futures) {
                        f.get();
                    }
                    setupSplits(sites);
                    Utility.sleep(1000);
                }
            } while (true);
        } catch (
                Exception e) {
            e.printStackTrace();
        }

    }

    private void setupSplits(ArrayList<BaseSiteWithStock> sites) {
        this.sites = Utility.buildSites(sites);
        for (BaseSiteWithStock site : this.sites) {
            if (!poppers.containsKey(site.getSitePrettyName())) {
                poppers.put(site.getSitePrettyName(), new ArrayList<>());
                lasthit.put(site.getSitePrettyName(), 0l);
            }

            poppers.get(site.getSitePrettyName()).add(site);
        }
    }

    private Runnable launchGroup(ArrayList<BaseSiteWithStock> poppers) {
        return new Runnable() {
            @Override
            public void run() {
                List<Future> futures = new ArrayList<>();

                while (!poppers.isEmpty()) {
                    int randomIndex = 0;
                    if (poppers.size() > 1)
                        randomIndex = new Random(System.currentTimeMillis()).nextInt(poppers.size() - 1);
                    try {
                        Future f = executorService.submit(poppers.get(randomIndex));
                        futures.add(f);
                        f.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    poppers.remove(randomIndex);
                }

                for (Future f : futures) {
                    try {
                        f.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }


    private void sleeper(int i) {
        if (i < CartHacker.THREAD_WAIT_MINIMUM_BETWEEN_HITS) i = CartHacker.THREAD_WAIT_MINIMUM_BETWEEN_HITS;
        int finalI = i;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(new Random(System.currentTimeMillis()).nextInt(finalI));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void checkStockResult(BaseSiteWithStock site) throws
            ExecutionException, InterruptedException, TimeoutException {


    }
}

