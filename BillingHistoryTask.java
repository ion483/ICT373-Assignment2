/**
 * Background task using javafx.concurrent.Task to compute and generate 
 * a paying customer's billing history asynchronously without blocking the JavaFX UI thread.
 * 
 * @author Yun Shiang
 * @version 2.0
 */

import javafx.concurrent.Task;

public class BillingHistoryTask extends Task<String> {
    private PayingCustomer payingCustomer;
    private double magazineBaseCost;

    public BillingHistoryTask(PayingCustomer payingCustomer, double magazineBaseCost) {
        this.payingCustomer = payingCustomer;
        this.magazineBaseCost = magazineBaseCost;
    }

    @Override
    protected String call() throws Exception {
        // Simulate background computation progress
        updateMessage("Computing billing history...");
        for (int i = 1; i <= 3; i++) {
            Thread.sleep(200); // Simulate background processing delay
            updateProgress(i, 3);
        }

        if (payingCustomer == null) {
            return "Error: No paying customer selected for billing history.";
        }

        // Build the itemized billing history statement
        StringBuilder sb = new StringBuilder();
        sb.append("=== ASYNCHRONOUS BILLING HISTORY REPORT ===\n");
        sb.append("Customer Name: ").append(payingCustomer.getName()).append("\n");
        sb.append("Email: ").append(payingCustomer.getEmail()).append("\n");
        sb.append("Payment Method: ").append(payingCustomer.getPaymentMethod().getPaymentDetails()).append("\n\n");
        
        sb.append("Itemized Charges (4-Week Billing Cycle):\n");
        sb.append(" - Main Magazine Base Fee: $").append(String.format("%.2f", magazineBaseCost * 4)).append("\n");
        sb.append(" - Primary Supplements Fee: $").append(String.format("%.2f", payingCustomer.getWeeklySupplementsCost() * 4)).append("\n");

        if (!payingCustomer.getAssociateCustomers().isEmpty()) {
            sb.append("\nSponsored Associate Accounts:\n");
            for (AssociateCustomer assoc : payingCustomer.getAssociateCustomers()) {
                sb.append(" * ").append(assoc.getName()).append(" - Supplements: $")
                  .append(String.format("%.2f", assoc.getWeeklySupplementsCost() * 4)).append("\n");
            }
        }

        double totalMonthlyCost = payingCustomer.calculateMonthlyTotal(magazineBaseCost);
        sb.append("\nTotal Monthly Billing Amount: $").append(String.format("%.2f", totalMonthlyCost)).append("\n");
        sb.append("===========================================\n");

        return sb.toString();
    }
}