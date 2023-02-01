package com.c195.dbclientapp.model;

import com.c195.dbclientapp.model.Country;
import com.c195.dbclientapp.model.Customer;
import com.c195.dbclientapp.model.FirstLevelDivision;

/**
 *
 * A class that represents a customer with their associated division and country.
 */
public class CustomerWithDivision {
    private final Customer customer;
    private final FirstLevelDivision division;
    private final Country country;

    /**
     * 
     * @param customer
     * @param division
     * @param country
     */
    public CustomerWithDivision(Customer customer, FirstLevelDivision division, Country country) {
        this.customer = customer;
        this.division = division;
        this.country = country;
    }

    /**
     * 
     * @return customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * 
     * @return division
     */
    public FirstLevelDivision getDivision() { return division; }

    /**
     * 
     * @return country
     */
    public Country getCountry() { return country; }

    /**
     * 
     * @return customer.getCustomerId()
     */
    public int getCustomerId() { return customer.getCustomerId(); }

    /**
     * 
     * @return customer.getCustomerName()
     */
    public String getCustomerName() { return customer.getCustomerName(); }

    /**
     * 
     * @return customer.getAddress()
     */
    public String getAddress() { return customer.getAddress(); }

    /**
     * 
     * @return customer.getPostalCode()
     */
    public String getPostalCode() { return customer.getPostalCode(); }

    /**
     * 
     * @return customer.getPhone()
     */
    public String getPhone() { return customer.getPhone(); }

    /**
     * 
     * @return division.getDivision()
     */
    public String getDivisionName() { return division.getDivision(); }

    /**
     * 
     * @return country.getCountry()
     */
    public String getCountryName() { return country.getCountry(); }
}


