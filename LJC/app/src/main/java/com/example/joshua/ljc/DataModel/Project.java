package com.example.joshua.ljc.DataModel;

import com.example.joshua.ljc.DataModel.Interfaces.IProject;

/**
 * Created by Joshua on 27/03/2017.
 */

public class Project implements IProject{

    private String name;
    private String description;
    private String image;

    public Project(){
        name = "N/A";
        description = "N/A";
        image = "N/A";
    }

    public Project(String tag, String info, String img) throws Exception {

        if(tag == null || info == null){
            throw new Exception("Please complete all fields.");
        }
        if(tag.length() < 3 || tag.length() > 30){
            throw new Exception("The project title must be between 3 and 30 characters in length.");
        }
        if(info.length() < 20 || info.length() > 1000){
            throw new Exception("The project description must be between 20 and 1000 characters in length.");
        }
        if (img == null){
            throw new Exception("Please add an image to the project.");
        }

        //format
        tag = tag.substring(0,1).toUpperCase() + tag.substring(1);
        info = info.substring(0,1).toUpperCase() + info.substring(1);

        name = tag;
        description = info;
        image = img;
    }


    @Override
    public String getUUID() {
        return image;
    }

    @Override
    public String getName() {
        return name;
    }
    @Override
    public void setName(String title) throws Exception {
        if(title.length() < 3 || title.length() > 30){
            throw new Exception("The project title must be between 3 and 30 characters in length.");
        }
        title = title.substring(0,1).toUpperCase() + title.substring(1);
        this.name = title;
    }
    @Override
    public String getDescription() {
        return description;
    }
    @Override
    public void setDescription(String info) throws Exception {
        if(info.length() < 20 || info.length() > 1000){
            throw new Exception("The project description must be between 20 and 1000 characters in length.");
        }
        info = info.substring(0,1).toUpperCase() + info.substring(1);
        this.description = info;
    }

    @Override
    public String getImage() {
        return image;
    }

}
