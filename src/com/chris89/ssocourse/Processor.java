package com.chris89.ssocourse;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.SECONDS;

class Processor {
    Process activeProcess;
    boolean active;
    boolean sleep;
    private int remainingQuantum;
    int processIndex;

    Processor(int quantum){
        activeProcess = new Process("idle", -1, 0);
        activateProcessor();
        remainingQuantum = quantum;
        sleep = true;
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
    void awake(){
        sleep = false;
    }
    void sleep(){
        sleep = true;
    }
    void deactivateProcessor(){
        active = false;
    }
    public Process getProcess(){
        return activeProcess;
    }
    public boolean isActive(){
        return active;
    }
    public int getRemainingQuantum(){
        return remainingQuantum;
    }
    void decRemainingQuantum(){
        --remainingQuantum;
    }
    int getProcessIndex(){
        return processIndex;
    }
    void incProcessIndex(){
        ++processIndex;
    }
    void resetProcessIndex(){
        processIndex=0;
    }
    void quantum(int quantum){
        remainingQuantum=quantum;
    }
}