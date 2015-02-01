package com.github.norbo11.topbuilders.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.TimeZone;

public class DateTimeUtil {
    public static long getCurrentTimestamp() {
        return Calendar.getInstance().getTimeInMillis() / 1000;
    }

    public static String formatDateAndTime(LocalDateTime date) {
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }
    
    public static String formatDate(LocalDateTime date) {
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
    
    public static String formatTime(LocalDateTime date) {
        return date.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

	public static LocalDateTime getDateTimeFromTimestamp(long timestamp) {
		return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), TimeZone.getDefault().toZoneId());
	}

}
