package com.unofx;

import com.unofx.model.Colour;
import com.unofx.model.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseEvent;
import com.unofx.model.Table;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Table.getInstance().set_player_list(8);
        Table.getInstance().start_game(4, false);
        List<String> user_card_list= Table.getInstance().get_user_info_card();
        this.set_start_card(user_card_list);

        /*
        while(Table.getInstance().control_winner() == false)
        {
            if(Table.getInstance().is_my_turn() == true)
            {
                this.show_user_hand();
                Table.getInstance().play_card(1, null);
                this.hide_user_hand();
            }
            else{}

        }*/
    }

    public void set_start_card(List<String> e) throws IOException {
        for (String s: e) {
            draw(generate_imagePath(s));
        }
    }
    
    public String generate_imagePath(String cardName)
    {
        cardName = cardName.replaceFirst(cardName.substring(0, 1), cardName.substring(0, 1).toUpperCase());
        return "file:" + this.imagesPath + cardName + ".png";
    }


    public int countButtons(){
        int count = 0;
        for (Node node : this.root.getChildrenUnmodifiable()) {
            if (node instanceof Button) {
                count++;
            }
        }
        return count;
    }

    @FXML
    public void on_start(MouseEvent mouseEvent) throws IOException {
        initialize("game_pane.fxml", mouseEvent);
        update();
        this.start_game();
    }

    @FXML
    public void on_nickname(InputMethodEvent _inputMethodEvent) {
        String nickname = _inputMethodEvent.getCommitted();
        Table.getInstance().set_username(nickname);
        System.out.println(nickname);

    }

    @FXML
    public void on_deck_click(MouseEvent mouseEvent) throws IOException
    {
        List<String> user_card_list= Table.getInstance().get_user_info_card();
        for (String s: user_card_list) {
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

        addButton.setOnAction(e -> {
            //TODO solo se la carta può essere giocata, aggiunge la carta al pane di carte scoperte, gioca la carta
            this.set_currentCard(cardPath);
            this.userHand.getChildren().remove(addButton);
        });

        // Aggiungi il bottone "Aggiungi Bottone" al pannello
        this.userHand.getChildren().add(addButton);
    }
}