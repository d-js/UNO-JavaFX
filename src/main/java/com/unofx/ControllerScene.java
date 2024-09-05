package com.unofx;

import com.unofx.model.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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
    private TextField firstPlayerNameField;
    @FXML
    private TextField secondPlayerNameField;
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
    public void on_start_duel_mode() {
        loadScene("Insert_name_duel_players.fxml");
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
        String playerName = playerNameField.getText();
        UserPlayer user = new UserPlayer(playerName);
        Table.getInstance().addPlayerInTable(user);
        if (!playerName.isEmpty()) {
            System.out.println("Starting game with player: " + playerName);
            // Implementa la logica per iniziare il gioco
            this.inizializeTable("Single");
            loadScene("Game_single_mode_pane.fxml");
        } else {
            System.out.println("Please enter a player name");
        }

    }
    @FXML
    public void onStartGameCouple() throws IOException {

        String playerName1 = firstPlayerNameField.getText();
        String playerName2 = secondPlayerNameField.getText();

        UserPlayer user = new UserPlayer(playerName1);
        Table.getInstance().addPlayerInTable(user);

        UserPlayer user1 = new UserPlayer(playerName2);
        Table.getInstance().addPlayerInTable(user1);

        if (!playerName1.isEmpty() && !playerName2.isEmpty()) {
            System.out.println("Starting game with player: " + playerName1 + playerName2);
            // Implementa la logica per iniziare il gioco
            this.inizializeTable("Couple");
            System.out.println(Table.getInstance().getAllUser().get(0).getUsername() + " e " + Table.getInstance().getAllUser().get(1).getUsername() );
            Platform.runLater(() -> loadScene("Game_couple_mode_pane.fxml"));
        } else {
            System.out.println("Please enter all player name");
        }
        loadScene("Game_couple_mode_pane.fxml");
    }

    @FXML
    public void onDuelStart() throws IOException {

        String playerName = playerNameField.getText();
        UserPlayer user = new UserPlayer(playerName);
        Table.getInstance().addPlayerInTable(user);
        if (!playerName.isEmpty()) {
            System.out.println("Starting game with player: " + playerName);
            // Implementa la logica per iniziare il gioco
            this.inizializeTable("Duel");
            loadScene("Game_duel_mode_pane.fxml");
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

    public void inizializeTable(String gamemode) throws IOException
    {
        Platform.runLater(() -> {
            // TODO valutare se spostare il possibile nel table
            Table.getInstance().setDeck();
            Card e = Table.getInstance().deck.setInitialCard();
            if(e.getColor() == Colour.BLACK)
            {
                ((ActionCard)e).setColour(Colour.fromValue(new Random().nextInt(3)));
            }
            Table.getInstance().setCurrentCardInformation(e);
            if(gamemode.equals("Single"))
                Table.getInstance().setPlayerList(3);
            else if (gamemode.equals("Duel"))
                Table.getInstance().setPlayerList(1);
            Table.getInstance().setCurrentPlayer();
            Table.getInstance().giveStartCard();

        });
    }




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){}
}