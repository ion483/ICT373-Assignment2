/**
 * Title: Magazine Service Test Driver
 * Author: Chang Yun Shiang
 * Student ID: 35702674
 * Date: October 2026
 * Abstract class representing a generic payment method.
 * File Name: PaymentMethod.java
 */

import java.io.Serializable;

public abstract class PaymentMethod implements Serializable {
    private static final long serialVersionUID = 1L;

    /** The full legal name of the account holder associated with this payment method. */
    private String accountName;

    /**
     * Constructs a new PaymentMethod instance with the specified account holder name.
     *
     * @param accountName the name of the primary cardholder or account owner; defaults to an empty 
     *                    string if {@code null} is provided
     */
    public PaymentMethod(String accountName) {
        this.accountName = accountName != null ? accountName.trim() : "";
    }

    /**
     * Retrieves the account holder's name.
     *
     * @return the name associated with this payment method
     */
    public String getAccountName() { return accountName; }

    /**
     * Abstract contract method to retrieve formatted payment account details suitable for billing statements.
     *
     * @return a formatted String containing key payment method details (e.g., masked card numbers or BSB/Account numbers)
     */
    public abstract String getPaymentDetails();
}