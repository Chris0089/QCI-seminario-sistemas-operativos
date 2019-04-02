package com.chris89.ssocourse;

public class Process {
    private String name;
    private int executionTime;
    private int id;
    private String castedName;
    private int remainingTime;
    private boolean isBeingExecuted;
    private String active;


    public Process(String newName, int newTime){
        name = newName;
        executionTime = newTime;
        remainingTime = newTime;
        generateCastedName();
    }
    public Process(String newName, int newTime, int newId){
       this(newName, newTime);
       id = newId;
    }
    private void generateCastedName(){
        castedName = name + " (" + Integer.toString(remainingTime) + ")";
    }
    String getCastedName(){
        if(castedName==""){
            generateCastedName();
            return castedName;
        }else{
            return castedName;
        }
    }
    void updateRemainingTime(int newTime){
        remainingTime = newTime;
    }
    void decRemeaningTime(){
        --remainingTime;
    }
    void resetRemaingTime(){
        remainingTime = executionTime;
    }
    void updateId(int newID){
        id=newID;
    }

    public String getName(){
        return name;
    }
    public int getExecutionTime(){
        return  executionTime;
    }
    public int getRemainingTime(){
        return remainingTime;
    }
    public String getActive(){
        return active;
    }

    void setActive(int cpu){
        active = "CPU"+Integer.toString(cpu+1);
    }
    void setInactive(){
        active="";
    }

}
