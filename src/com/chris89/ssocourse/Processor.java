package com.chris89.ssocourse;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.SECONDS;

class Processor {
    Process activeProcess;
    boolean active;
    boolean sleep;

    Processor(){
        activeProcess = new Process("idle", -1, 0);
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
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void runEverySecond() {
        final Runnable watcher = new Runnable() {
            public void run() {
                //every second
                System.out.println("beep");

            }
        };
        final ScheduledFuture<?> watcherHandle =
                scheduler.scheduleAtFixedRate(watcher, 1, 1, SECONDS);
        scheduler.schedule(new Runnable() {
            public void run() { watcherHandle.cancel(true); }
        }, 60 * 60, SECONDS);
    }
}