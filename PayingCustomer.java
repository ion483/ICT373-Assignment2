import java.util.ArrayList;
import java.util.List;

/**
 * Title: Magazine Service Test Driver
 * Author: Chang Yun Shiang
 * Student ID: 35702674
 * Date: October 2026
 * Represents a customer who pays for their own subscription and optional associates.
 * File Name: payingCustomer.java
 */
public class PayingCustomer extends Customer {
    /** The payment method registered to this customer for automatic billing. */
    private PaymentMethod paymentMethod;

    /** List of associate customers sponsored by this paying customer. */
    private List<AssociateCustomer> associateCustomers;

    /**
     * Constructs a PayingCustomer with specified name, email address, and payment method.
     *
     * @param name          the full name of the paying customer
     * @param email         the email address of the paying customer
     * @param paymentMethod the payment method used to process invoices
     */
    public PayingCustomer(String name, String email, PaymentMethod paymentMethod) {
        super(name, email);
        this.paymentMethod = paymentMethod;
        this.associateCustomers = new ArrayList<>();
    }

    /**
     * Gets the current payment method linked to this account.
     *
     * @return the associated {@link PaymentMethod}
     */
    public PaymentMethod getPaymentMethod() { return paymentMethod; }

    /**
     * Updates the payment method for this account.
     *
     * @param paymentMethod the new {@link PaymentMethod} to set
     */
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }

    /**
     * Retrieves the list of associate customers sponsored by this paying customer.
     *
     * @return a list of {@link AssociateCustomer} instances
     */
    public List<AssociateCustomer> getAssociateCustomers() { return associateCustomers; }

    /**
     * Links an associate customer to this paying customer's account.
     * <p>
     * Ensures bidirectional linkage by updating the associate's paying customer reference.
     * </p>
     *
     * @param associate the {@link AssociateCustomer} to be added
     */
    public void addAssociateCustomer(AssociateCustomer associate) {
        if (!associateCustomers.contains(associate)) {
            associateCustomers.add(associate);
            associate.setPayingCustomer(this);
        }
    }

    /**
     * Unlinks an associate customer from this paying customer's account.
     * <p>
     * Ensures bidirectional unlinking by clearing the associate's paying customer reference.
     * </p>
     *
     * @param associate the {@link AssociateCustomer} to be removed
     */
    public void removeAssociateCustomer(AssociateCustomer associate) {
        if (associateCustomers.remove(associate)) {
            associate.setPayingCustomer(null);
        }
    }

    /**
     * Calculates the total monthly charge for this primary customer and all linked associates.
     * <p>
     * The total is computed by summing the weekly base magazine cost and supplement costs 
     * for the primary customer and each associate, multiplied by 4 weeks.
     * </p>
     *
     * @param magazineBaseCost the weekly base cost of the main magazine subscription
     * @return the total calculated monthly cost ($) across 4 weeks
     */
    public double calculateMonthlyTotal(double magazineBaseCost) {
        double weeklyTotal = magazineBaseCost + getWeeklySupplementsCost();
        for (AssociateCustomer assoc : associateCustomers) {
            weeklyTotal += magazineBaseCost + assoc.getWeeklySupplementsCost();
        }
        return weeklyTotal * 4;
    }

    /**
     * Generates a fully itemized monthly billing statement email formatted for console output.
     *
     * @param magazineBaseCost the weekly base cost of the main magazine subscription
     * @return a formatted email String containing itemized charges for primary and associate accounts
     */
    public String generateMonthlyBillingEmail(double magazineBaseCost) {
        StringBuilder sb = new StringBuilder();
        sb.append("To: ").append(getEmail()).append("\n");
        sb.append("Subject: Monthly Subscription Statement & Invoice\n");
        sb.append("Dear ").append(getName()).append(",\n\n");
        sb.append("Below is your itemized monthly billing statement (4-Week Period):\n\n");

        // Primary account holder itemization
        sb.append("Primary Customer: ").append(getName()).append("\n");
        sb.append(" - Main Magazine Base Fee (4 weeks): $")
          .append(String.format("%.2f", magazineBaseCost * 4)).append("\n");
        sb.append(" - Supplements Fee (4 weeks): $")
          .append(String.format("%.2f", getWeeklySupplementsCost() * 4)).append("\n");

        // Associate customers itemization
        if (!associateCustomers.isEmpty()) {
            sb.append("\nAssociate Accounts Covered:\n");
            for (AssociateCustomer assoc : associateCustomers) {
                sb.append(" * Associate: ").append(assoc.getName()).append("(").append(assoc.getEmail()).append(")\n");
                sb.append("   - Main Magazine Base Fee (4 weeks): $")
                  .append(String.format("%.2f", magazineBaseCost * 4)).append("\n");
                sb.append("   - Supplements Fee (4 weeks): $")
                  .append(String.format("%.2f", assoc.getWeeklySupplementsCost() * 4)).append("\n");
            }
        }

        double totalMonthlyCost = calculateMonthlyTotal(magazineBaseCost);
        sb.append("\nTotal Monthly Charge: $").append(String.format("%.2f", totalMonthlyCost)).append("\n");
        sb.append("Payment Method Charged: ").append(paymentMethod.getPaymentDetails()).append("\n");
        sb.append("--------------------------------------------------\n");
        return sb.toString();
    }
}