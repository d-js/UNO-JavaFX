package com.unofx.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TableTest {

    private Table table = Table.getInstance();
    private UserPlayer player1;
    private UserPlayer player2;
    private BotPlayer bot1;

    @BeforeEach
    public void setup() {

        table.reset(); // Reset the table before each test
        table = Table.getInstance();
        player1 = new UserPlayer("Player1");
        player2 = new UserPlayer("Player2");
        bot1 = new BotPlayer("Bot1");

    }

    @Test
    public void testSingletonInstance() {
        Table instance1 = Table.getInstance();
        Table instance2 = Table.getInstance();
        assertSame(instance1, instance2, "Table should be a singleton");
    }

    @Test
    public void testAddPlayerInTable() {
        table.addPlayerInTable(player1);
        table.addPlayerInTable(player2);
        List<UserPlayer> players = table.getAllUser();
        assertEquals(2, players.size(), "There should be two players added to the table");
        assertTrue(players.contains(player1), "Player1 should be in the list");
        assertTrue(players.contains(player2), "Player2 should be in the list");
    }

    @Test
    public void testSetPlayerList() {
        table.setPlayerList(2); // Add 2 bots
        assertEquals(2, table.getSitDownPlayer().size(), "There should be 2 bot players added");
        assertTrue(table.getSitDownPlayer().get(0) instanceof BotPlayer, "The first player should be a bot");
        assertTrue(table.getSitDownPlayer().get(1) instanceof BotPlayer, "The second player should be a bot");
    }

    @Test
    public void testGetUserPlayerByUsername() {
        table.addPlayerInTable(player1);
        UserPlayer foundPlayer = table.getUserPlayerByUsername("Player1");
        assertNotNull(foundPlayer, "Player1 should be found");
        assertEquals(player1, foundPlayer, "Found player should match Player1");

        UserPlayer notFoundPlayer = table.getUserPlayerByUsername("NonExistent");
        assertNull(notFoundPlayer, "Non-existing player should return null");
    }

    @Test
    public void testGiveStartCard() {
        table.addPlayerInTable(player1);
        table.addPlayerInTable(player2);
        table.setDeck(); // Setup the deck
        table.giveStartCard();
        assertEquals(8, player1.getHand().size(), "Player1 should have 8 cards");
        assertEquals(8, player2.getHand().size(), "Player2 should have 8 cards");
    }

    @Test
    public void testSetCurrentPlayer() {
        table.addPlayerInTable(player1);
        table.setCurrentPlayer();
        assertEquals(player1, table.getCurrentPlayer(), "Player1 should be the current player");
    }

    @Test
    public void testControlWinner() {
        table.addPlayerInTable(player1);
        table.addPlayerInTable(player2);

        // Simulate player1 winning (empty hand)
        player1.getHand().clear();

        assertTrue(table.controlWinner(), "Player1 should be the winner because their hand is empty");
    }

    @Test
    public void testPassTurn() {
        table.addPlayerInTable(player1);
        table.addPlayerInTable(player2);
        table.addPlayerInTable(bot1);

        table.setCurrentPlayer();
        assertEquals(player1, table.getCurrentPlayer(), "Player1 should be the current player");

        table.passTurn();
        assertEquals(player2, table.getCurrentPlayer(), "Player2 should be the current player after Player1");

        table.passTurn();
        assertEquals(bot1, table.getCurrentPlayer(), "Bot1 should be the current player after Player2");
    }

    @Test
    public void testBlockNextPlayer() {
        table.addPlayerInTable(player1);
        table.addPlayerInTable(player2);
        table.addPlayerInTable(bot1);

        table.setCurrentPlayer();
        table.blockNext();

        assertTrue(player2.is_blocked(), "Player2 should be blocked");

        table.passTurn(); // Player1 -> Player2 (blocked)
        assertEquals(bot1, table.getCurrentPlayer(), "Bot1 should be the current player after Player2 is blocked");

        // Ensure Player2 is no longer blocked after turn pass
        assertFalse(player2.is_blocked(), "Player2 should no longer be blocked after passing turn");
    }

    @Test
    public void testPlayCardAction_DrawTwo() {
        // Assumiamo che Deck, Card, ActionCard e Caction siano correttamente implementate.
        table.addPlayerInTable(player1);
        table.addPlayerInTable(player2);
        table.setDeck();
        table.setCurrentPlayer();
        table.setCurrentCardInformation(Table.getInstance().deck.drawOut());

        ActionCard drawTwoCard = new ActionCard(Caction.DRAWTWO, Colour.BLUE);
        int initialHandSize = player2.getHand().size();
        table.playCard(drawTwoCard);

        assertEquals(initialHandSize + 2, player2.getHand().size(), "Player2 should draw 2 cards after DRAWTWO action");
    }

    // Altri test per altre carte azione come DRAWFOUR, CHANGECOLOR, etc., possono essere aggiunti qui.
}
