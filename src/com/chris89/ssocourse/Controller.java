package com.chris89.ssocourse;

import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable{
    public TableView<Process> appTableView;
    public TableView<Process> queueTableView;
    public TableView<Process> cpu1TableView;
    public TableColumn<Process, String> appNameField;
    public TableColumn<Process, Integer> appTimeField;
    public TableColumn<Process, String> queueNameField;
    public TableColumn<Process, Integer> queueTimeField;
    public TableColumn<Process, String> cpu1NameField;
    public TableColumn<Process, Integer> cpu1TimeField;
    ObservableList<Process> selectedApp;
    public TextField quantumField;
    int queueTableSize = 0;
    int quantum = 5;
    Process idle = new Process("idle", -1, 0);
    Process test = new Process("test", 25, 0);
    Processor cpu1 = new Processor();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        appNameField.setCellValueFactory(new PropertyValueFactory<>("Name"));
        appTimeField.setCellValueFactory(new PropertyValueFactory<>("ExecutionTime"));
        appTableView.setItems(appObservableList);
        queueNameField.setCellValueFactory(new PropertyValueFactory<>("Name"));
        queueTimeField.setCellValueFactory(new PropertyValueFactory<>("ExecutionTime"));
        queueTableView.setItems(queueObservableList);
        cpu1NameField.setCellValueFactory(new PropertyValueFactory<>("Name"));
        cpu1TimeField.setCellValueFactory(new PropertyValueFactory<>("ExecutionTime"));
        cpu1TableView.setItems(cpu1ObservableList);
        quantumField.setText(Integer.toString(quantum));
    }
    ObservableList<Process> appObservableList = FXCollections.observableArrayList(
            new Process("Internet", 30, 1),
            new Process("Firefox", 20, 2),
            new Process("Paint", 5, 3),
            new Process("Winamp", 15, 4),
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
    public void addProcessToCpu(Processor cpu, Process process, ObservableList oList){
        cpu.addProcess(process);
        oList.add(process);
    }
    public void addToQueue(){
        selectedApp = appTableView.getSelectionModel().getSelectedItems();
        queueObservableList.add(selectedApp.get(0));
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
        if(quantum>0){
            --quantum;
            quantumField.setText(Integer.toString(quantum));
        }else{
            quantum = 0;
        }
    }

    public void addToCpu1(){
        //addProcessToCpu(cpu1, test, cpu1ObservableList);
    }
}

