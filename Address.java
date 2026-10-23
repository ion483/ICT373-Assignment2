

/**
 * Title: Magazine Service Test Driver
 * Author: Chang Yun Shiang
 * Student ID: 35702674
 * Date: October 2026
 * Represents a physical address for a customer.
 */
public class Address {
    /** The street address line (e.g., "123 Main St"). */
    private String street;

    /** The suburb or city name (e.g., "Sydney"). */
    private String suburb;

    /** The state or territory abbreviation or name. */
    private String state;

    /** The postal or ZIP code. */
    private String postCode;

    /**
     * Constructs a new Address object with specified details.
     * <p>
     * Null arguments are converted to empty strings, and leading/trailing
     * whitespace is automatically trimmed.
     * </p>
     *
     * @param street   the street number and name
     * @param suburb   the suburb, city, or locality
     * @param state    the state or territory code
     * @param postCode the postal or ZIP code
     */
    public Address(String street, String suburb, String state, String postCode) {
        this.street = street != null ? street.trim() : "";
        this.suburb = suburb != null ? suburb.trim() : "";
        this.state = state != null ? state.trim() : "";
        this.postCode = postCode != null ? postCode.trim() : "";
    }

    /**
     * Gets the street address line.
     *
     * @return the street address
     */
    public String getStreet() { return street; }

    /**
     * Gets the suburb or locality.
     *
     * @return the suburb name
     */
    public String getSuburb() { return suburb; }

    /**
     * Gets the state or territory.
     *
     * @return the state name or abbreviation
     */
    public String getState() { return state; }

    /**
     * Gets the postal code.
     *
     * @return the postal code
     */
    public String getPostCode() { return postCode; }

    /**
     * Returns a single-line formatted representation of the address.
     *
     * @return a formatted string in the format "Street, Suburb State PostCode"
     */
    @Override
    public String toString() {
        return street + ", " + suburb + " " + state + " " + postCode;
    }
}