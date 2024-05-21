package com.unofx;

import com.sun.tools.javac.Main;
import com.unofx.model.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import javafx.application.Platform;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class GameController implements Initializable {

    // View scene var
    private Scene scene;
    private Stage stage;
    private Parent root;
    //TODO fare file .env (come si usa e dove crearlo)

    // Game var
    @FXML
    private Pane currentCardView = new Pane();

    @FXML
    private HBox userHandView = new HBox();

    @FXML
    private Button deck = new Button();

    @FXML
    private Button pass = new Button();

    private List<Card> currentUserHand = new ArrayList<>();

    private String imagesPath = "/home/diego/IdeaProjects/unoFX/src/main/resources/com/cardImages/";



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        this.scene = Controller.scene;
        this.stage = Controller.stage;
        this.root = Controller.root;
        this.userHandView.setPadding(new Insets(10));
        this.userHandView.setSpacing(5);
        this.userHandView.setMaxWidth(1000);

        this.update_view();
        this.setCurrentCardImage(this.generate_imagePath(Table.getInstance().getCurrentCard().getName()));
        this.show_user_hand();
        // Inizia la partita
        this.cicleBotTurns();
    }

    public void cicleBotTurns() {

        this.deck.setDisable(false);
        hide_user_hand();


            // TODO il cambiogiro giocato dall'ultimo bot non funziona
            // TODO il blocco giocato dal bot non blocca l'utente

            // TODO messaggio di output per la partita vinta (controlla modello)
            // TODO tutte i +2 / +4 giocati dai bot hanno effetto sull'user e ignorano i bot

            // TODO forse il blocco da bot a bot e' permanente, un bot bloccato una volta non gioca piu' (da risolvere)
            // TODO impostare un delay tra la giocata di carte dei bot (last)
            // TODO ALLE VOLTE IL PROGRAMMA TERMINA


            // Il controllo per l'utente e' fatto dalla vista, quello per i bot e' fatto nel loro metodo

        while (!Table.getInstance().control_winner()) {

            this.pass.setDisable(true);

            // Controllo se il giocatore corrente è un bot e se la partita non è ancora conclusa
            if (!(Table.getInstance().getCurrentPlayer() instanceof BotPlayer))
            {

                this.show_user_hand();
                break; // Esce dal ciclo se il giocatore corrente non è un bot o se la partita è finita
            }

            // Gioca una carta
            System.out.println("LA SUA MANO: " + Table.getInstance().getCurrentPlayer().get_info_hand());
            Card card = Table.getInstance().getCurrentPlayer().playCard(0);

            Table.getInstance().play_card(card);


            // Aggiorna l'immagine della carta corrente
            this.setCurrentCardImage(this.generate_imagePath(Table.getInstance().getCurrentCard().getName()));
        }

        if(Table.getInstance().control_winner())
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("FINE");
            alert.setHeaderText("qualcuno ha vinto");
            alert.setContentText("partita conclusa");
            // Mostra il popup e attendi la sua chiusura
            alert.showAndWait();
        }
    }

    public void initializeScene(String _fxmlScene) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(_fxmlScene));
        this.root = fxmlLoader.load();
        this.scene = new Scene(root);

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
        this.stage.setFullScreen(true);
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
                    n.setDisable(!((Button) n).getText().toLowerCase().contains(Table.getInstance().getCurrentCard().getColor().getColour().toLowerCase()) &&
                            !((Button) n).getText().toLowerCase().contains(Table.getInstance().getCurrentCard().getAction().getAction().toLowerCase()) &&
                            !((Button) n).getText().toLowerCase().contains(Caction.CHANGECOLOR.getAction().toLowerCase()) &&
                            !((Button) n).getText().toLowerCase().contains(Caction.DRAWFOUR.getAction().toLowerCase()));
                }
            }
        });
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
        return "file:" + this.imagesPath + cardName + ".png";
    }


    public void draw(String cardPath) throws IOException
    {

        // Crea il bottone per aggiungere altri bottoni
        Button addButton = new Button();
        ImageView i = new ImageView();
        i.setImage(new Image(cardPath));
        i.setFitHeight(100);
        i.setFitWidth(75);
        addButton.setAccessibleHelp(cardPath);
        addButton.setGraphic(i);
        addButton.setText(cardPath);

        addButton.setDisable(!((Button) addButton).getText().toLowerCase().contains(Table.getInstance().getCurrentCard().getColor().getColour().toLowerCase()) &&
                !((Button) addButton).getText().toLowerCase().contains(Table.getInstance().getCurrentCard().getAction().getAction().toLowerCase()) &&
                !((Button) addButton).getText().toLowerCase().contains(Caction.CHANGECOLOR.getAction().toLowerCase()) &&
                !((Button) addButton).getText().toLowerCase().contains(Caction.DRAWFOUR.getAction().toLowerCase()));

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

                        Card c = Table.getInstance().getUserPlayer().playCard(this.userHandView.getChildren().indexOf(addButton));
                        ((ActionCard)c).setChoosenColour(Colour.BLUE);
                        Table.getInstance().play_card(c);

                    }
                    else if (buttonType.getText() == "GREEN") {

                        Card c = Table.getInstance().getUserPlayer().playCard(this.userHandView.getChildren().indexOf(addButton));
                        ((ActionCard)c).setChoosenColour(Colour.GREEN);
                        Table.getInstance().play_card(c);
                    }
                    else if (buttonType.getText() == "YELLOW")
                    {
                        Card c = Table.getInstance().getUserPlayer().playCard(this.userHandView.getChildren().indexOf(addButton));
                        ((ActionCard)c).setChoosenColour(Colour.YELLOW);
                        Table.getInstance().play_card(c);

                    }
                    else if (buttonType.getText() == "RED")
                    {
                        Card c = Table.getInstance().getUserPlayer().playCard(this.userHandView.getChildren().indexOf(addButton));
                        ((ActionCard)c).setChoosenColour(Colour.RED);
                        Table.getInstance().play_card(c);
                    }
                });


            }
            else
            {
                Card c = Table.getInstance().getUserPlayer().playCard(this.userHandView.getChildren().indexOf(addButton));
                Table.getInstance().play_card(c);
            }


            this.userHandView.getChildren().remove(addButton);
            this.cicleBotTurns();


        });


        // Aggiungi il bottone "Aggiungi Bottone" al pannello
        this.userHandView.getChildren().add(addButton);
        this.pass.setDisable(false);

    }

    public void on_pass(MouseEvent mouseEvent)
    {
        Table.getInstance().next_turn();
        this.cicleBotTurns();
    }
}
