package com.example.joshua.ljc.DataModel.Interfaces;

import java.io.Serializable;

/**
 * Created by Joshua on 27/03/2017.
 * This interface ensures that objects which implement it, contain the necessary methods to perform the actions of a "Construction Project".
 */
public interface IProject extends Serializable {

    /**
     * Gets the projects Fire Base key value. This is needed to perform updates and removals.
     *
     * @return String value which represents the documents key (Document store).
     */
    String getUUID();

    /**
     * Gets a projects title, this is the caption held in the list view UI.
     *
     * @return String value of the projects title.
     */
    String getName();

    /**
     * Sets the caption of a project, which is used as an overlay for both the website and mobile application.
     *
     * @param title This is the new caption's String value.
     * @throws Exception This exception is used to ensure that the String meets the minimum requirements e.g. length.
     */
    void setName(String title) throws Exception;

    /**
     * Gets a project's description. The description is subject to the users interpretation.
     *
     * @return String value of the project's description.
     */
    String getDescription();

    /**
     * Sets a projects description. This description must conform to the included constraints.
     *
     * @param description String value of the new description.
     * @throws Exception This exception is thrown when the new description doesn't meet the pre-defined requirements.
     */
    void setDescription(String description) throws Exception;

    /**
     * Returns the Fire base key used to store the projects data. The reason for this is that the projects image is held under the same value inside of Fire Base storage.
     *
     * @return String value which represents the projects Fire Base key / ImageID.
     */
    String getImage();


}
