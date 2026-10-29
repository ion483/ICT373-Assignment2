/**
 * Title: Magazine Service Test Driver
 * Author: Chang Yun Shiang
 * Student ID: 35702674
 * Date: October 2026
 * Represents a bank account payment method for direct debit.
 * File Name: BankAccount.java
 */
public class BankAccount extends PaymentMethod {
    /** The Bank-State-Branch (BSB) number identifying the bank branch. */
    private String bsb;

    /** The individual bank account number. */
    private String accountNumber;

    /**
     * Constructs a new BankAccount payment method with specified details.
     * <p>
     * Null arguments for BSB and account number are automatically converted to 
     * empty strings, and leading/trailing whitespace is trimmed.
     * </p>
     *
     * @param accountName   the full name registered on the bank account
     * @param bsb           the BSB code associated with the account branch
     * @param accountNumber the unique account identification number
     */
    public BankAccount(String accountName, String bsb, String accountNumber) {
        super(accountName);
        this.bsb = bsb != null ? bsb.trim() : "";
        this.accountNumber = accountNumber != null ? accountNumber.trim() : "";
    }

    /**
     * Retrieves the BSB code of the bank account.
     *
     * @return the BSB code as a String
     */
    public String getBsb() { return bsb; }

    /**
     * Retrieves the account number of the bank account.
     *
     * @return the account number as a String
     */
    public String getAccountNumber() { return accountNumber; }

    /**
     * Formats and returns the detailed bank account summary for payment identification.
     *
     * @return a formatted string containing account name, BSB, and account number
     */
    @Override
    public String getPaymentDetails() {
        return "Bank Account [Account Name: " + getAccountName() 
                + ", BSB: " + bsb + ", Acc #: " + accountNumber + "]";
    }
}