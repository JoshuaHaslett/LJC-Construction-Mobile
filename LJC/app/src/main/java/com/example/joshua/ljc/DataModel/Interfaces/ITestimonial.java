package com.example.joshua.ljc.DataModel.Interfaces;

/**
 * Created by Joshua on 01/04/2017.
 * This interface ensures that objects which implement it, contain the necessary methods to perform the actions of a "Client Testimonial".
 */
public interface ITestimonial {
    /**
     * This gets a testimonial's author's full name.
     *
     * @return String value, representing a clients name.
     */
    String getName();


    /**
     * Sets the client's name which is attached to a testimonial.
     *
     * @param authorName String value representing a clients full name (should be the author of the testimonial).
     * @return Returns a boolean value which represents whether the testimonial's author's name was successfully changed.
     * @throws Exception This exception is thrown if the new author name doesn't meet the minimum requirements e.g. length > 3, etc.
     */
    boolean setName(String authorName) throws Exception;

    /**
     * Gets the quote made by the author.
     *
     * @return String value of the actual testimonial.
     */
    String getQuote();

    /**
     * Sets the body of the testimonial, the authors name will remain unchanged.
     *
     * @param authorQuote The String value which represents the actual quote made by a client.
     * @return Returns a boolean value, representing whether the quote was actually successfully changed.
     * @throws Exception This exception is thrown if the new quote doesn't exist or doesn't meet a certain length.
     */
    boolean setQuoteString(String authorQuote) throws Exception;

    /**
     * Gets the Fire Base key where the testimonial is stored. This is used for updates and removals.
     *
     * @return String value of the Fire Base key.
     */
    String getUUID();

    /**
     * Sets the key for where the testimonial will be stored in Fire Base.
     *
     * @param id The String value of the new Fire Base directory (Key).
     * @return Returns a boolean value, representing whether the Fire Base key was successfully changed.
     * @throws Exception This exception is thrown if the key is null, which means Fire Base was unable to generate a key.
     */
    boolean setUUID(String id) throws Exception;
}
