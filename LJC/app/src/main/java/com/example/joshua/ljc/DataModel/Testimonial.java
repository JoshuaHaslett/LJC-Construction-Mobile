package com.example.joshua.ljc.DataModel;

import com.example.joshua.ljc.DataModel.Interfaces.ITestimonial;

/**
 * Created by Joshua on 01/04/2017.
 * The "Testimonial" object represents the testimonials given by the construction companies clients (or other companies that have interacted with this business).
 */
public class Testimonial implements ITestimonial {
    private String name;
    private String quote;
    private String uUID;

    /**
     * This is the default constructor for a clients testimonial. This can be used to create a Fire Base key, allowing the rest of the testimonial details to be added later.
     */
    public Testimonial() {
        name = "N/A";
        quote = "N/A";
        uUID = "N/A";
    }

    /**
     * This constructor facilitates the creation of a full testimonial object. This includes both the authors full name and their quote/testimonial in regard to the services provided by JLC Construction.
     * @param authorName String value which represents the full name of the testimonial's author.
     * @param authorQuote String value of the clients quote. Note: The client should confirm that they are happy for this quote to be published.
     * @throws Exception This exception is thrown if either of the string variables don't meet the minimum requirements needed to keep the data consistent and valid.
     */
    public Testimonial(String authorName, String authorQuote) throws Exception {
        if (authorName == null || authorQuote == null) {
            throw new Exception("Please complete both fields.");
        }
        if (authorName.matches(".*\\d+.*") || authorName.length() < 3 || authorName.length() > 30) {
            throw new Exception("The clients name must be between 3-30 characters in length, and contain no numbers.");
        }
        if (authorQuote.length() < 3 || authorQuote.length() > 300) {
            throw new Exception("The clients quote must be between 8-300 characters in length.");
        }
        authorName = authorName.substring(0, 1).toUpperCase() + authorName.substring(1);
        authorQuote = authorQuote.substring(0, 1).toUpperCase() + authorQuote.substring(1);
        name = authorName;
        quote = authorQuote;
    }

    /**
     *
     * @param authorName
     * @param authorQuote
     * @param id
     * @throws Exception
     */
    public Testimonial(String authorName, String authorQuote, String id) throws Exception {

        // Variable checks
        if (authorName == null || authorQuote == null) {
            throw new Exception("Please complete both fields.");
        }
        if (authorName.matches(".*\\d+.*") || authorName.length() < 3 || authorName.length() > 30) {
            throw new Exception("The clients name must be between 3-30 characters in length, and contain no numbers.");
        }
        if (authorQuote.length() < 3 || authorQuote.length() > 300) {
            throw new Exception("The clients quote must be between 8-300 characters in length.");
        }
        if (id == null) {
            throw new Exception("Fire base was unable to generate an ID, please try again later.");
        }

        // Variable formatting
        authorName = authorName.substring(0, 1).toUpperCase() + authorName.substring(1);
        authorQuote = authorQuote.substring(0, 1).toUpperCase() + authorQuote.substring(1);

        // Variable allocation
        name = authorName;
        quote = authorQuote;
        uUID = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean setName(String authorName) throws Exception {
        if (authorName == null) {
            throw new NullPointerException("Please enter the clients full name. This cannot be left empty.");
        }
        if (authorName.matches(".*\\d+.*") || authorName.length() < 3 || authorName.length() > 30) {
            throw new Exception("The clients name must be between 3-30 characters in length, and contain no numbers.");
        }
        authorName = authorName.substring(0, 1).toUpperCase() + authorName.substring(1);
        name = authorName;
        return true;
    }

    @Override
    public String getQuote() {
        return quote;
    }

    @Override
    public boolean setQuoteString(String authorQuote) throws Exception {
        if (authorQuote == null) {
            throw new Exception("Please enter the quote given by the client. This cannot be left empty.");
        }
        if (authorQuote.length() < 3 || authorQuote.length() > 300) {
            throw new Exception("The clients quote must be between 8-300 characters in length.");
        }
        authorQuote = authorQuote.substring(0, 1).toUpperCase() + authorQuote.substring(1);
        quote = authorQuote;
        return true;
    }

    @Override
    public String getUUID() {
        return uUID;
    }

    @Override
    public boolean setUUID(String id) throws Exception {
        if (id == null) {
            throw new Exception("The provided Fire Base key was null, please provide a valid key.");
        }
        uUID = id;
        return true;
    }
}
