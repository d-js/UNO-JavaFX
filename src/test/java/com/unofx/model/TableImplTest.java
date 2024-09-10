package com.unofx.model;

import static org.junit.jupiter.api.Assertions.*;

import com.unofx.model.classes.ActionCard;
import com.unofx.model.classes.BotPlayer;
import com.unofx.model.classes.TableImpl;
import com.unofx.model.classes.UserPlayer;
import com.unofx.model.enums.Caction;
import com.unofx.model.enums.Colour;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TableImplTest {

    private TableImpl tableImpl = TableImpl.getInstance();
    private UserPlayer player1;
    private UserPlayer player2;
    private BotPlayer bot1;

    @BeforeEach
    public void setup() {

        tableImpl.reset();
        tableImpl = TableImpl.getInstance();
        player1 = new UserPlayer("Player1");
        player2 = new UserPlayer("Player2");
        bot1 = new BotPlayer("Bot1");

    }

    @Test
    public void testSingletonInstance() {
        TableImpl instance1 = TableImpl.getInstance();
        TableImpl instance2 = TableImpl.getInstance();
        assertSame(instance1, instance2, "Table should be a singleton");
    }

    @Test
    public void testAddPlayerInTable() {
        tableImpl.addPlayerInTable(player1);
        tableImpl.addPlayerInTable(player2);
        List<UserPlayer> players = tableImpl.getAllUser();
        assertEquals(2, players.size(), "There should be two players added to the table");
        assertTrue(players.contains(player1), "Player1 should be in the list");
        assertTrue(players.contains(player2), "Player2 should be in the list");
    }

    @Test
    public void testSetPlayerList() {
        tableImpl.setPlayerList(2);
        assertEquals(2, tableImpl.getSitDownPlayer().size(), "There should be 2 bot players added");
        assertTrue(tableImpl.getSitDownPlayer().get(0) instanceof BotPlayer, "The first player should be a bot");
        assertTrue(tableImpl.getSitDownPlayer().get(1) instanceof BotPlayer, "The second player should be a bot");
    }

    @Test
    public void testGetUserPlayerByUsername() {
        tableImpl.addPlayerInTable(player1);
        UserPlayer foundPlayer = tableImpl.getUserPlayerByUsername("Player1");
        assertNotNull(foundPlayer, "Player1 should be found");
        assertEquals(player1, foundPlayer, "Found player should match Player1");

        UserPlayer notFoundPlayer = tableImpl.getUserPlayerByUsername("NonExistent");
        assertNull(notFoundPlayer, "Non-existing player should return null");
    }

    @Test
    public void testGiveStartCard() {
        tableImpl.addPlayerInTable(player1);
        tableImpl.addPlayerInTable(player2);
        tableImpl.setDeck();
        tableImpl.giveStartCard();
        assertEquals(8, player1.getHand().size(), "Player1 should have 8 cards");
        assertEquals(8, player2.getHand().size(), "Player2 should have 8 cards");
    }

    @Test
    public void testSetCurrentPlayer() {
        tableImpl.addPlayerInTable(player1);
        tableImpl.setCurrentPlayer();
        assertEquals(player1, tableImpl.getCurrentPlayer(), "Player1 should be the current player");
    }

    @Test
    public void testControlWinner() {
        tableImpl.addPlayerInTable(player1);
        tableImpl.addPlayerInTable(player2);

        player1.getHand().clear();

        assertTrue(tableImpl.controlWinner(), "Player1 should be the winner because their hand is empty");
    }

    @Test
    public void testPassTurn() {
        tableImpl.addPlayerInTable(player1);
        tableImpl.addPlayerInTable(player2);
        tableImpl.addPlayerInTable(bot1);

        tableImpl.setCurrentPlayer();
        assertEquals(player1, tableImpl.getCurrentPlayer(), "Player1 should be the current player");

        tableImpl.passTurn();
        assertEquals(player2, tableImpl.getCurrentPlayer(), "Player2 should be the current player after Player1");

        tableImpl.passTurn();
        assertEquals(bot1, tableImpl.getCurrentPlayer(), "Bot1 should be the current player after Player2");
    }

    @Test
    public void testBlockNextPlayer() {
        tableImpl.addPlayerInTable(player1);
        tableImpl.addPlayerInTable(player2);
        tableImpl.addPlayerInTable(bot1);

        tableImpl.setCurrentPlayer();
        tableImpl.blockNext();

        assertTrue(player2.is_blocked(), "Player2 should be blocked");

        tableImpl.passTurn();
        assertEquals(bot1, tableImpl.getCurrentPlayer(), "Bot1 should be the current player after Player2 is blocked");

        assertFalse(player2.is_blocked(), "Player2 should no longer be blocked after passing turn");
    }

    @Test
    public void testPlayCardAction_DrawTwo() {
        tableImpl.addPlayerInTable(player1);
        tableImpl.addPlayerInTable(player2);
        tableImpl.setDeck();
        tableImpl.setCurrentPlayer();
        tableImpl.setCurrentCardInformation(TableImpl.getInstance().deck.drawOut());

        ActionCard drawTwoCard = new ActionCard(Caction.DRAWTWO, Colour.BLUE);
        int initialHandSize = player2.getHand().size();
        tableImpl.playCard(drawTwoCard);

        assertEquals(initialHandSize + 2, player2.getHand().size(), "Player2 should draw 2 cards after DRAWTWO action");
    }

}
