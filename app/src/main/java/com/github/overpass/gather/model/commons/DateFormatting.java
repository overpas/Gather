package com.github.overpass.gather.model.commons;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateFormatting {

    public static SimpleDateFormat getMeetingDateFormat() {
        return new SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault());
    }
}
