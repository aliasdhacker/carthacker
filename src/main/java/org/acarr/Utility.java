package org.acarr;

import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class Utility {

    private static final Logger logger = LoggerFactory.getLogger(Utility.class);

    public static long getRandomTimeLengthForSleep(long startingValue) {
        long finishingValue = startingValue;
        int additionalTime = 0;

        Random random = new Random(System.currentTimeMillis());

        additionalTime = random.nextInt(100);

        additionalTime *= 100;

        finishingValue += additionalTime;

        logger.info("Generated random amount of time. {} ", finishingValue);

        return finishingValue;
    }
}
