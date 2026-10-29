/**
 * Title: Magazine Service Test Driver
 * Author: Chang Yun Shiang
 * Student ID: 35702674
 * Date: October 2026
 * Represents a credit card payment method.
 * File Name: CreditCard.java
 */
public class CreditCard extends PaymentMethod {
    /** The full credit card number string. */
    private String cardNumber;

    /** The card expiry date (formatted as MM/YY). */
    private String expiryDate;

    /**
     * Constructs a new CreditCard payment method with specified details.
     * <p>
     * Null arguments for card number and expiry date are automatically converted 
     * to empty strings, and leading/trailing whitespace is trimmed.
     * </p>
     *
     * @param accountName the full name of the credit card holder
     * @param cardNumber  the full card number digit string
     * @param expiryDate  the expiration date string (e.g., "12/28")
     */
    public CreditCard(String accountName, String cardNumber, String expiryDate) {
        super(accountName);
        this.cardNumber = cardNumber != null ? cardNumber.trim() : "";
        this.expiryDate = expiryDate != null ? expiryDate.trim() : "";
    }

    /**
     * Retrieves the unmasked credit card number.
     *
     * @return the card number as a String
     */
    public String getCardNumber() { return cardNumber; }

    /**
     * Retrieves the credit card expiration date.
     *
     * @return the expiry date as a String
     */
    public String getExpiryDate() { return expiryDate; }

    /**
     * Formats and returns masked credit card details for secure displaying and billing summaries.
     * <p>
     * Only reveals the final four digits of the credit card number.
     * </p>
     *
     * @return a formatted string showing holder name, masked card number, and expiry date
     */
    @Override
    public String getPaymentDetails() {
        String lastFour = cardNumber.length() >= 4 
                ? cardNumber.substring(cardNumber.length() - 4) 
                : cardNumber;
        return "Credit Card [Holder: " + getAccountName() + ", Card #: **** **** **** " 
                + lastFour + ", Expiry: " + expiryDate + "]";
    }
}