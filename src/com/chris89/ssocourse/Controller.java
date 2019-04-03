package com.chris89.ssocourse;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import static java.util.concurrent.TimeUnit.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Controller implements Initializable{
    public TableView<Process> appTableView;
    public TableView<Process> queueTableView;
    public TableView<Process> cpu1TableView;
    public TableView<Process> cpu2TableView;
    public TableView<Process> cpu3TableView;
    public TableView<Process> cpu4TableView;
    public TableColumn<Process, String> appNameField;
    public TableColumn<Process, Integer> appTimeField;
    public TableColumn<Process, String> queueNameField;
    public TableColumn<Process, Integer> queueTimeField;
    public TableColumn<Process, Integer> activeField;
    public TableColumn<Process, String> cpu1NameField;
    public TableColumn<Process, String> cpu2NameField;
    public TableColumn<Process, String> cpu3NameField;
    public TableColumn<Process, String> cpu4NameField;
    public TableColumn<Process, Integer> cpu1TimeField;
    public TableColumn<Process, Integer> cpu2TimeField;
    public TableColumn<Process, Integer> cpu3TimeField;
    public TableColumn<Process, Integer> cpu4TimeField;
    ObservableList<Process> selectedApp;
    public TextField quantumField;
    int queueTableSize = 0;
    int quantum = 5;
    Process idle = new Process("idle", -1, 0);
    Process test = new Process("test", 25, 0);
    Processor cpu [] = new Processor[4];


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        appNameField.setCellValueFactory(new PropertyValueFactory<>("Name"));
        appTimeField.setCellValueFactory(new PropertyValueFactory<>("ExecutionTime"));
        appTableView.setItems(appObservableList);
        queueNameField.setCellValueFactory(new PropertyValueFactory<>("Name"));
        queueTimeField.setCellValueFactory(new PropertyValueFactory<>("RemainingTime"));
        activeField.setCellValueFactory(new PropertyValueFactory<>("Active"));
        queueTableView.setItems(queueObservableList);
        cpu1NameField.setCellValueFactory(new PropertyValueFactory<>("Name"));
        cpu1TimeField.setCellValueFactory(new PropertyValueFactory<>("RemainingTime"));
        cpu1TableView.setItems(cpu1ObservableList);
        cpu2NameField.setCellValueFactory(new PropertyValueFactory<>("Name"));
        cpu2TimeField.setCellValueFactory(new PropertyValueFactory<>("RemainingTime"));
        cpu2TableView.setItems(cpu1ObservableList);
        cpu3NameField.setCellValueFactory(new PropertyValueFactory<>("Name"));
        cpu3TimeField.setCellValueFactory(new PropertyValueFactory<>("RemainingTime"));
        cpu3TableView.setItems(cpu1ObservableList);
        cpu4NameField.setCellValueFactory(new PropertyValueFactory<>("Name"));
        cpu4TimeField.setCellValueFactory(new PropertyValueFactory<>("RemainingTime"));
        cpu4TableView.setItems(cpu1ObservableList);
        quantumField.setText(Integer.toString(quantum));
        for(int i = 0; i<4; i++)
            cpu[i] = new Processor(quantum);
        cpu[0].awake();
        new cpu1Controller().runEverySecond();

    }
    ObservableList<Process> appObservableList = FXCollections.observableArrayList(
            new Process("Internet", 30, 1),
            new Process("Firefox", 20, 2),
            new Process("Paint", 5, 3),
            new Process("Winamp", 16, 4),
            new Process("Spotify", 45, 5 )

    );
    ObservableList<Process> queueObservableList = FXCollections.observableArrayList();
    ObservableList<Process> cpu1ObservableList = FXCollections.observableArrayList(
            new Process("idle", -1, 0)
    );
    // GUI methods
    public void addToQueue(){
        selectedApp = appTableView.getSelectionModel().getSelectedItems();
        Process process = new Process(selectedApp.get(0).getName(), selectedApp.get(0).getExecutionTime());
        queueObservableList.add(process);
        ++queueTableSize;
    }
    public void incQuantum(){
        ++quantum;
        quantumField.setText(Integer.toString(quantum));
    }
    public void decQuantum(){
        if(quantum>1){
            --quantum;
            quantumField.setText(Integer.toString(quantum));
        }else{
            quantum = 1;
        }
    }
    public void awakeCpu(int number){
        cpu[number].awake();
        System.out.println(number);
    }


    class cpu1Controller {
        private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        public void runEverySecond() {
            final Runnable watcher = new Runnable() {
                public void run() {
                    runCpu1();
                }
            };
            final ScheduledFuture<?> watcherHandle =
                    scheduler.scheduleAtFixedRate(watcher, 1, 1, SECONDS);
            scheduler.schedule(new Runnable() {
                public void run() { watcherHandle.cancel(true); }
            }, 60 * 60, SECONDS);
        }
    }
    void takeProcess(){
        cpu[0].quantum(quantum);
        if(queueObservableList.size()>0){
            if(cpu[0].getProcessIndex()>=queueObservableList.size()){
                cpu[0].resetProcessIndex();
            }
            cpu[0].addProcess(queueObservableList.get(cpu[0].getProcessIndex()));
            queueObservableList.get(cpu[0].getProcessIndex()).setActive(0);
        }else{
            cpu[0].addProcess(idle);
        }
    }
    void refreshViews(){
        queueTableView.refresh();
        cpu1ObservableList.clear();
        cpu1ObservableList.add(cpu[0].getProcess());
    }
    public void runCpu1(){
        if(cpu[0].getProcess().getName()=="idle"){
            takeProcess();
        }else{
            if(cpu[0].getProcess().getRemainingTime()>0){
                if(cpu[0].getRemainingQuantum()>0){
                    cpu[0].decRemainingQuantum();
                    cpu[0].getProcess().decRemeaningTime();
                    refreshViews();
                }else{
                    queueObservableList.get(cpu[0].getProcessIndex()).setInactive();
                    cpu[0].incProcessIndex();
                    takeProcess();
                    runCpu1();
                }
            }else{
                queueObservableList.remove(cpu[0].getProcess());
                cpu[0].removeProcess();
                takeProcess();
                runCpu1();
            }
        }
        refreshViews();
    }
    void runcpu(int id){

    };
    public void handlerCPU(){
        if(queueObservableList.size()>0){
            for (Process process: queueObservableList) {
                if(process.getActive()!=""){
                    for(int i=0;i<4;i++){
                        if(cpu[i].isActive() && cpu[i].getProcess()!=null){
                            cpu[i].addProcess(process);
                            cpu[i].getProcess().setActive(i);
                            runcpu(i);
                        }
                    }
                }
            }
            refreshViews();
        }
    }
   
}

