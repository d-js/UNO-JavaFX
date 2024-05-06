package com.unofx;

import com.unofx.model.*;
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

    private List<Card> currentUserHand = new ArrayList<>();

    private String imagesPath = "/home/diego/IdeaProjects/unoFX/src/main/resources/com/cardImages/";



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        this.currentCardView.layout();
        this.scene = Controller.scene;
        this.stage = Controller.stage;
        this.root = Controller.root;
        this.userHandView.setPadding(new Insets(10));
        this.userHandView.setSpacing(5);
        this.userHandView.setMaxWidth(1000);

        // Inizia la partita
        this.cicleBotTurns();
    }

    public void cicleBotTurns()
    {
        this.update_view();
        this.hide_user_hand();
        while(Table.getInstance().getCurrentPlayer() instanceof BotPlayer)
        {
            Table.getInstance().getCurrentPlayer().playCard(0);
            this.update_view();
            if(Table.getInstance().control_winner())
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("PARTITA CONCLUSA");
                alert.setHeaderText(null);
                alert.setContentText("Il player " + Table.getInstance().getCurrentPlayer().getUsername() + "Ha vinto la partita.");

                // Mostra il popup e attendi la sua chiusura
                alert.showAndWait();
            }
        }
        this.update_view();
        this.show_user_hand();
        // todo controllare se il player ha vinto
    }


    public void initializeScene(String _fxmlScene, MouseEvent mouseEvent) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(_fxmlScene));
        this.root = fxmlLoader.load();
        stage = (Stage) ((Button) mouseEvent.getSource()).getScene().getWindow();
        this.scene = new Scene(root);

    }

    @FXML
    public void on_deck_click(MouseEvent mouseEvent) throws IOException
    {
        Card e = Table.getInstance().deck.drawOut();
        Table.getInstance().getSitDownPlayer().stream().forEach(p -> {
            if(p instanceof UserPlayer)
                p.drawCard(e);
        });
        draw(generate_imagePath(e.getName()));
    }



    public void update_view()
    {
        this.setCurrentCardImage(this.generate_imagePath(Table.getInstance().getCurrentCard().getName()));

        List<Card> updated_cardlist = Table.getInstance().getCurrentPlayer().getHand();


        List<String> nameCardList = updated_cardlist.stream().map(e -> e.getName()).collect(Collectors.toList());

        this.updateHand(nameCardList);
    }

    private void updateHand(List<String> nameCardList)
    {
        this.userHandView.getChildren().clear();

        for(String cardName : nameCardList){
            try {
                this.draw(this.generate_imagePath(cardName));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public void hide_user_hand()
    {
        for(Node n: this.userHandView.getChildren())
        {
            n.setDisable(true);
        }
        this.deck.setDisable(true);
    }

    // Rendi usabili le carte dell'utente
    public void show_user_hand()
    {
        for(Node n: this.userHandView.getChildren())
        {
            n.setDisable(false);
        }
        this.deck.setDisable(false);
    }

    public void setCurrentCardImage(String _file_path)
    {
        Image i = new Image(_file_path);
        ImageView iw = new ImageView();
        iw.setImage(i);
        iw.setFitWidth(currentCardView.getWidth());
        iw.setFitHeight(currentCardView.getHeight());

        this.currentCardView.getChildren().add(iw);
    }

    public String generate_imagePath(String cardName)
    {
        cardName = cardName.replaceFirst(cardName.substring(0, 1), cardName.substring(0, 1).toUpperCase());
        return "file:" + this.imagesPath + cardName + ".png";
    }

    public int countButtons()
    {
        int count = 0;
        for (Node node : this.root.getChildrenUnmodifiable())
        {
            if (node instanceof Button)
            {
                count++;
            }
        }
        return count;
    }

    public void draw(String cardPath) throws IOException
    {
        System.out.println(cardPath);

        // Crea il bottone per aggiungere altri bottoni
        Button addButton = new Button();
        ImageView i = new ImageView();
        i.setImage(new Image(cardPath));
        i.setFitHeight(100);
        i.setFitWidth(75);
        addButton.setGraphic(i);

        // Definisco le azioni che deve eseguire il bottone
        addButton.setOnAction(e ->
        {

            AtomicReference<Event> verify_played_card = new AtomicReference<>(); ;
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

                        Card c = Table.getInstance().getCurrentPlayer().playCard(this.userHandView.getChildren().indexOf(addButton));
                        ((ActionCard)c).setChoosenColour(Colour.BLUE);
                        verify_played_card.set(Table.getInstance().play_card(c));

                    }
                    else if (buttonType.getText() == "GREEN") {

                        Card c = Table.getInstance().getCurrentPlayer().playCard(this.userHandView.getChildren().indexOf(addButton));
                        ((ActionCard)c).setChoosenColour(Colour.GREEN);
                        verify_played_card.set(Table.getInstance().play_card(c));
                    }
                    else if (buttonType.getText() == "YELLOW")
                    {
                        Card c = Table.getInstance().getCurrentPlayer().playCard(this.userHandView.getChildren().indexOf(addButton));
                        ((ActionCard)c).setChoosenColour(Colour.YELLOW);
                        verify_played_card.set(Table.getInstance().play_card(c));

                    }
                    else if (buttonType.getText() == "RED")
                    {
                        Card c = Table.getInstance().getCurrentPlayer().playCard(this.userHandView.getChildren().indexOf(addButton));
                        ((ActionCard)c).setChoosenColour(Colour.RED);
                        verify_played_card.set(Table.getInstance().play_card(c));
                    }
                });


            }
            else
            {
                Card c = Table.getInstance().getCurrentPlayer().playCard(this.userHandView.getChildren().indexOf(addButton));
                verify_played_card.set(Table.getInstance().play_card(c));
            }


            if(verify_played_card.get() == Event.ALLDONE)
            {
                this.setCurrentCardImage(cardPath);
                this.userHandView.getChildren().remove(addButton);
                this.cicleBotTurns();

            } else if (verify_played_card.get() == Event.CHANGECARD) {

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("CARTA NON VALIDA");
                alert.setHeaderText(null);
                alert.setContentText("La carta selezionata non rientra nei parametri per essere giocata al momento," +
                        " rileggi le regole per capire le carte che si possono giocare in base alla carta corrente del tavolo.");

                // Mostra il popup e attendi la sua chiusura
                alert.showAndWait();
            }
            else if(verify_played_card.get() == Event.BLOCKED)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("BLOCCATO");
                alert.setHeaderText(null);
                alert.setContentText("Salti questo turno, il giocatore prima di te ti ha bloccato.");

                // Mostra il popup
                alert.show();
            }

        });


        // Aggiungi il bottone "Aggiungi Bottone" al pannello
        this.userHandView.getChildren().add(addButton);

    }
}
