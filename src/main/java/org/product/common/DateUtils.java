package org.product.common;

import java.time.*;

public class DateUtils {
    public static int KST_OFFSET = 9;

    public static long getNow() {
        return Instant.now().getEpochSecond();
    }

    public static long getPlusMinute(int minute) {
        LocalDateTime now = LocalDateTime.now();
        return now.plusMinutes(minute).toEpochSecond(ZoneOffset.ofHours(KST_OFFSET));
    }

    public static long getMinusMinute(int minute) {
        LocalDateTime now = LocalDateTime.now();
        return now.minusMinutes(minute).toEpochSecond(ZoneOffset.ofHours(KST_OFFSET));
    }

    public static LocalDate fromEpochDate(long timestamp) {
        return Instant.ofEpochSecond(timestamp).atZone(ZoneOffset.ofHours(KST_OFFSET).systemDefault()).toLocalDate();
    }

    public static long fromLocalDateMin(LocalDate localDate) {
        return LocalDateTime.of(localDate, LocalTime.MIN)
                .toEpochSecond(ZoneOffset.ofHours(KST_OFFSET));
    }

    public static long fromLocalDateMax(LocalDate localDate) {
        return LocalDateTime.of(localDate, LocalTime.MAX)
                .toEpochSecond(ZoneOffset.ofHours(KST_OFFSET));
    }
}
