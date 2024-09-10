package com.unofx.model;

import static org.junit.jupiter.api.Assertions.*;


import com.unofx.model.classes.Deck;
import com.unofx.model.classes.NormalCard;
import com.unofx.model.enums.Colour;
import com.unofx.model.enums.Number;
import com.unofx.model.interfaces.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class DeckTest {

    private Deck deck;

    @BeforeEach
    public void setUp() {
        deck = new Deck();
    }

    @Test
    public void testInitialDeckIsNotEmpty() {
        // Verifica che il mazzo coperto non sia vuoto dopo la creazione
        assertFalse(deck.getCoverDeck().isEmpty(), "Il mazzo coperto non dovrebbe essere vuoto");
    }

    @Test
    public void testDrawOutRemovesCardFromCoverDeck() {
        int initialSize = deck.getCoverDeck().size();
        Card drawnCard = deck.drawOut();
        assertNotNull(drawnCard, "La carta pescata non dovrebbe essere null");
        assertEquals(initialSize - 1, deck.getCoverDeck().size(), "La dimensione del mazzo coperto dovrebbe diminuire di 1");
    }

    @Test
    public void testDrawOutMovesToUncoverDeckWhenEmpty() {
        // Svuotiamo il mazzo coperto
        while (!deck.getCoverDeck().isEmpty()) {
            deck.drawOut();
        }

        // Ora copriamo di nuovo con drawDown e peschiamo una carta
        Card drawnCard = deck.drawOut();
        assertNotNull(drawnCard, "Dopo aver mescolato con drawDown, il mazzo non dovrebbe restituire null");
    }

    @Test
    public void testPlayCardAddsCardToUncoverDeck() {
        Card card = new NormalCard(Number.ONE, Colour.RED);
        deck.playCard(card);
        assertEquals(card, deck.getLastUncover(), "La carta giocata dovrebbe essere l'ultima nel mazzo scoperto");
    }

    @Test
    public void testShuffleDeck() {
        // Verifica che mescolando il mazzo, la sequenza di carte cambi
        List<Card> originalDeck = new ArrayList<>(deck.getCoverDeck());
        deck.shuffle();
        List<Card> shuffledDeck = deck.getCoverDeck();
        assertNotEquals(originalDeck, shuffledDeck, "Il mazzo dovrebbe essere stato mescolato");
    }

    @Test
    public void testSetInitialCardReturnsNormalCard() {
        Card initialCard = deck.setInitialCard();
        assertNotNull(initialCard, "La carta iniziale non dovrebbe essere null");
        assertTrue(initialCard instanceof NormalCard, "La carta iniziale dovrebbe essere una NormalCard");
    }

    @Test
    public void testDeleteClearsAllDecks() {
        deck.delete();
        assertTrue(deck.getCoverDeck().isEmpty(), "Il mazzo coperto dovrebbe essere vuoto dopo delete()");
        assertTrue(deck.uncoverDeck.isEmpty(), "Il mazzo scoperto dovrebbe essere vuoto dopo delete()");
    }
}