package com.cst2335.finalproject;

public class CarData {
    String carName;
    int id;
    int modelID;
    String modelName;
    public CarData(int ID,String carName,  int modelID, String modelName) {
        this.carName = carName;
        this.id = ID;
        this.modelID = modelID;
        this.modelName = modelName;

    }

    public String getCarName(){
        return carName;
    }
    public int getId(){
        return id;
    }
    public int getModelID(){
        return modelID;
    }
    public String getModelName(){
        return modelName;
    }



}
