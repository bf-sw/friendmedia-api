package org.product.common;

import org.product.exception.ApiException;
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

    public static long parseToEpochSecond(String yyyymmdd) {
        String tmp = UtilManager.onlyNumber(yyyymmdd);
        if (tmp.length() != 8) {
            throw new ApiException("yyyymmdd 8자리 날짜 포맷이 필요합니다.");
        }

        int year = Integer.valueOf(tmp.substring(0, 4));
        int month = Integer.valueOf(tmp.substring(4, 6));
        int day = Integer.valueOf(tmp.substring(6, 8));

        return parseDate(year, month, day);
    }

    public static long parseDate(int year, int month, int day) {
        LocalDate localDate = LocalDate.of(year, month, day);

        return localDate.atTime(LocalTime.now()).toEpochSecond(ZoneOffset.ofHours(KST_OFFSET));
    }

    public static String parseDateWithDash(long epoch) {
        return formatDate(epoch, DateFormat.yyyymmdd_with_dash);
    }

    public static String formatDate(long epoch, DateFormat dateFormat) {
        return fromEpochTime(epoch)
                .format(dateFormat.format);
    }

    public static LocalDateTime fromEpochTime(long timestamp) {
        return LocalDateTime.ofEpochSecond(timestamp, 0, ZoneOffset.ofHours(KST_OFFSET));
    }
}
