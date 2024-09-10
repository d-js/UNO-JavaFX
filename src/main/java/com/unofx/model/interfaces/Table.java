package com.unofx.model.interfaces;
import com.unofx.model.classes.UserPlayer;

import java.util.List;

public interface Table
{

    // Metodo per ottenere l'istanza di Table (singleton)
    static Table getInstance() {
        return Table.getInstance();
    }

    // Aggiunge un giocatore alla partita
    void addPlayerInTable(Player e);

    // Restituisce il prossimo giocatore umano (UserPlayer)
    UserPlayer getUserPlayer();

    // Restituisce un UserPlayer cercato per username
    UserPlayer getUserPlayerByUsername(String Username);

    // Restituisce la lista di tutti i giocatori umani
    List<UserPlayer> getAllUser();

    // Restituisce il giocatore corrente
    Player getCurrentPlayer();

    // Imposta la lista dei giocatori nella partita
    void setPlayerList(int numberOfPlayer);

    // Distribuisce le carte iniziali ai giocatori
    void giveStartCard();

    // Restituisce la carta corrente sul tavolo
    Card getCurrentCard();

    // Imposta il mazzo di carte
    void setDeck();

    // Imposta il giocatore corrente
    void setCurrentPlayer();

    // Imposta le informazioni della carta corrente
    void setCurrentCardInformation(Card currentCard);

    // Controlla se c'è un vincitore
    boolean controlWinner();

    // Permette a un giocatore di giocare una carta
    void playCard(Card e);

    // Controlla se un giocatore ha solo una carta rimanente
    void controlIfOneCard();

    // Cambia il giro dei giocatori (inverte l'ordine)
    void changeLap();

    // Blocca il turno del prossimo giocatore
    void blockNext();

    // Passa il turno al giocatore successivo
    void passTurn();

    // Restituisce la lista dei giocatori seduti
    List<Player> getSitDownPlayer();

    // Reset delle impostazioni della partita
    void reset();

}
