package com.unofx;

import com.unofx.model.classes.BotPlayer;
import com.unofx.model.classes.TableImpl;
import com.unofx.model.enums.Caction;
import com.unofx.model.enums.Colour;
import com.unofx.model.interfaces.Card;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class DuelGameController implements Initializable {

    public Pane top_bot;
    public Pane right_bot;
    public Pane left_bot;
    // View scene var
    private Scene scene;
    private Stage stage;
    private Parent root;

    // Game var
    @FXML
    private Pane currentCardView = new Pane();

    @FXML
    private Pane userHandView = new HBox();

    @FXML
    private Button deck = new Button();

    @FXML
    private Button pass = new Button();

    @FXML
    private Label Name_user_duel = new Label();

    //private String imagesPath = dotenv.get("IMAGES_PATH");    //  scommentare prima del push
    private final String imagesPath   =   "src/main/resources/com/cardImages/";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        this.scene = ControllerScene.scene;
        this.stage = ControllerScene.stage;
        this.root = ControllerScene.root;
        this.userHandView.setPadding(new Insets(10));
        Platform.runLater(() -> {
            this.update_view();
            this.setCurrentCardImage(this.generate_imagePath(TableImpl.getInstance().getCurrentCard().getName()));
            this.Name_user_duel.setText("Players name: "+ TableImpl.getInstance().getUserPlayer().getUsername());
            this.cicleBotTurns();
        });

        // Inizia la partita
    }

    public void cicleBotTurns()
    {


        this.deck.setDisable(true);
        // doppio runlater perche?

        Platform.runLater(this::hide_user_hand);
        // Il controllo per l'utente e' fatto dalla vista, quello per i bot e' fatto nel loro metodo
        cicleBotUntilUser();

        if (TableImpl.getInstance().controlWinner())
            this.showWinnerAlert();

    }

    private void cicleBotUntilUser()
    {
        if (!TableImpl.getInstance().controlWinner())
        {
            this.pass.setDisable(true);
            // Controllo se il giocatore corrente è un bot e se la partita non è ancora conclusa
            if (!(TableImpl.getInstance().getCurrentPlayer() instanceof BotPlayer))
            {

                Platform.runLater(() -> {
                    this.deck.setDisable(false);
                    this.update_view();
                    this.show_user_hand();
                });
            }
            else
            {
                this.playBotMovesWithDelay(((BotPlayer) TableImpl.getInstance().getCurrentPlayer()));

            }
        }
        else
        {
            this.showWinnerAlert();
        }
    }

    private void playBotMovesWithDelay(BotPlayer bot)
    {
        PauseTransition pause = new PauseTransition(Duration.seconds(1)); // Ritardo di 5 secondi tra le mosse dei bot
        pause.setOnFinished(event -> {
            bot.playCard(0); // Esegui la mossa del bot (esempio: playCard(0) supponendo la prima carta)
            updateViewAfterMove(); // Aggiorna la vista dopo ogni mossa del bot
            cicleBotUntilUser();
        });
        pause.play();
    }

    private void updateViewAfterMove()
    {
        Platform.runLater(() -> {
            setCurrentCardImage(generate_imagePath(TableImpl.getInstance().getCurrentCard().getName())); // Aggiorna l'immagine della carta corrente
        });
    }

    private void showWinnerAlert()
    {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Partita Conclusa.");
            alert.setHeaderText(TableImpl.getInstance().getCurrentPlayer().getUsername().toUpperCase() + "VINCE!!");
            alert.setContentText("Press 'Close' to exit the game or 'Menu' to go to initial game screen");

            ButtonType closeButton = new ButtonType("Close", ButtonBar.ButtonData.OK_DONE);
            ButtonType loadAnotherScreenButton = new ButtonType("Menu", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(closeButton, loadAnotherScreenButton);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == closeButton) {
                System.exit(0); // Chiudi l'applicazione
            }
            else if(result.isPresent() && result.get() == loadAnotherScreenButton)
            {
                TableImpl.getInstance().reset();
                try {
                    // Carica il nuovo layout dal file FXML
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Select_game_mode.fxml"));
                    Parent menuRoot = loader.load();

                    // Imposta la nuova root nella scena esistente
                    this.stage.getScene().setRoot(menuRoot);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                alert.close();
            }
        });
    }

    @FXML
    public void on_one_card()
    {
        TableImpl.getInstance().getUserPlayer().setOneTrue();
    }

    @FXML
    public void on_deck_click() throws IOException
    {
        Card e = TableImpl.getInstance().deck.drawOut();
        TableImpl.getInstance().getUserPlayer().drawCard(e);
        this.deck.setDisable(true);
        this.pass.setDisable(false);
        draw(generate_imagePath(e.getName()));
    }

    public void update_view()
    {
        Platform.runLater(() -> {

            List<String> nameCardList = TableImpl.getInstance().getUserPlayer().getHand().stream().map(Card::getName).collect(Collectors.toList());
            this.updateHand(nameCardList);
        });
    }

    private void updateHand(List<String> nameCardList)
    {
        Platform.runLater(() -> {
            this.userHandView.getChildren().clear();

            for (String cardName : nameCardList) {
                try {
                    this.draw(this.generate_imagePath(cardName));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }


    public void hide_user_hand()
    {
        Platform.runLater(() -> {
            for (Node n : this.userHandView.getChildren()) {
                if (n instanceof Button) {
                    n.setDisable(true);
                }
            }
        });
    }


    // Rendi usabili le carte che puo giocare, all'utente
    public void show_user_hand()
    {
        Platform.runLater(() -> {
            for (Node n : this.userHandView.getChildren()) {
                if (n instanceof Button) {
                    n.setDisable(!this.playable((Button)n));
                }
                else
                    n.setDisable(true);
            }
        });
    }

    // metodo che controlla se l'user puo giocare la carta
    private boolean playable(Button n)
    {
        return TableImpl.getInstance().getUserPlayer().isPlayable(n.getAccessibleText());

    }

    public void setCurrentCardImage(String _file_path)
    {

        Image i = new Image(_file_path);
        ImageView iw = new ImageView();
        iw.setImage(i);
        iw.setFitWidth(currentCardView.getPrefWidth());
        iw.setFitHeight(currentCardView.getPrefHeight());

        this.currentCardView.getChildren().add(iw);
    }

    public String generate_imagePath(String cardName)
    {
        cardName = cardName.replaceFirst(cardName.substring(0, 1), cardName.substring(0, 1).toUpperCase());
        //System.out.println(this.imagesPath);
        return "file:" + this.imagesPath + cardName + ".png";

    }

    public void draw(String cardPath) throws IOException
    {

        // Crea il bottone per aggiungere altri bottoni
        Button addButton = new Button();
        ImageView i = new ImageView();
        //i.setId("card-image");
        i.setImage(new Image(cardPath));
        i.setFitHeight(300/1.5);
        i.setFitWidth(200/1.5);
        addButton.setId("card-button");
        addButton.setAccessibleHelp(cardPath);
        addButton.setGraphic(i);
        addButton.setAccessibleText(cardPath);
        addButton.setPrefHeight(300.0/1.5);
        addButton.setPrefWidth(200.0/1.5);
        addButton.getStyleClass().add("card-button");

        // Gestore MOUSE_ENTERED
        addButton.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
            // Sposta il bottone in una nuova posizione Z (all'inizio della lista dei figli)
            addButton.toFront();
            TranslateTransition tt = new TranslateTransition(Duration.millis(100), addButton);
            tt.setToY(-15); // Sposta verso l'alto di 10px

            tt.play();
        });

        // Gestore MOUSE_EXITED
        addButton.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
            // Animazione per riportare il bottone alla posizione originale lungo l'asse Y
            TranslateTransition tt = new TranslateTransition(Duration.millis(100), addButton);

            tt.setToY(0); // Riporta alla posizione originale lungo l'asse Y

            tt.setOnFinished(event -> {
                // Porta il bottone in fondo alla lista dei figli dopo l'animazione
                addButton.toBack();
            });

            tt.play();
        });

        // Definisco le azioni che deve eseguire il bottone
        addButton.setOnAction(e ->
        {

            if(cardPath.toUpperCase().contains(Caction.DRAWFOUR.getAction()) ||
                    cardPath.toUpperCase().contains(Caction.CHANGECOLOR.getAction()))
            {
                // Creo un Alert per la scelta del colore che l'utente potra' selezionare

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("CHANGECOLOR");
                alert.setHeaderText(null);
                alert.setContentText("Seleziona un colore:");

                // Aggiunta dei pulsanti al popup
                alert.getButtonTypes().setAll(new ButtonType("BLUE"), new ButtonType("RED"), new ButtonType("GREEN"), new ButtonType("YELLOW"));

                // Mostra il popup e attendi la selezione dell'utente
                alert.showAndWait().ifPresent(buttonType ->
                {
                    if (buttonType.getText().equals("BLUE"))
                    {
                        TableImpl.getInstance().getUserPlayer().playCard(addButton.getAccessibleText(), Colour.BLUE);


                    }
                    else if (buttonType.getText().equals("GREEN"))
                    {
                        TableImpl.getInstance().getUserPlayer().playCard(addButton.getAccessibleText(), Colour.GREEN);


                    }
                    else if (buttonType.getText().equals("YELLOW"))
                    {
                        TableImpl.getInstance().getUserPlayer().playCard(addButton.getAccessibleText(), Colour.YELLOW);


                    }
                    else if (buttonType.getText().equals("RED"))
                    {

                        TableImpl.getInstance().getUserPlayer().playCard(addButton.getAccessibleText(), Colour.RED);

                    }
                });


            }
            else
            {
                TableImpl.getInstance().getUserPlayer().playCard(addButton.getAccessibleText(), null);

            }
            this.userHandView.getChildren().remove(addButton);
            updateViewAfterMove();

            this.cicleBotTurns();

        });


        // Aggiungi il bottone "Aggiungi Bottone" al pannello

        this.userHandView.getChildren().add(addButton);
        this.arrangeButtonsInLine(this.userHandView);
        addButton.setDisable(!this.playable(addButton));

    }

    private void arrangeButtonsInLine(Pane container) {
        int numButtons = container.getChildren().size();
        double startX = 0; // Coordinata X di inizio
        double startY = 0; // Coordinata Y fissa per tutti i pulsanti
        double spacing = 80; // Spaziatura orizzontale tra i pulsanti

        for (int i = 0; i < numButtons; i++) {
            Button button = (Button) container.getChildren().get(i);

            // Calcolo della coordinata X per il bottone corrente
            double x = startX + i * spacing;

            // Imposta la posizione del bottone
            button.setLayoutX(x);
            button.setLayoutY(startY);

        }
    }

    public void on_pass()
    {
        TableImpl.getInstance().passTurn();
        this.cicleBotTurns();
    }


}
