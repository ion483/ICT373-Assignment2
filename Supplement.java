/**
 * Title: Magazine Service Test Driver
 * Author: Chang Yun Shiang
 * Student ID: 35702674
 * Date: October 2026
 * Represents a weekly supplement that can be added to a customer's subscription.
 * File Name: Supplement.java
 */

import java.io.Serializable;

public class Supplement implements Serializable {
    private static final long serialVersionUID = 1L;

    /** The display name of the supplement. */
    private String name;

    /** The weekly recurring cost of this supplement in dollars ($). */
    private double weeklyCost;

    /**
     * Constructs a new Supplement instance with the specified name and weekly cost.
     *
     * @param name       the display name of the supplement; defaults to empty string if {@code null}
     * @param weeklyCost the recurring weekly cost of the supplement ($)
     * @throws IllegalArgumentException if the name is null, empty, or consists only of whitespace
     */
    public Supplement(String name, double weeklyCost) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Supplement name cannot be null or empty.");
        }
        this.name = name.trim();
        this.weeklyCost = weeklyCost;
    }

    /**
     * Gets the name of the supplement.
     *
     * @return the supplement name
     */
    public String getName() { return name; }

    /**
     * Sets the name of the supplement, automatically trimming leading and trailing whitespace.
     *
     * @param name the new supplement name.
     * @throws IllegalArgumentException if the name is null, empty, or consists only of whitespace
     */
    public void setName(String name) { 
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Supplement name cannot be null or empty.");
        }
        this.name = name.trim();
    }

    /**
     * Gets the weekly subscription cost of the supplement.
     *
     * @return the weekly cost ($)
     */
    public double getWeeklyCost() { return weeklyCost; }

    /**
     * Updates the weekly subscription cost of the supplement.
     *
     * @param weeklyCost the new weekly cost ($) to set
     */
    public void setWeeklyCost(double weeklyCost) { this.weeklyCost = weeklyCost; }

    /**
     * Returns a string representation of the supplement containing its name and formatted weekly cost.
     *
     * @return a formatted String (e.g., "Tech & Gadgets ($4.50/week)")
     */
    @Override
    public String toString() {
        return name + " ($" + String.format("%.2f", weeklyCost) + "/week)";
    }

    /**
     * Compares this supplement with another object for equality.
     * <p>
     * Two supplements are considered equal if their names match on a case-insensitive, trimmed basis.
     * </p>
     *
     * @param o the reference object with which to compare
     * @return {@code true} if this object is equal to the argument; {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Supplement that = (Supplement) o;
        return name != null && that.name != null 
            && name.trim().equalsIgnoreCase(that.name.trim());
    }

    /**
     * Generates a hash code for this supplement based on its case-insensitive trimmed name.
     *
     * @return integer hash code derived from the lowercase supplement name
     */
    @Override
    public int hashCode() {
        return name != null ? name.trim().toLowerCase().hashCode() : 0;
    }
}