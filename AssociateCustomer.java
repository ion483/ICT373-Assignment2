/**
 * Title: Magazine Service Test Driver
 * Author: Chang Yun Shiang
 * Student ID: 35702674
 * Date: October 2026
 * Represents an associate customer paid for by a PayingCustomer.
 * File Name: AssociateCustomer.java
 */

import java.io.Serializable;

public class AssociateCustomer extends Customer implements Serializable {
    private static final long serialVersionUID = 1L;

    /** The paying customer responsible for handling payment for this associate. */
    private PayingCustomer payingCustomer;

    /**
     * Constructs an AssociateCustomer with a name and email, leaving the paying customer unassigned.
     *
     * @param name  the full name of the associate customer
     * @param email the email address of the associate customer
     * @param address the physical address of the associate customer
     */
    public AssociateCustomer(String name, String email, Address address) {
        super(name, email, address);
    }

    /**
     * Constructs an AssociateCustomer with a name, email, and designated paying customer.
     *
     * @param name           the full name of the associate customer
     * @param email          the email address of the associate customer
     * @param address        the physical address of the associate customer
     * @param payingCustomer the paying customer responsible for billing
     */
    public AssociateCustomer(String name, String email, Address address, PayingCustomer payingCustomer) {
        super(name, email, address);
        this.payingCustomer = payingCustomer;
    }

    /**
     * Retrieves the paying customer associated with this account.
     *
     * @return the PayingCustomer instance responsible for payment
     */
    public PayingCustomer getPayingCustomer() { return payingCustomer; }

    /**
     * Sets or updates the paying customer responsible for this account.
     *
     * @param payingCustomer the PayingCustomer instance to assign
     */
    public void setPayingCustomer(PayingCustomer payingCustomer) { this.payingCustomer = payingCustomer; }
}