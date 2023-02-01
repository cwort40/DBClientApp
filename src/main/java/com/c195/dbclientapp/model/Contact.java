package com.c195.dbclientapp.model;

/**
 *
 The Contact class represents a contact in the database containing information such as contactId, contactName, and
 email.
 */
public class Contact {

    private int contactId;
    private String contactName;
    private String email;

    /**
     *
     * @param contactId
     * @param contactName
     * @param email
     */
    public Contact(int contactId, String contactName, String email) {
        this.contactId = contactId;
        this.contactName = contactName;
        this.email = email;
    }

    /**
     *
     * @return contactId
     */
    public int getContactId() {
        return contactId;
    }

    /**
     *
     * @param contactId
     */
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    /**
     *
     * @return contactName
     */
    public String getContactName() {
        return contactName;
    }

    /**
     *
     * @param contactName
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     *
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

}

