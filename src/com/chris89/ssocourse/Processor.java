package com.chris89.ssocourse;

class Processor {
    Process activeProcess;
    boolean active;

    Processor(){
        activateProcessor();
    }
    void addProcess(Process process){
        activeProcess = process;
    }
    void removeProcess(){
        activeProcess = null;
    }
    void activateProcessor(){
        active = true;
    }
    void deactivateProcessor(){
        active = false;
    }

}