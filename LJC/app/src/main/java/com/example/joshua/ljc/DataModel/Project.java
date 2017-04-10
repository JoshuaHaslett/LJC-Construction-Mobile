package com.example.joshua.ljc.DataModel;

import com.example.joshua.ljc.DataModel.Interfaces.IProject;

/**
 * Created by Joshua on 27/03/2017.
 * A "Project" object represents a construction project, which contains a title, image and description. Projects are stored in one of three directories on Fire Base, depending on the type of project.
 */
public class Project implements IProject {
    private String name;
    private String description;
    private String image;

    /**
     * This is the default constructor, used to create empty projects which can be later added to.
     */
    public Project() {
        name = "N/A";
        description = "N/A";
        image = "N/A";
    }

    /**
     * This is the primary constructor, considering all the necessary variables necessary to produce a complete "Project" object.
     *
     * @param tag  String variable used as the projects title (Shown as an overlay on the projects image).
     * @param info String value of the projects description. This can be as detailed or brief as the user wants (hence the large length range allowed).
     * @param img  String value which doubles as the projects Fire Base key. This is due to the image being stored at an identical address inside of Fire Base storage.
     * @throws Exception An exception is thrown when one of the string values doesn't conform to the pre-existing checks (ensures consistent, valid data).
     */
    public Project(String tag, String info, String img) throws Exception {

        // Variable checks
        if (tag == null || info == null) {
            throw new Exception("Please complete all fields.");
        }
        if (tag.length() < 3 || tag.length() > 30) {
            throw new Exception("The project title must be between 3 and 30 characters in length.");
        }
        if (info.length() < 20 || info.length() > 1000) {
            throw new Exception("The project description must be between 20 and 1000 characters in length.");
        }
        if (img == null) {
            throw new Exception("Please add an image to the project.");
        }
        // Variable formatting
        tag = tag.substring(0, 1).toUpperCase() + tag.substring(1);
        info = info.substring(0, 1).toUpperCase() + info.substring(1);

        // Variable allocation
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
        if (title.length() < 3 || title.length() > 30) {
            throw new Exception("The project title must be between 3 and 30 characters in length.");
        }
        title = title.substring(0, 1).toUpperCase() + title.substring(1);
        this.name = title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String info) throws Exception {
        if (info.length() < 20 || info.length() > 1000) {
            throw new Exception("The project description must be between 20 and 1000 characters in length.");
        }
        info = info.substring(0, 1).toUpperCase() + info.substring(1);
        this.description = info;
    }

    @Override
    public String getImage() {
        return image;
    }

}
