package com.c195.dbclientapp.model;

/**
 *
 Class representing a customer report containing the customer's ID and the country they are located in.
 */
public class CustomerReport {
    private final int customerId;
    private final int countryId;

    /**
     *
     * @param customerId
     * @param countryId
     */
    public CustomerReport(int customerId, int countryId) {
        this.customerId = customerId;
        this.countryId = countryId;
    }

    /**
     *
     * @return customerId
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     *
     * @return countryId
     */
    public int getCountryId() {
        return countryId;
    }
}
