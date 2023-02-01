package com.c195.dbclientapp.model;

/**
 *
 The TypeReport class represents a type report in the database containing information such as the total number of
 appointments of a certain type and the type itself.
 */
public class TypeReport {
    private final int total;
    private final String type;

    /**
     *
     * @param total
     * @param type
     */
    public TypeReport(int total, String type) {
        this.total = total;
        this.type = type;
    }

    /**
     *
     * @return total
     */
    public int getTotal() {
        return total;
    }

    /**
     *
     * @return type
     */
    public String getType() {
        return type;
    }
}

