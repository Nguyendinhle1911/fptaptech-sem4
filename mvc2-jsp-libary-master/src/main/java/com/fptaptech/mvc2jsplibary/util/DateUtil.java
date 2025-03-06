package com.fptaptech.mvc2jsplibary.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateUtil {

    // Định dạng mặc định cho ngày: yyyy-MM-dd
    private static final DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Chuyển LocalDate thành chuỗi theo định dạng mặc định
     */
    public static String formatDate(LocalDate date) {
        if (date == null) return null;
        return date.format(DEFAULT_DATE_FORMATTER);
    }

    /**
     * Chuyển LocalDate thành chuỗi theo định dạng tùy chỉnh
     */
    public static String formatDate(LocalDate date, String pattern) {
        if (date == null || pattern == null) return null;
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * Chuyển chuỗi thành LocalDate theo định dạng mặc định
     */
    public static LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) return null;
        try {
            return LocalDate.parse(dateStr, DEFAULT_DATE_FORMATTER);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date format: " + dateStr + ". Expected format: yyyy-MM-dd");
        }
    }

    /**
     * Chuyển chuỗi thành LocalDate theo định dạng tùy chỉnh
     */
    public static LocalDate parseDate(String dateStr, String pattern) {
        if (dateStr == null || dateStr.trim().isEmpty() || pattern == null) return null;
        try {
            return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date format: " + dateStr + ". Expected format: " + pattern);
        }
    }

    /**
     * Tính số ngày giữa hai ngày
     */
    public static long daysBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date must not be null");
        }
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    /**
     * Kiểm tra một ngày có quá hạn so với ngày hiện tại hay không
     */
    public static boolean isOverdue(LocalDate dueDate) {
        if (dueDate == null) return false;
        return dueDate.isBefore(LocalDate.now());
    }

    /**
     * Lấy ngày hiện tại
     */
    public static LocalDate getCurrentDate() {
        return LocalDate.now();
    }
}