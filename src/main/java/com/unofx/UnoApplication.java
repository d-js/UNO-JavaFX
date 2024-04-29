package com.unofx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class UnoApplication extends Application {

    private Stage stage;

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.stage = primaryStage; // Inizializzazione dello stage
        FXMLLoader fxmlLoader = new FXMLLoader(UnoApplication.class.getResource("initial_screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("UNO");
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}