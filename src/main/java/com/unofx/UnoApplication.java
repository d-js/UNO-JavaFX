package com.unofx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class UnoApplication extends Application {

    private MediaPlayer mediaPlayer;


    @Override
    public void start(Stage primaryStage) throws IOException
    {
        String audioFilePath = Objects.requireNonNull(UnoApplication.class.getResource("/com/music/lofichillmusic50.wav")).toExternalForm(); // Assicurati di usare il percorso corretto del file

        Media media = new Media(audioFilePath);

        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setVolume(0.15);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();

        FXMLLoader fxmlLoader = new FXMLLoader(UnoApplication.class.getResource("Select_game_mode.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        primaryStage.setTitle("UNO - The Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
