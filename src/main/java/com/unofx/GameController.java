package com.unofx;

import com.unofx.model.*;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import io.github.cdimascio.dotenv.Dotenv;

public class GameController implements Initializable {

    public Pane top_bot;
    public Pane right_bot;
    public Pane left_bot;
    // View scene var
    private Scene scene;
    private Stage stage;
    private Parent root;
    //TODO fare file .env (come si usa e dove crearlo)
    Dotenv dotenv = Dotenv.load();

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
    private Label Name_user = new Label();

    //private String imagesPath = dotenv.get("IMAGES_PATH");    //  scommentare prima del push
    private String imagesPath   =   "src/main/resources/com/cardImages/";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        this.scene = ControllerScene.scene;
        this.stage = ControllerScene.stage;
        this.root = ControllerScene.root;
        this.userHandView.setPadding(new Insets(10));
        Platform.runLater(() -> {
            this.update_view();
            this.setCurrentCardImage(this.generate_imagePath(Table.getInstance().getCurrentCard().getName()));
            this.Name_user.setText("Players name: "+Table.getInstance().getUserPlayer().getUsername());
            this.cicleBotTurns();
        });

        // Inizia la partita
    }

    // TODO problema di alcuni metodi, non funzionano con l'user
    // TODO i blocchi sono permanenti e ritardano di un turno
    // TODO i +4 e i +2 non hanno effetto sull'utente``
    // TODO impostare un delay tra la giocata di carte dei bot (last)
    // TODO ERRORE SPORADICO, a volte quando si gioca una carta non va avanti e aspetta il giocare di un'altra carta
    //  (forse succede con i cambio giro) forse cambiando giro reimposta il turno corrente di nuovo a me quindi devo giocare un'altra carta
    // TODO a volte non rileva la vittoria dell'utente, (forse quando prima si gioca una carta black)
    // TODO se il gioco inizia con un cambio colore o con un +4 non posso giocare niente (a volte) (serve un runLater?)
    // TODO c'e' qualche problema con il cambio giro
    // TODO a volte il gioco si impalla e bisogna chiuderlo, se si riesce e il pc non e' bloccato
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // TODO QUANDO SCRIVO A VOLTE POTREBBE ESSERE IN BASE A SE LA CARTA E' QUELLA INIZIALE NEL TAVOLO OPPURE QUELLA GIOCATA DA UN BOT
    //  dato che per me compare sempre come prima carta

    // TODO il cambio giro cambia giro ma fa riniziare a giocare a me
    // Il controllo per l'utente e' fatto dalla vista, quello per i bot e' fatto nel loro metodo


    public void cicleBotTurns()
    {
        // doppio runlater perche?
        this.deck.setDisable(false);
        Platform.runLater(this::hide_user_hand);
        // Il controllo per l'utente e' fatto dalla vista, quello per i bot e' fatto nel loro metodo
        cicleBotUntilUser();
        if (Table.getInstance().control_winner())
            this.showWinnerAlert();
    }

    private void cicleBotUntilUser()
    {
        if (!Table.getInstance().control_winner())
        {
            this.pass.setDisable(true);
            // Controllo se il giocatore corrente è un bot e se la partita non è ancora conclusa
            if (!(Table.getInstance().getCurrentPlayer() instanceof BotPlayer))
            {
                Platform.runLater(() -> {
                    this.update_view();
                    this.show_user_hand();
                });
            }
            else
            {
                this.playBotMovesWithDelay(((BotPlayer) Table.getInstance().getCurrentPlayer()));
            }
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
            setCurrentCardImage(generate_imagePath(Table.getInstance().getCurrentCard().getName())); // Aggiorna l'immagine della carta corrente
        });
    }

    private void showWinnerAlert()
    {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("FINE");
            alert.setHeaderText("Qualcuno ha vinto");
            alert.setContentText("Partita conclusa");

            ButtonType closeButton = new ButtonType("Chiudi gioco", ButtonBar.ButtonData.OK_DONE);
            ButtonType loadAnotherScreenButton = new ButtonType("continue", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(closeButton, loadAnotherScreenButton);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == closeButton) {
                System.exit(0); // Chiudi l'applicazione
            }
        });
    }

    // TODO al momento il tasto per dire uno bisogna cliccarlo prima di giocare la carta
    @FXML
    public void on_one_card(MouseEvent mouseEvent) throws IOException
    {
        Table.getInstance().getUserPlayer().setOneTrue();
    }

    @FXML
    public void on_deck_click(MouseEvent mouseEvent) throws IOException
    {
        Card e = Table.getInstance().deck.drawOut();
        Table.getInstance().getUserPlayer().drawCard(e);
        this.deck.setDisable(true);
        draw(generate_imagePath(e.getName()));
    }

    public void update_view()
    {
        Platform.runLater(() -> {

            List<String> nameCardList = Table.getInstance().getUserPlayer().getHand().stream().map(Card::getName).collect(Collectors.toList());
            // TODO fare sottrazione con la lista in questo oggetto per ottenere le carte che bisogna aggiungere
            //List<String> nameCardList = updated_cardlist.stream().map(Card::getName).collect(Collectors.toList());

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
                    ((Button) n).setDisable(true);
                }
            }
        });
    }

    public void update()
    {
        this.stage.setScene(this.scene);

        try
        {
            stage.show();
        }catch(Exception e) {
            System.out.println("error on update");
        }
    }

    // Rendi usabili le carte che puo giocare, all'utente
    public void show_user_hand()
    {
        Platform.runLater(() -> {
            for (Node n : this.userHandView.getChildren()) {
                if (n instanceof Button) {
                    // TODO da rivedere
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
        return Table.getInstance().getUserPlayer().isPlayable(n.getAccessibleText());

    }


    public static String capitalize(String str)
    {

        // get the first character of the inputString
        char firstLetter = str.charAt(0);

        // convert it to an UpperCase letter
        char capitalFirstLetter = Character.toUpperCase(firstLetter);

        // return the output string by updating
        //the first char of the input string
        return str.replace(str.charAt(0), capitalFirstLetter);
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

    private void simulateBotMoves() {
        // Example delay between moves
        int delay = 2000; // 2000 milliseconds = 2 seconds

        // Simulate a bot move, and then schedule the next one
        for (int i = 0; i < 5; i++) {
            int moveNumber = i + 1;
            PauseTransition pause = new PauseTransition(Duration.millis(delay * moveNumber));
            pause.setOnFinished(event -> performBotMove(moveNumber));
            pause.play();
        }
    }

    private void performBotMove(int moveNumber) {
        Platform.runLater(() -> {
            // Code to perform the bot move
            System.out.println("il bot aspetta" + moveNumber);
        });
    }

    public void draw(String cardPath) throws IOException
    {

        // Crea il bottone per aggiungere altri bottoni
        Button addButton = new Button();
        ImageView i = new ImageView();
        //i.setId("card-image");
        i.setImage(new Image(cardPath));
        i.setFitHeight(135/1.5);
        i.setFitWidth(100/1.5);
        addButton.setId("card-button");
        addButton.setAccessibleHelp(cardPath);
        addButton.setGraphic(i);
        addButton.setAccessibleText(cardPath);
        //addButton.setStyle("card-button");
        addButton.getStyleClass().add("card-button");

        // Gestore MOUSE_ENTERED
        addButton.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
            // Sposta il bottone in una nuova posizione Z (all'inizio della lista dei figli)
            addButton.toFront();

            TranslateTransition tt = new TranslateTransition(Duration.millis(100), addButton);
            tt.setToY(-10); // Sposta verso l'alto di 10px

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
                // TODO aggiungere al posto della scritta reppresentante il colore la foto del colore selezionabile
                alert.getButtonTypes().setAll(new ButtonType("BLUE"), new ButtonType("RED"), new ButtonType("GREEN"), new ButtonType("YELLOW"));

                // Mostra il popup e attendi la selezione dell'utente
                alert.showAndWait().ifPresent(buttonType ->
                {
                    if (buttonType.getText() == "BLUE") {

                        Table.getInstance().getUserPlayer().playCard(addButton.getAccessibleText());
                        ((ActionCard)Table.getInstance().getCurrentCard()).setColour(Colour.BLUE);

                    }
                    else if (buttonType.getText() == "GREEN") {

                        Table.getInstance().getUserPlayer().playCard(addButton.getAccessibleText());
                        ((ActionCard)Table.getInstance().getCurrentCard()).setColour(Colour.GREEN);

                    }
                    else if (buttonType.getText() == "YELLOW")
                    {
                        Table.getInstance().getUserPlayer().playCard(addButton.getAccessibleText());
                        ((ActionCard)Table.getInstance().getCurrentCard()).setColour(Colour.YELLOW);
                    }
                    else if (buttonType.getText() == "RED")
                    {
                        Table.getInstance().getUserPlayer().playCard(addButton.getAccessibleText());
                        ((ActionCard)Table.getInstance().getCurrentCard()).setColour(Colour.RED);

                    }
                });


            }
            else
            {
                Table.getInstance().getUserPlayer().playCard(addButton.getAccessibleText());

            }
            this.userHandView.getChildren().remove(addButton);
            updateViewAfterMove();
            this.cicleBotTurns();

        });


        // Aggiungi il bottone "Aggiungi Bottone" al pannello

        this.userHandView.getChildren().add(addButton);
        this.arrangeButtonsInLine(this.userHandView);
        addButton.setDisable(!this.playable(addButton));
        this.pass.setDisable(false);

    }

    private void arrangeButtonsInLine(Pane container) {
        int numButtons = container.getChildren().size();
        double startX = 0; // Coordinata X di inizio
        double startY = 0; // Coordinata Y fissa per tutti i pulsanti
        double spacing = 50; // Spaziatura orizzontale tra i pulsanti

        for (int i = 0; i < numButtons; i++) {
            Button button = (Button) container.getChildren().get(i);

            // Calcolo della coordinata X per il bottone corrente
            double x = startX + i * spacing;

            // Imposta la posizione del bottone
            button.setLayoutX(x);
            button.setLayoutY(startY);
        }
    }

    public void on_pass(MouseEvent mouseEvent)
    {
        Table.getInstance().next_turn();
        this.cicleBotTurns();
    }
}
