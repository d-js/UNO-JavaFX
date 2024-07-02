package com.unofx;

import com.unofx.model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class ControllerScene implements Initializable{

    @FXML
    private Button start_button_single_mode;
    @FXML
    private Button start_button_couple_mode;
    @FXML
    private Button backButton;
    @FXML
    private TextField playerNameField;
    @FXML
    private TextField playerNameField1;
    @FXML
    private TextField playerNameField2;
    @FXML
    private Button startButton;
    @FXML
    private Button ruleButton;
    @FXML
    private Button ruleButtonTwo;
    protected static Scene scene;
    protected static Stage stage;
    protected static Parent root;


    private String previousFXML;

    @FXML
    public void on_start_single_mode() {
        loadScene("Insert_name_single_players.fxml");
    }

    @FXML
    public void on_start_couple_mode() {
        loadScene("Insert_name_couple_players.fxml");
    }

    @FXML
    public void onBackButtonClicked() {
        loadScene("Select_game_mode.fxml");
    }

    @FXML
    public void onRuleGame() {
        loadScene("Rule_pane_one.fxml");
    }

    @FXML
    public void onButtonClickedRulePane2() {
        loadScene("Rule_pane_two.fxml");
    }

    @FXML
    public void onStartGame() throws IOException {
        this.inizializeTable();
        loadScene("Game_single_mode_pane.fxml");
        String playerName = playerNameField.getText();
        Table.getInstance().getUserPlayer().setUsername(playerName);
        if (!playerName.isEmpty()) {
            System.out.println("Starting game with player: " + playerName);
            // Implementa la logica per iniziare il gioco
        } else {
            System.out.println("Please enter a player name");
        }

    }
    @FXML
    public void onStartGameCouple() throws IOException {
        this.inizializeTable();
        loadScene("Game_couple_mode_pane.fxml");
        String playerName1 = playerNameField1.getText();
        Table.getInstance().getUserPlayer().setUsername(playerName1);
        if (!playerName1.isEmpty()) {
            System.out.println("Starting game with player: " + playerName1);
            // Implementa la logica per iniziare il gioco
        } else {
            System.out.println("Please enter a player name");
        }
        String playerName2 = playerNameField2.getText();
        Table.getInstance().getUserPlayer().setUsername(playerName2);
        if (!playerName2.isEmpty()) {
            System.out.println("Starting game with player: " + playerName2);
            // Implementa la logica per iniziare il gioco
        } else {
            System.out.println("Please enter a player name");
        }
    }

    private void loadScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            this.root = loader.load();
            //Parent root = loader.load();
            this.stage = (Stage) (start_button_single_mode != null ? start_button_single_mode.getScene().getWindow() :
                                    (start_button_couple_mode != null ? start_button_couple_mode.getScene().getWindow() :
                                        (startButton != null ? startButton.getScene().getWindow() :
                                            (ruleButton != null ? ruleButton.getScene().getWindow() :
                                                (ruleButtonTwo!= null ? ruleButtonTwo.getScene().getWindow() : backButton.getScene().getWindow())))));
            this.scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Errore durante il caricamento della scena: " + fxmlFile);
            e.printStackTrace();
        }
    }

    public void inizializeTable() throws IOException
    {
        // TODO valutare se spostare il possibile nel table
        Table.getInstance().set_deck();
        Card e = Table.getInstance().deck.set_initial_card();
        if(e.getColor() == Colour.BLACK)
        {
            ((ActionCard)e).setColour(Colour.fromValue(new Random().nextInt(3)));
        }
        Table.getInstance().setCurrentCardInformation(e);
        Table.getInstance().set_player_list(4);
        Table.getInstance().setCurrentPlayer();
        Table.getInstance().give_start_card();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){}
}