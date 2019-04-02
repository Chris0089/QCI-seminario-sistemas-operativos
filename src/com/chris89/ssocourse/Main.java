package com.chris89.ssocourse;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("ssocourse.fxml"));
        primaryStage.setTitle("Awsome App - Threads");
        primaryStage.setScene(new Scene(root, 850, 750));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
