package org.acarr;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class DateUtility {

    public static final DateTimeFormatter formatter =
            DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
                    .withLocale(Locale.US)
                    .withZone(ZoneId.systemDefault());
}
