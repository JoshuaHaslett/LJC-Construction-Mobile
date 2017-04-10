package com.example.joshua.ljc;


import org.junit.Test;

import com.example.joshua.ljc.DataModel.Interfaces.IProject;
import com.example.joshua.ljc.DataModel.Project;

import junit.framework.Assert;

import static org.junit.Assert.assertEquals;

/**
 * Created by Joshua on 21/02/2017.
 */
public class ProjectTest {

    private IProject testProject;

    public ProjectTest() throws Exception {
        testProject = new Project("Example title", "Example description over 20 characters", "Example image/UUID");
    }


    @Test
    public void getUUID() throws Exception {
        System.out.println("getUUID: Test to see if a projects UUID can be retrieved.");
        String actualResult = "N/A";
        try {
            actualResult = testProject.getUUID();
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        assertEquals("The retrieval of a valid projects UUID was unsuccessful.", actualResult,"Example image/UUID");
    }


    @Test
    public void getName() {
        System.out.println("getName: Test to see if a projects Title/Name can be retrieved.");
        String actualResult = "N/A";
        try {
            actualResult = testProject.getName();
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        assertEquals("The retrieval of a valid projects title was unsuccessful.", actualResult,"Example title");
    }


    @Test
    public void setName() throws Exception {
        System.out.println("setName: Test to see if a projects Title/Name can be set to a null value.");
        try {
            testProject.setName(null);
            Assert.fail("Entering a NULL project name didn't throw a NullPointerException.");
        }catch (NullPointerException ex){
        }
        System.out.println("setName: Test to see if a projects Title/Name can be set to a invalid value.");
        try {
            testProject.setName("0");
            Assert.fail("Entering an invalid project name didn't throw an Exception.");
        }catch (Exception ex){
        }
        System.out.println("setName: Test to see if a valid project Title/Name can be set.");
        try {
            testProject.setName("Torquay, Devon");
        }catch (Exception ex){
            Assert.fail("Entering an valid project name threw an Exception.");
        }finally{
            assertEquals("The project name was set to an unspecified value.", testProject.getName(),"Torquay, Devon");
        }
        resetProject();
    }

    @Test
    public void getDescription(){
        System.out.println("getDescription: Test to see if a projects description can be retrieved.");
        String actualResult = "N/A";
        try {
            actualResult = testProject.getDescription();
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        assertEquals("The retrieval of a valid projects description was unsuccessful.", actualResult,"Example description over 20 characters");
    }

    @Test
    public void setDescription() throws Exception {
        System.out.println("setDescription: Test to see if a projects Description can be set to a null value.");
        try {
            testProject.setDescription(null);
            Assert.fail("Entering a NULL project description didn't throw a NullPointerException.");
        }catch (NullPointerException ex){
        }
        System.out.println("setDescription: Test to see if a projects Description can be set to a invalid value.");
        try {
            testProject.setDescription("0");
            Assert.fail("Entering an invalid project Description didn't throw an Exception.");
        }catch (Exception ex){
        }
        System.out.println("setDescription: Test to see if a valid project Description can be set.");
        try {
            testProject.setDescription("This is a new valid description; third test.");
        }catch (Exception ex){
            Assert.fail("Entering an valid project name threw an Exception.");
        }finally{
            assertEquals("The project Description was set to an unspecified value.", testProject.getDescription(),"This is a new valid description; third test.");
        }
        resetProject();
    }

    @Test
    public void getImage() {
        System.out.println("getImage/UUID: Test to see if a projects Image/project key can be retrieved.");
        String actualResult = "N/A";
        try {
            actualResult = testProject.getImage();
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        assertEquals("The retrieval of a valid projects UUID/Image key was unsuccessful.", actualResult,"Example image/UUID");
    }

    private void resetProject() throws Exception {
        testProject = new Project("Example title", "Example description over 20 characters", "Example image/UUID");
    }
}
