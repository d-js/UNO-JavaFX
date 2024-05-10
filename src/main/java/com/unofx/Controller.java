package com.unofx;

import com.unofx.model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class Controller implements Initializable
{
    // View scene var
    protected static Scene scene;
    protected static Stage stage;
    protected static Parent root;



    public void initializeScene(String _fxmlScene, MouseEvent mouseEvent) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(_fxmlScene));
        this.root = fxmlLoader.load();
        stage = (Stage) ((Button) mouseEvent.getSource()).getScene().getWindow();
        this.scene = new Scene(root);

    }



    public void update()
    {
        this.stage.setScene(this.scene);
        this.stage.setFullScreen(true);
        try
        {
            stage.show();
        }catch(Exception e) {
            System.out.println("error on update");
        }
    }

    // Rendi inusabili le carte dell'utente



    @FXML
    public void on_start(MouseEvent mouseEvent) throws IOException
    {
        this.inizializeTable();
        initializeScene("game_pane.fxml", mouseEvent);
        update();
    }

    public void inizializeTable() throws IOException
    {
        Table.getInstance().set_deck();
        Table.getInstance().setCurrentCard(Table.getInstance().deck.set_initial_card());
        Table.getInstance().setCurrentCard(Table.getInstance().deck.getLastUncover());
        Table.getInstance().set_player_list(4);
        Table.getInstance().setCurrentPlayer();
        Table.getInstance().give_start_card();
    }

    @FXML
    public void on_nickname(InputMethodEvent _inputMethodEvent)
    {
        String nickname = _inputMethodEvent.getCommitted();
        Table.getInstance().set_username(nickname);
        System.out.println(nickname);

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {

    }
}