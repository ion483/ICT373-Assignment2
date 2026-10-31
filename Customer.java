/**
 * Title: Magazine Service Test Driver
 * Author: Chang Yun Shiang
 * Student ID: 35702674
 * Date: October 2026
 * Abstract base class representing a generic customer.
 * File Name: Customer.java
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Customer implements Serializable {
    private static final long serialVersionUID = 1L;

    /** The full name of the customer. */
    private String name;

    /** The email address of the customer (used as a unique identifier). */
    private String email;

    /** The list of magazine supplements this customer is subscribed to. */
    private List<Supplement> interestedSupplements;

    /**
     * Constructs a new Customer instance with the specified name and email.
     * <p>
     * Both inputs are stripped of leading and trailing whitespace. Empty or null 
     * arguments are rejected to ensure every customer has a valid identity.
     * </p>
     *
     * @param name                      the full name of the customer (cannot be null or empty)
     * @param email                     the contact email address used as a unique identifier (cannot be null or empty)
     * @throws IllegalArgumentException if the name or email is null, empty, or consists only of whitespace
     */
    public Customer(String name, String email) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name cannot be null or empty.");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer email cannot be null or empty.");
        }
        this.name = name.trim();
        this.email = email.trim();
        this.interestedSupplements = new ArrayList<>();
    }

    /**
     * Retrieves the customer's full name.
     *
     * @return the customer name
     */
    public String getName() { return name; }

    /**
     * Updates the customer's full name.
     *
     * @param name the new full name (cannot be null or empty)
     * @throws IllegalArgumentException if the name is null or empty
     */
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name cannot be null or empty.");
        }
        this.name = name.trim();
    }

    /**
     * Retrieves the customer's email address.
     *
     * @return the customer email
     */
    public String getEmail() { return email; }


    /**
     * Updates the customer's email address.
     *
     * @param email the new unique email address (cannot be null or empty)
     * @throws IllegalArgumentException if the email is null or empty
     */
    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer email cannot be null or empty.");
        }
        this.email = email.trim();
    }


    /**
     * Retrieves the list of supplements the customer is currently subscribed to.
     *
     * @return a List of Supplement objects
     */
    public List<Supplement> getInterestedSupplements() { return interestedSupplements; }

    /**
     * Subscribes the customer to a new supplement if they are not already subscribed.
     *
     * @param supplement the Supplement instance to add
     * @return true if the supplement was added successfully; false if it was already present
     */
    public boolean addSupplement(Supplement supplement) {
        if (!interestedSupplements.contains(supplement)) {
            return interestedSupplements.add(supplement);
        }
        return false;
    }

    /**
     * Unsubscribes the customer from a supplement if currently subscribed.
     *
     * @param supplement the Supplement instance to remove
     * @return true if the supplement was removed successfully; false if it was not found
     */
    public boolean removeSupplement(Supplement supplement) {
        if (interestedSupplements.contains(supplement)) {
            return interestedSupplements.remove(supplement);
        }
        return false;
    }

    /**
     * Calculates the total weekly cost of all supplements subscribed to by this customer.
     *
     * @return the sum of weekly costs for all subscribed supplements as a double
     */
    public double getWeeklySupplementsCost() {
        double total = 0.0;
        for (Supplement s : interestedSupplements) {
            total += s.getWeeklyCost();
        }
        return total;
    }

    /**
     * Generates a formatted weekly notification email detailing new issue availability and active supplements.
     *
     * @param magazineBaseCost the base weekly cost of the main magazine issue
     * @return a formatted email notification String
     */
    public String generateWeeklyEmail(double magazineBaseCost) {
        StringBuilder sb = new StringBuilder();
        sb.append("To: ").append(email).append("\n");
        sb.append("Subject: Your Weekly Magazine is Ready!\n");
        sb.append("Dear ").append(name).append(",\n\n");
        sb.append("Your main magazine issue for this week is ready to read.\n");
        sb.append("Current Subscribed Supplements:\n");
        
        if (interestedSupplements.isEmpty()) {
            sb.append(" - None\n");
        } else {
            for (Supplement s : interestedSupplements) {
                sb.append(" - ").append(s.getName())
                  .append(" ($").append(String.format("%.2f", s.getWeeklyCost())).append("/week)\n");
            }
        }
        sb.append("--------------------------------------------------\n");
        return sb.toString();
    }

    /**
     * Compares this customer to another object based on case-insensitive email equality.
     *
     * @param o the object to compare with
     * @return true if the given object is a Customer with the same email address; false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;
        Customer customer = (Customer) o;
        return email != null && customer.email != null 
            && email.trim().equalsIgnoreCase(customer.email.trim());
    }

    /**
     * Returns a hash code derived from the customer's normalized lower-case email address.
     *
     * @return an integer hash code
     */
    @Override
    public int hashCode() {
        return email != null ? email.trim().toLowerCase().hashCode() : 0;
    }
}

