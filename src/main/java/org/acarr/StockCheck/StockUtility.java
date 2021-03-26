package org.acarr.StockCheck;

import org.acarr.CartHacker;
import org.acarr.DateUtility;
import org.acarr.Tracking.GenericProduct;
import org.acarr.Utility;
import org.acarr.swing.Frame;
import org.acarr.swing.TableMaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class StockUtility {
    private static final Logger log = LoggerFactory.getLogger(StockUtility.class);

    public void stockUpdate(BaseSiteWithStock site) {
        if (site.isInStock()) {
            site.setLastInStock(System.currentTimeMillis());
            Utility.saveProduct(new GenericProduct(site.getSitePrettyName(), site.getRawProduct(), site.getURL()));
            TableMaker.setStatus(site.getArrayIndex(), "OK");
            TableMaker.setRaw(site.getArrayIndex(), "FOUND PRODUCT GET OFF YER ASS N GIT TO IT");
            if (!CartHacker.headlessMode) {
                if (CartHacker.frame instanceof Frame) {
                    ((Frame) CartHacker.frame).updateTopRightTextOutput("FOUND AT URL: " + site.getURL().toString());
                }
            }
//            site.logCurrentTimeVsSleepTimer();
        } else {
            long current = System.currentTimeMillis();
            // If never found update status now, if found recently we don't want to clear status too fast.
            if (site.getLastInStock() == 0 || (current - 120000 > site.getLastInStock())) {
                TableMaker.setStatus(site.getArrayIndex(), "OOS");
                TableMaker.setRaw(site.getArrayIndex(), "Sleep.. at: " + DateUtility.formatter.format(Instant.now()) +
                        ((current - site.getLastInStock()) == current ? " NEVER " : String.valueOf((current - site.getLastInStock()) / 1000 / 60)) + " minutes ");
            } else {
                TableMaker.setRaw(site.getArrayIndex(), "Thread sleeping...");
            }
        }
        site.getTableMaker().setWebsite(site.getArrayIndex(), site.getURL().toString());
    }

    public void logHitServerTime(Instant now, String server) {
        log.info("{} hit at: {}", server, DateUtility.formatter.format(now));
    }

}
