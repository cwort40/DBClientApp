package com.c195.dbclientapp.model;

/**
 *
 The MonthReport class represents a report containing information about the number of appointments for a given month,
 including the month name and the total number of appointments.
 */
public class MonthReport {
    private final String month;
    private final int total;

    /**
     *
     * @param month
     * @param total
     */
    public MonthReport(String month, int total) {
        this.month = month;
        this.total = total;
    }

    /**
     *
     * @return month
     */
    public String getMonth() {
        return month;
    }

    /**
     *
     * @return total
     */
    public int getTotal() {
        return total;
    }
}

