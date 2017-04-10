package com.example.joshua.ljc.DataModel.Interfaces;

import java.io.Serializable;

/**
 * Created by Joshua on 27/03/2017.
 */

public interface IProject extends Serializable{

    String getUUID();

    String getName();

    void setName(String title) throws Exception;

    String getDescription();

    void setDescription(String description) throws Exception;

    String getImage();


}
