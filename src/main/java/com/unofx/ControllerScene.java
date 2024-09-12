package com.unofx;

import com.unofx.model.classes.TableImpl;
import com.unofx.model.classes.UserPlayer;
import com.unofx.model.interfaces.Card;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ControllerScene implements Initializable{

    @FXML
    private Button start_button_single_mode;
    @FXML
    private Button start_button_duel_mode;
    @FXML
    private Button backButton;
    @FXML
    private TextField playerNameField;
    @FXML
    private Button startButton;
    @FXML
    private Button ruleButton;
    @FXML
    private Button ruleButtonTwo;
    protected static Scene scene;
    protected static Stage stage;
    protected static Parent root;
    private AudioClip buttonClickSound;
    static Dotenv dot = Dotenv.load();
    private static final String SOUND_FILE_BUTTON = Objects.requireNonNull(UnoApplication.class.getResource(dot.get("BUTTON_SOUND"))).toExternalForm();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inizializza l'AudioClip con il suono
        buttonClickSound = new AudioClip(SOUND_FILE_BUTTON);
        buttonClickSound.setVolume(0.75);

        // Aggiungi il suono del click a ogni bottone, solo se non sono nulli
        if (startButton != null) {
            addClickSound(startButton);
        }
        if (ruleButton != null) {
            addClickSound(ruleButton);
        }
        if (backButton != null) {
            addClickSound(backButton);
        }
        if (start_button_single_mode != null) {
            addClickSound(start_button_single_mode);
        }
        if (ruleButtonTwo != null) {
            addClickSound(ruleButtonTwo);
        }
        if (start_button_duel_mode != null) {
            addClickSound(start_button_duel_mode);
        }
    }

    // Metodo per aggiungere il suono del click a un bottone
    private void addClickSound(Button button) {
        if (button != null) {
            // Salva l'azione esistente
            EventHandler<ActionEvent> existingAction = button.getOnAction();

            // Imposta un nuovo gestore dell'evento che esegue il suono e poi l'azione esistente
            button.setOnAction(event -> {
                buttonClickSound.play();
                if (existingAction != null) {
                    existingAction.handle(event);
                }
            });
        }
    }

    @FXML
    public void onStartSingleMode() {
        GameState.getInstance().setChoosenMode("Single");
        loadScene("Insert_name_single_players.fxml");
    }

    @FXML
    public void onStartDuelMode() {
        GameState.getInstance().setChoosenMode("Duel");
        loadScene("Insert_name_single_players.fxml");
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
    public void onStartGame() {
        String playerName = playerNameField.getText();
        UserPlayer user = new UserPlayer(playerName);
        TableImpl.getInstance().addPlayerInTable(user);

        if (!playerName.isEmpty()) {
            System.out.println("Starting game with player: " + playerName);
            String choosenMode = GameState.getInstance().getChoosenMode();  // Recupero la modalità di gioco

            if (choosenMode.equals("Single")) {
                inizializeTable("Single");
                loadScene("Game_single_mode_pane.fxml");
            } else if (choosenMode.equals("Duel")) {
                inizializeTable("Duel");
                loadScene("Game_duel_mode_pane.fxml");
            }
        } else {
            System.out.println("Please enter a player name");
        }
    }


    private void loadScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            root = loader.load();
            //Parent root = loader.load();
            stage = (Stage) (start_button_single_mode != null ? start_button_single_mode.getScene().getWindow() :
                    (startButton != null ? startButton.getScene().getWindow() :
                            (ruleButton != null ? ruleButton.getScene().getWindow() :
                                    (ruleButtonTwo!= null ? ruleButtonTwo.getScene().getWindow() : backButton.getScene().getWindow()))));


            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Errore durante il caricamento della scena: " + fxmlFile);
            e.printStackTrace();
        }
    }


    public void inizializeTable(String gamemode)
    {
        Platform.runLater(() -> {
            TableImpl.getInstance().setDeck();
            Card e = TableImpl.getInstance().deck.setInitialCard();
            TableImpl.getInstance().setCurrentCardInformation(e);
            if(gamemode.equals("Single"))
                TableImpl.getInstance().setPlayerList(3);
            else if (gamemode.equals("Duel"))
                TableImpl.getInstance().setPlayerList(1);
            TableImpl.getInstance().setCurrentPlayer();
            TableImpl.getInstance().giveStartCard();

        });
    }

}