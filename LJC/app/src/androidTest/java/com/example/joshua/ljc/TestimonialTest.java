package com.example.joshua.ljc;


import com.example.joshua.ljc.DataModel.Interfaces.IProject;
import com.example.joshua.ljc.DataModel.Interfaces.ITestimonial;
import com.example.joshua.ljc.DataModel.Project;
import com.example.joshua.ljc.DataModel.Testimonial;

import junit.framework.Assert;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Joshua on 21/02/2017.
 */
public class TestimonialTest {

    private ITestimonial testTestimonial;

    public TestimonialTest() throws Exception {
        testTestimonial = new Testimonial("Example author name", "Example quote given by author", "Example testimonial UUID");
    }

    @Test
    public void getName() {
        System.out.println("getName: Test to see if a Testimonials Name can be retrieved.");
        String actualResult = "N/A";
        try {
            actualResult = testTestimonial.getName();
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        assertEquals("The retrieval of a valid testimonial's name was unsuccessful.", actualResult,"Example author name");
    }

    @Test
    public void setName() throws Exception {
        System.out.println("setName: Test to see if a testimonial's author name can be set to a null value.");
        try {
            testTestimonial.setName(null);
            Assert.fail("Entering a NULL author name didn't throw a NullPointerException.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("setName: Test to see if a testimonial's author name can be set to a value with an invalid length.");
        try {
            testTestimonial.setName("sm");
            Assert.fail("Entering an author name with an invalid length didn't throw an Exception.");
        }catch (Exception ex){
        }
        System.out.println("setName: Test to see if a testimonial's author name can be set to a value which contains numbers.");
        try {
            testTestimonial.setName("this isn't valid 123");
            Assert.fail("Entering an author name which contains numbers didn't throw an Exception.");
        }catch (Exception ex){
        }
        System.out.println("setName: Test to see if a valid author name can be set.");
        try {
            testTestimonial.setName("Mr Example Test");
        }catch (Exception ex){
            Assert.fail("Entering a valid author name threw an Exception.");
        }finally{
            assertEquals("The author name was set to an unspecified value.", testTestimonial.getName(),"Mr Example Test");
        }
        resetTestimonial();
    }

    @Test
    public void getQuote(){
        System.out.println("getQuote: Test to see if a Testimonials quote can be retrieved.");
        String actualResult = "N/A";
        try {
            actualResult = testTestimonial.getQuote();
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        assertEquals("The retrieval of a valid testimonial's quote was unsuccessful.", actualResult,"Example quote given by author");
    }

    @Test
    public void setQuoteString() throws Exception {
        System.out.println("setQuoteString: Test to see if a testimonial's quote can be set to a null value.");
        try {
            testTestimonial.setQuoteString(null);
            Assert.fail("Entering a NULL quote didn't throw a NullPointerException.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("setQuoteString: Test to see if a testimonial's quote can be set to a value with an invalid length.");
        try {
            testTestimonial.setQuoteString("in");
            Assert.fail("Entering an quote with an invalid length didn't throw an Exception.");
        }catch (Exception ex){
        }
        System.out.println("setQuoteString: Test to see if a valid quote can be set.");
        try {
            testTestimonial.setQuoteString("Valid example quote, minimum of 20 characters.");
        }catch (Exception ex){
            Assert.fail("Entering a valid quote threw an Exception.");
        }finally{
            assertEquals("The quote was set to an unspecified value.", testTestimonial.getQuote(),"Valid example quote, minimum of 20 characters.");
        }
        resetTestimonial();
    }

    @Test
    public void getUUID(){
        System.out.println("getUUID: Test to see if a Testimonials UUID can be retrieved.");
        String actualResult = "N/A";
        try {
            actualResult = testTestimonial.getUUID();
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        assertEquals("The retrieval of a valid testimonial's UUID was unsuccessful.", actualResult,"Example testimonial UUID");
    }

    @Test
    public void setUUID() throws Exception {
        System.out.println("setUUID: Test to see if a testimonial's UUID can be set to a null value.");
        try {
            testTestimonial.setUUID(null);
            Assert.fail("Entering a NULL UUID didn't throw a NullPointerException.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("setUUID: Test to see if a valid UUID can be set.");
        try {
            testTestimonial.setUUID("Valid UUID");
        }catch (Exception ex){
            Assert.fail("Entering a valid UUID threw an Exception.");
        }finally{
            assertEquals("The UUID was set to an unspecified value.", testTestimonial.getUUID(),"Valid UUID");
        }
        resetTestimonial();
    }

    private void resetTestimonial() throws Exception {
        testTestimonial = new Testimonial("Example author name", "Example quote given by author", "Example testimonial UUID");
    }
}
