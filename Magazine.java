import java.util.ArrayList;
import java.util.List;

/**
 * Title: Magazine Service Test Driver
 * Author: Chang Yun Shiang
 * Student ID: 35702674
 * Date: October 2026
 * Core domain class managing magazine pricing, available supplements, and customer base.
 * File Name: Magazine.java
 */
public class Magazine {
    /** The base weekly subscription cost for the main magazine. */
    private double weeklyBaseCost;

    /** The list of all supplements available for subscription. */
    private List<Supplement> availableSupplements;

    /** The list of all registered active customers (both paying and associate). */
    private List<Customer> customers;

    /**
     * Constructs a new Magazine service with a specified weekly base cost.
     *
     * @param weeklyBaseCost the weekly price for the main magazine issue
     */
    public Magazine(double weeklyBaseCost) {
        this.weeklyBaseCost = weeklyBaseCost;
        this.availableSupplements = new ArrayList<>();
        this.customers = new ArrayList<>();
    }

    /**
     * Retrieves the base weekly subscription cost of the main magazine.
     *
     * @return the weekly base cost as a double
     */
    public double getWeeklyBaseCost() { return weeklyBaseCost; }

    /**
     * Updates the base weekly subscription cost of the main magazine.
     *
     * @param weeklyBaseCost the new weekly base cost to set
     */
    public void setWeeklyBaseCost(double weeklyBaseCost) { this.weeklyBaseCost = weeklyBaseCost; }

    /**
     * Retrieves the list of all available supplements offered by the magazine.
     *
     * @return a List of Supplement objects
     */
    public List<Supplement> getAvailableSupplements() { return availableSupplements; }

    /**
     * Retrieves the list of all active registered customers.
     *
     * @return a List of Customer objects
     */
    public List<Customer> getCustomers() { return customers; }

    /**
     * Adds a new supplement to the magazine's catalog if it is not already present.
     *
     * @param supplement the Supplement instance to add
     * @return true if the supplement was added successfully; false if it already exists
     */
    public boolean addSupplement(Supplement supplement) {
        if (!availableSupplements.contains(supplement)) {
            return availableSupplements.add(supplement);
        }
        return false;
    }

    /**
     * Registers a new customer with the magazine service if not already registered.
     *
     * @param customer the Customer instance to add
     * @return true if the customer was registered successfully; false if already registered
     */
    public boolean addCustomer(Customer customer) {
        if (!customers.contains(customer)) {
            return customers.add(customer);
        }
        return false;
    }

    /**
     * Removes an existing customer from the magazine service.
     * <p>
     * If the customer is a PayingCustomer, a cascade deletion occurs to remove all 
     * associated customers paid for by them. If the customer is an AssociateCustomer, 
     * they are unlinked from their PayingCustomer before removal.
     * </p>
     *
     * @param customer the Customer instance to remove
     * @return true if the customer was successfully removed; false if null or not found
     */
    public boolean removeCustomer(Customer customer) {
        if (customer == null || !customers.contains(customer)) {
            return false;
        }

        if (customer instanceof PayingCustomer) {
            PayingCustomer pc = (PayingCustomer) customer;
            
            // Cascade delete: remove all associated customers paid for by this customer
            List<AssociateCustomer> associates = new ArrayList<>(pc.getAssociateCustomers());
            for (AssociateCustomer assoc : associates) {
                pc.removeAssociateCustomer(assoc);
                customers.remove(assoc); // Remove associate from active magazine customer list
            }
        } else if (customer instanceof AssociateCustomer) {
            AssociateCustomer ac = (AssociateCustomer) customer;
            if (ac.getPayingCustomer() != null) {
                ac.getPayingCustomer().removeAssociateCustomer(ac);
            }
        }

        return customers.remove(customer);
    }
}