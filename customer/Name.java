package com.src.customer;

import java.util.Arrays;
import java.util.Objects;

public class Name {
//    private static final long serialVersionUID = 1L;

    private final String firstName;
    private final String middleName; // This can be optional
    private final String lastName;

    /**
     * Constructs a Name object with the given components.
     *
     * @param firstName  The first name (required).
     * @param middleName The middle name (optional).
     * @param lastName   The last name (required).
     * @throws IllegalArgumentException if firstName or lastName are null or empty.
     */
    public Name(String firstName, String middleName, String lastName) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be null or empty");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be null or empty");
        }

        this.firstName = firstName.trim();
        this.middleName = (middleName != null && !middleName.trim().isEmpty()) ? middleName.trim() : null;
        this.lastName = lastName.trim();
    }

    // Constructor accepting fullName and parsing it
    public Name(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be null or empty");
        }

        // Split the full name into parts
        String[] parts = fullName.trim().split("\\s+");
        if (parts.length == 1) {
            // Only first name is provided
            this.firstName = parts[0];
            this.middleName = null;
            this.lastName = "";
        } else if (parts.length == 2) {
            // First and last name
            this.firstName = parts[0];
            this.middleName = null;
            this.lastName = parts[1];
        } else {
            // First name, middle name(s), last name
            this.firstName = parts[0];
            this.lastName = parts[parts.length - 1];
            this.middleName = String.join(" ", Arrays.copyOfRange(parts, 1, parts.length - 1));
        }
    }

    // Getters
    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName; // Can be null
    }

    public String getLastName() {
        return lastName;
    }

    // Method to get the full name
    public String getFullName() {
        StringBuilder sb = new StringBuilder(firstName);
        if (middleName != null) {
            sb.append(" ").append(middleName);
        }
        sb.append(" ").append(lastName);
        return sb.toString();
    }

    // Override toString, equals, and hashCode methods
    @Override
    public String toString() {
        return getFullName();
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        Name name = (Name) o;
//        return firstName.equalsIgnoreCase(name.firstName) &&
//                Objects.equals(middleName, name.middleName) &&
//                lastName.equalsIgnoreCase(name.lastName);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(firstName.toLowerCase(), middleName, lastName.toLowerCase());
//    }
}