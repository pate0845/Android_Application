package com.cst2335.finalproject;
/**
 * public class CarData
 */
public class CarData {
    /**
     * data variables carName of type String,
     * id of type int
     * modelID of type int
     * modelName of type String
     */
    String carName;
    int id;
    int modelID;
    String modelName;

    /**
     * four argument constructor
     * @param ID
     * @param carName
     * @param modelID
     * @param modelName
     */
    public CarData(int ID,String carName,  int modelID, String modelName) {
        this.carName = carName;
        this.id = ID;
        this.modelID = modelID;
        this.modelName = modelName;

    }

    /**
     * getter method
     * @return carName
     */

    public String getCarName(){
        return carName;
    }

    /**
     * getter method
     * @return id
     */
    public int getId(){
        return id;
    }

    /**
     * getter method
     * @return modelID
     */
    public int getModelID(){
        return modelID;
    }

    /**
     * getter method
     * @return nodelName
     */
    public String getModelName(){
        return modelName;
    }



}
