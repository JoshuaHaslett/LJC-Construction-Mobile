package com.example.joshua.ljc.DataModel;

import com.example.joshua.ljc.DataModel.Interfaces.ITestimonial;

/**
 * Created by Joshua on 01/04/2017.
 */

public class Testimonial implements ITestimonial {

    private String name;
    private String quote;
    private String uUID;

    public Testimonial(){
        name = "N/A";
        quote = "N/A";
        uUID = "N/A";
    }

    public Testimonial(String authorName, String authorQuote) throws Exception {
        if(authorName == null || authorQuote == null){
            throw new Exception("Please complete both fields.");
        }
        if (authorName.matches(".*\\d+.*") || authorName.length() < 3 || authorName.length() > 30){
            throw new Exception("The clients name must be between 3-30 characters in length, and contain no numbers.");
        }
        if (authorQuote.length() < 3 || authorQuote.length() > 300){
            throw new Exception("The clients quote must be between 8-300 characters in length.");
        }
        authorName = authorName.substring(0,1).toUpperCase() + authorName.substring(1);
        authorQuote = authorQuote.substring(0,1).toUpperCase() + authorQuote.substring(1);
        name = authorName;
        quote = authorQuote;
    }

    public Testimonial(String authorName, String authorQuote, String id) throws Exception {

        if(authorName == null || authorQuote == null){
            throw new Exception("Please complete both fields.");
        }
        if (authorName.matches(".*\\d+.*")  || authorName.length() < 3 || authorName.length() > 30){
            throw new Exception("The clients name must be between 3-30 characters in length, and contain no numbers.");
        }
        if (authorQuote.length() < 3 || authorQuote.length() > 300){
            throw new Exception("The clients quote must be between 8-300 characters in length.");
        }
        if (id == null){
            throw new Exception("Fire base was unable to generate an ID, please try again later.");
        }
        authorName = authorName.substring(0,1).toUpperCase() + authorName.substring(1);
        authorQuote = authorQuote.substring(0,1).toUpperCase() + authorQuote.substring(1);
        name = authorName;
        quote = authorQuote;
        uUID = id;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean setName(String authorName) {
        name = authorName;
        return true;
    }

    @Override
    public String getQuote() {
        return quote;
    }

    @Override
    public boolean setQuoteString(String authorQuote) {
        quote = authorQuote;
        return true;
    }

    @Override
    public String getUUID() {
        return uUID;
    }

    @Override
    public boolean setUUID(String id) {
        uUID = id;
        return true;
    }
}
