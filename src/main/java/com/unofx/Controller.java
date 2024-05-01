package com.unofx;

import com.unofx.model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Controller {
    @FXML
    private Label welcomeText;
    private Scene scene;
    private Stage stage;
    private Parent root;

    private int count = 0;
    @FXML
    private HBox userHand = new HBox();
    @FXML
    private Pane currentCard = new Pane();

    private List<Button> cardList = new ArrayList<>();
    private String imagesPath = "/home/diego/IdeaProjects/UNOfx/src/main/resources/com/cardImages/";


    public void initialize(String _fxmlScene, MouseEvent mouseEvent) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(_fxmlScene));
        this.root = fxmlLoader.load();
        stage = (Stage) ((Button) mouseEvent.getSource()).getScene().getWindow();
        this.scene = new Scene(root);

    }

    public void update() {
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


    public void start_game() throws IOException {
        // TODO settare il metodo inizialize
        this.userHand.setPadding(new Insets(10));
        this.userHand.setSpacing(5);
        this.userHand.setMaxWidth(this.userHand.getWidth());

        Table.getInstance().set_deck();
        Table.getInstance().deck.set_initial_card();
        Table.getInstance().set_player_list(4);
        Table.getInstance().setCurrentPlayer();
        Table.getInstance().give_start_card();
        List<String> user_card_list= Table.getInstance().get_user_info_card();
        this.set_start_card(user_card_list);

        /*
        while(Table.getInstance().control_winner() == false)
        {
            for(Player p : Table.getInstance().getSitDownPlayer())
            {
                //TODO ismyturn deve controllare se il currentplayer corrisponde al player user
                if(Table.getInstance().is_my_turn() == true)
                {
                    //TODO gioca la carta l'utente
                    this.show_user_hand();

                    this.hide_user_hand();
                }
                else
                {
                    //TODO gioca la carta il bot, non bisogna passare nessun parametro
                    //TODO gestire tutto il turno nel metodo playcard in modo che al passaggio al player successivo
                    // la classe sia pronta a far giocare il player successivo
                    Table.getInstance().play_card(1, null);
                }
            }
        }*/
    }

    public void set_start_card(List<String> e) throws IOException
    {
        // Setto la carta iniziale sia nel model che nella vista
        Card c = Table.getInstance().deck.drawOut();
        Table.getInstance().setCurrentCard(c);
        Table.getInstance().deck.playCard(c);
        this.set_currentCard(this.generate_imagePath(c.getName()));

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
        initialize("game_pane.fxml", mouseEvent);
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
        List<String> user_card_list= Table.getInstance().get_user_info_card();
        for (String s: user_card_list)
        {
            draw(generate_imagePath(s));
        }
    }


    public void set_currentCard(String _file_path)
    {
        Image i = new Image(_file_path);
        ImageView iw = new ImageView();
        iw.setImage(i);
        iw.setFitWidth(currentCard.getWidth());
        iw.setFitHeight(currentCard.getHeight());

        this.currentCard.getChildren().add(iw);
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

                        verify_played_card.set(Table.getInstance().play_card(this.cardList.indexOf(addButton), Colour.BLUE));

                    }
                    else if (buttonType.getText() == "GREEN") {

                        verify_played_card.set(Table.getInstance().play_card(this.cardList.indexOf(addButton), Colour.GREEN));

                    }
                    else if (buttonType.getText() == "YELLOW")
                    {

                        verify_played_card.set(Table.getInstance().play_card(this.cardList.indexOf(addButton), Colour.YELLOW));

                    }
                    else if (buttonType.getText() == "RED")
                    {

                        verify_played_card.set(Table.getInstance().play_card(this.userHand.getChildren().indexOf(addButton), Colour.RED));
                    }
                });


            }
            else
            {
                System.out.println(this.cardList.indexOf(addButton));
                verify_played_card.set(Table.getInstance().play_card(this.userHand.getChildren().indexOf(addButton), null));
            }


            if(verify_played_card.get() == Event.ALLDONE)
            {
                this.set_currentCard(cardPath);
                this.userHand.getChildren().remove(addButton);

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
        this.userHand.getChildren().add(addButton);
    }
}