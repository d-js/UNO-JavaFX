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
    private Scene scene;
    private Stage stage;
    private Parent root;

    // View game var
    @FXML
    private Pane currentCardView = new Pane();
    @FXML
    private HBox userHandView = new HBox();

    //TODO fare file .env (come si usa e dove crearlo)
    private String imagesPath = "/home/diego/IdeaProjects/unoFX/src/main/resources/com/cardImages/";

    // Not useful
    public List<Card> updated_cardlist = new ArrayList<>();

    public void initializeScene(String _fxmlScene, MouseEvent mouseEvent) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(_fxmlScene));
        this.root = fxmlLoader.load();
        stage = (Stage) ((Button) mouseEvent.getSource()).getScene().getWindow();
        this.scene = new Scene(root);
        // Set userhand information
        this.userHandView.setPadding(new Insets(10));
        this.userHandView.setSpacing(5);
        this.userHandView.setMaxWidth(this.userHandView.getWidth());


        // Creazione di uno ScrollPane per contenere l'HBox
        ScrollPane scrollPane = new ScrollPane();
        // Imposta la politica di scroll orizzontale
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        // Imposta il contenuto dello ScrollPane all'HBox
        scrollPane.setContent(userHandView);
        scrollPane.setMaxWidth(Double.MAX_VALUE);

    }

    public void inizializeTable() throws IOException
    {

        Table.getInstance().set_deck();
        Table.getInstance().deck.set_initial_card();
        Table.getInstance().set_player_list(4);
        Table.getInstance().setCurrentPlayer();
        Table.getInstance().give_start_card();
        List<String> user_card_list= Table.getInstance().get_user_info_card();
        this.set_user_card(user_card_list);

        Card c = Table.getInstance().deck.drawOut();
        Table.getInstance().setCurrentCard(c);
        Table.getInstance().deck.playCard(c);
        this.set_currentCard(this.generate_imagePath(c.getName()));

    }

    public void startBots()
    {
        for(Player p : Table.getInstance().getSitDownPlayer())
        {
            if(p instanceof BotPlayer)
            {
                Card e;
                do {

                    e = p.playCard(0); // l'index per il bot non verra' considerato per la selezione della carta

                }while(Table.getInstance().play_card(e) == Event.CHANGECARD);

                this.set_currentCard(e.getName());
            }
        }
    }

    public void update_hand()
    {
        this.updated_cardlist = Table.getInstance().getCurrentPlayer().getHand();

        List<String> nameCardList = this.updated_cardlist.stream().map(e -> e.getName()).collect(Collectors.toList());

        try {
            this.set_user_card(nameCardList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
    public void hide_user_hand()
    {
        for(int i = 1; i < this.countButtons() + 1; i++) {
            Button button = (Button) this.root.lookup("#cardbutton" + i);
            button.setDisable(true);
        }
    }

    // Rendi usabili le carte dell'utente
    public void show_user_hand()
    {
        for(int i = 1; i < this.countButtons() + 1; i++) {
            Button button = (Button) this.root.lookup("#cardbutton" + i);
            button.setDisable(false);
        }
    }

    public void start_game() throws IOException
    {

        this.inizializeTable();
        Pane n = new Pane();

        while(!Table.getInstance().control_winner())
        {
            while(Table.getInstance().getCurrentPlayer() instanceof UserPlayer)
            {
                this.show_user_hand();
            }

            for(Player p : Table.getInstance().getSitDownPlayer())
            {
                p.playCard(9);
                this.set_currentCard(Table.getInstance().getCurrentCard().getName());
            }

            this.update_hand();
        }
    }

    public void endTurn()
    {
        this.hide_user_hand();
    }

    public void set_user_card(List<String> e) throws IOException
    {
        // Do le carte a tutti i giocatori, mostro nella vista solamente quelle dei player giocanti tralasciando i bot
        for (String s: e)
        {
            draw(generate_imagePath(s));
        }
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

    @FXML
    public void on_start(MouseEvent mouseEvent) throws IOException
    {
        initializeScene("game_pane.fxml", mouseEvent);
        update();
        this.start_game();
    }

    @FXML
    public void on_nickname(InputMethodEvent _inputMethodEvent)
    {
        String nickname = _inputMethodEvent.getCommitted();
        Table.getInstance().set_username(nickname);
        System.out.println(nickname);

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


    public void set_currentCard(String _file_path)
    {
        Image i = new Image(_file_path);
        ImageView iw = new ImageView();
        iw.setImage(i);
        iw.setFitWidth(currentCardView.getWidth());
        iw.setFitHeight(currentCardView.getHeight());

        this.currentCardView.getChildren().add(iw);
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
                this.set_currentCard(cardPath);
                this.userHandView.getChildren().remove(addButton);

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

            this.endTurn();
        });


        // Aggiungi il bottone "Aggiungi Bottone" al pannello
        this.userHandView.getChildren().add(addButton);

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}