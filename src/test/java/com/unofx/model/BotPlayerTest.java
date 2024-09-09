package com.unofx.model;

import com.unofx.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class BotPlayerTest {

    private BotPlayer botPlayer;
    private Table table = Table.getInstance();

    @BeforeEach
    void setUp() {
        table.reset();
        botPlayer = new BotPlayer("Bot");
        table = Table.getInstance();

        Table.getInstance().addPlayerInTable(botPlayer);
        Table.getInstance().setCurrentPlayer();
        Table.getInstance().setDeck();
    }

    @Test
    void testGetUsername() {
        assertEquals("Bot", botPlayer.getUsername());
    }

    @Test
    void testDrawCard() {
        Card card = new NormalCard(Number.SEVEN, Colour.RED);
        botPlayer.drawCard(card);
        assertEquals(1, botPlayer.getHand().size());
        assertEquals("sevenred", botPlayer.getHand().get(0).getName());
    }

    @Test
    void testPlayCardValid() {
        // Setup una carta nel tavolo
        Table.getInstance().setCurrentCardInformation(new NormalCard(Number.FIVE, Colour.RED));

        // Aggiungiamo una carta giocabile alla mano del bot
        NormalCard cardToPlay = new NormalCard(Number.SEVEN, Colour.RED);
        botPlayer.drawCard(cardToPlay);

        botPlayer.playCard(0); // Il bot gioca la carta

        // Verifica che la carta sia stata giocata
        assertEquals(0, botPlayer.getHand().size());
        assertEquals(cardToPlay, table.getCurrentCard());
    }

    @Test
    void testPlayCardInvalidThenDraw() {
        // Setup una carta nel tavolo
        NormalCard currentCard = new NormalCard(Number.FIVE, Colour.RED);
        table.setCurrentCardInformation(currentCard);

        // Aggiungiamo una carta non giocabile alla mano del bot
        NormalCard cardToPlay = new NormalCard(Number.THREE, Colour.BLUE);
        botPlayer.drawCard(cardToPlay);

        botPlayer.playCard(0); // Il bot non può giocare e pesca una nuova carta

        // Verifica che la carta non è stata giocata e una nuova carta è stata pescata
        assertEquals(2, botPlayer.getHand().size());
        assertEquals("threeblue", botPlayer.getHand().get(0).getName());
    }

    @Test
    void testSetAndGetOne() {
        assertFalse(botPlayer.isOne());

        botPlayer.setOneTrue();
        assertTrue(botPlayer.isOne());

        botPlayer.setOneFalse();
        assertFalse(botPlayer.isOne());
    }

    @Test
    void testBlock() {
        assertFalse(botPlayer.is_blocked());

        botPlayer.setBlock();
        assertTrue(botPlayer.is_blocked());

        botPlayer.removeBlock();
        assertFalse(botPlayer.is_blocked());
    }

    @Test
    void testIsCardValidNormalCardAgainstNormalCard() {
        NormalCard cardToPlay = new NormalCard(Number.FIVE, Colour.RED);
        Table.getInstance().setCurrentCardInformation(new NormalCard(Number.SEVEN, Colour.RED));

        assertTrue(botPlayer.isCardValid(cardToPlay));
    }

    @Test
    void testIsCardValidActionCardAgainstNormalCard() {
        ActionCard cardToPlay = new ActionCard(Caction.DRAWFOUR, Colour.BLACK);
        Table.getInstance().setCurrentCardInformation(new NormalCard(Number.SEVEN, Colour.RED));

        assertTrue(botPlayer.isCardValid(cardToPlay));
    }

    @Test
    void testGetInfoHand() {
        Card card1 = new NormalCard(Number.FIVE, Colour.RED);
        Card card2 = new ActionCard(Caction.CHANGECOLOR, Colour.BLACK);

        botPlayer.drawCard(card1);
        botPlayer.drawCard(card2);

        List<String> handInfo = botPlayer.get_info_hand();
        assertEquals(2, handInfo.size());
        assertTrue(handInfo.contains("fivered"));
        assertTrue(handInfo.contains("changecolor"));
    }
}
