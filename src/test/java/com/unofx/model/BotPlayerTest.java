package com.unofx.model;

import com.unofx.model.classes.ActionCard;
import com.unofx.model.classes.BotPlayer;
import com.unofx.model.classes.NormalCard;
import com.unofx.model.classes.TableImpl;
import com.unofx.model.enums.Caction;
import com.unofx.model.enums.Colour;
import com.unofx.model.enums.Number;
import com.unofx.model.interfaces.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class BotPlayerTest {

    private BotPlayer botPlayer;
    private TableImpl tableImpl = TableImpl.getInstance();

    @BeforeEach
    void setUp()
    {
        tableImpl.reset();
        botPlayer = new BotPlayer("Bot");
        tableImpl = TableImpl.getInstance();

        TableImpl.getInstance().addPlayerInTable(botPlayer);
        TableImpl.getInstance().setCurrentPlayer();
        TableImpl.getInstance().setDeck();
    }

    @Test
    void testGetUsername()
    {
        assertEquals("Bot", botPlayer.getUsername());
    }

    @Test
    void testDrawCard()
    {
        Card card = new NormalCard(com.unofx.model.enums.Number.SEVEN, Colour.RED);
        botPlayer.drawCard(card);

        assertEquals(1, botPlayer.getHand().size());
        assertEquals("sevenred", botPlayer.getHand().get(0).getName());
    }

    @Test
    void testPlayCardValid()
    {
        // Setup una carta nel tavolo
        TableImpl.getInstance().setCurrentCardInformation(new NormalCard(com.unofx.model.enums.Number.FIVE, Colour.RED));

        // Aggiungiamo una carta giocabile alla mano del bot
        NormalCard cardToPlay = new NormalCard(com.unofx.model.enums.Number.SEVEN, Colour.RED);
        botPlayer.drawCard(cardToPlay);

        botPlayer.playCard(0); // Il bot gioca la carta

        // Verifica che la carta sia stata giocata
        assertEquals(0, botPlayer.getHand().size());
        assertEquals(cardToPlay, tableImpl.getCurrentCard());
    }

    @Test
    void testPlayCardInvalidThenDraw()
    {
        // Setup una carta nel tavolo
        NormalCard currentCard = new NormalCard(com.unofx.model.enums.Number.FIVE, Colour.RED);
        tableImpl.setCurrentCardInformation(currentCard);

        // Aggiungiamo una carta non giocabile alla mano del bot
        NormalCard cardToPlay = new NormalCard(com.unofx.model.enums.Number.THREE, Colour.BLUE);
        botPlayer.drawCard(cardToPlay);

        botPlayer.playCard(0); // Il bot non può giocare e pesca una nuova carta

        // Verifica che la carta non è stata giocata e una nuova carta è stata pescata
        assertEquals(2, botPlayer.getHand().size());
        assertEquals("threeblue", botPlayer.getHand().get(0).getName());
    }

    @Test
    void testSetAndGetOne()
    {
        assertFalse(botPlayer.isOne());

        botPlayer.setOneTrue();
        assertTrue(botPlayer.isOne());

        botPlayer.setOneFalse();
        assertFalse(botPlayer.isOne());
    }

    @Test
    void testBlock()
    {
        assertFalse(botPlayer.is_blocked());

        botPlayer.setBlock();
        assertTrue(botPlayer.is_blocked());

        botPlayer.removeBlock();
        assertFalse(botPlayer.is_blocked());
    }

    @Test
    void testIsCardValidNormalCardAgainstNormalCard()
    {
        NormalCard cardToPlay = new NormalCard(com.unofx.model.enums.Number.FIVE, Colour.RED);
        TableImpl.getInstance().setCurrentCardInformation(new NormalCard(com.unofx.model.enums.Number.SEVEN, Colour.RED));
        assertTrue(botPlayer.isCardValid(cardToPlay));
    }

    @Test
    void testIsCardValidActionCardAgainstNormalCard()
    {
        ActionCard cardToPlay = new ActionCard(Caction.DRAWFOUR, Colour.BLACK);
        TableImpl.getInstance().setCurrentCardInformation(new NormalCard(com.unofx.model.enums.Number.SEVEN, Colour.RED));

        assertTrue(botPlayer.isCardValid(cardToPlay));
    }

    @Test
    void testGetInfoHand()
    {
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
