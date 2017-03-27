package com.codepath.apps.hashtagfresh.util;

import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateUtil {
    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        relativeDate = relativeDate.replaceAll(" ago", "");
        relativeDate = relativeDate.replaceAll(" days?", "h");
        relativeDate = relativeDate.replaceAll(" hours?", "h");
        relativeDate = relativeDate.replaceAll(" minutes?", "m");
        relativeDate = relativeDate.replaceAll(" seconds?", "s");

        return relativeDate;
    }
}
