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
    public TableColumn<Process, String> appNameField;
    public TableColumn<Process, Integer> appTimeField;
    public TableColumn<Process, String> queueNameField;
    public TableColumn<Process, Integer> queueTimeField;
    public TableColumn<Process, Integer> activeField;
    public TableColumn<Process, String> cpu1NameField;
    public TableColumn<Process, Integer> cpu1TimeField;
    ObservableList<Process> selectedApp;
    public TextField quantumField;
    int queueTableSize = 0;
    int quantum = 5;
    Process idle = new Process("idle", -1, 0);
    Process test = new Process("test", 25, 0);
    Processor cpu1 = new Processor(quantum);

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
        quantumField.setText(Integer.toString(quantum));
        cpu1.awake();
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

    public void addIdle(ObservableList oList, TableView tView){
        if (oList.isEmpty()){
            oList.add(idle);
        }
    }

    public void addToQueue(){
        selectedApp = appTableView.getSelectionModel().getSelectedItems();
        Process process = new Process(selectedApp.get(0).getName(), selectedApp.get(0).getExecutionTime());
        queueObservableList.add(process);
        ++queueTableSize;
    }
    public void removeProcess(ObservableList oList, Process process){
        oList.remove(process);
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
        cpu1.quantum(quantum);
        if(queueObservableList.size()>0){
            if(cpu1.getProcessIndex()>=queueObservableList.size()){
                cpu1.resetProcessIndex();
            }
            cpu1.addProcess(queueObservableList.get(cpu1.getProcessIndex()));
            queueObservableList.get(cpu1.getProcessIndex()).setActive();
        }else{
            cpu1.addProcess(idle);
        }
    }
    void refreshViews(){
        queueTableView.refresh();
        cpu1ObservableList.clear();
        cpu1ObservableList.add(cpu1.getProcess());
    }
    public void runCpu1(){
        if(cpu1.getProcess().getName()=="idle"){
            takeProcess();
        }else{
            if(cpu1.getProcess().getRemainingTime()>0){
                if(cpu1.getRemainingQuantum()>0){
                    cpu1.decRemainingQuantum();
                    cpu1.getProcess().decRemeaningTime();
                    refreshViews();
                }else{
                    queueObservableList.get(cpu1.getProcessIndex()).setInactive();
                    cpu1.incProcessIndex();
                    takeProcess();
                    runCpu1();
                }
            }else{
                queueObservableList.remove(cpu1.getProcess());
                cpu1.removeProcess();
                takeProcess();
                runCpu1();
            }
        }
        refreshViews();
    }
}

