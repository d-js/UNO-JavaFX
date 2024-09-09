package com.unofx.model;

import static org.junit.jupiter.api.Assertions.*;

import com.unofx.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class UserPlayerTest {

    private UserPlayer userPlayer;
    private NormalCard normalCard;
    private ActionCard actionCard;
    private Table table = Table.getInstance();

    @BeforeEach
    public void setUp() {
        Table.getInstance().reset();
        userPlayer = new UserPlayer("TestUser");

        normalCard = new NormalCard(Number.FIVE, Colour.RED);
        actionCard = new ActionCard(Caction.DRAWTWO, Colour.RED);

        table = Table.getInstance();
        Table.getInstance().addPlayerInTable(userPlayer);
        Table.getInstance().setCurrentPlayer();
        Table.getInstance().setCurrentCardInformation(normalCard);
        Table.getInstance().setDeck();

    }

    @Test
    public void testGetUsername() {
        assertEquals("TestUser", userPlayer.getUsername());
    }

    @Test
    public void testSetUsername() {
        userPlayer.setUsername("NewUser");
        assertEquals("NewUser", userPlayer.getUsername());
    }

    @Test
    public void testDrawCard() {

        userPlayer.drawCard(this.normalCard);
        assertTrue(userPlayer.getHand().contains(normalCard));
    }

    @Test
    public void testPlayCard() {

        userPlayer.drawCard(normalCard);
        assertTrue(userPlayer.getHand().contains(normalCard));


        table.playCard(new NormalCard(Number.THREE, Colour.RED));


        userPlayer.playCard("Fivered.png", null);
        assertFalse(userPlayer.getHand().contains(normalCard));
    }

    @Test
    public void testSetBlock() {
        userPlayer.setBlock();
        assertTrue(userPlayer.is_blocked());
    }

    @Test
    public void testRemoveBlock() {
        userPlayer.setBlock();
        userPlayer.removeBlock();
        assertFalse(userPlayer.is_blocked());
    }

    @Test
    public void testGetInfoHand() {
        userPlayer.drawCard(normalCard);
        userPlayer.drawCard(actionCard);

        List<String> handInfo = userPlayer.get_info_hand();
        assertEquals(2, handInfo.size());
        assertEquals("fivered", handInfo.get(0));
        assertEquals("drawtwored", handInfo.get(1));
    }

    @Test
    public void testIsOneTrue() {
        userPlayer.setOneTrue();
        assertTrue(userPlayer.isOne());
    }

    @Test
    public void testIsOneFalse() {
        userPlayer.setOneFalse();
        assertFalse(userPlayer.isOne());
    }

    @Test
    public void testIsPlayable() {

        userPlayer.drawCard(normalCard);
        table.playCard(new NormalCard(Number.SEVEN, Colour.RED));


        boolean result = userPlayer.isPlayable("Fivered.png");
        assertTrue(result);
    }

    @Test
    public void testIsCardValid_NormalCardVsNormalCard() {

        NormalCard currentCard = new NormalCard(Number.SEVEN, Colour.RED);
        table.playCard(currentCard);


        assertTrue(userPlayer.isCardValid(normalCard));
    }

    @Test
    public void testIsCardValid_ActionCardVsNormalCard() {

        NormalCard currentCard = new NormalCard(Number.THREE, Colour.RED);
        table.playCard(currentCard);


        assertTrue(userPlayer.isCardValid(actionCard));
    }

    @Test
    public void testIsCardValid_ActionCardVsActionCard() {

        ActionCard currentActionCard = new ActionCard(Caction.DRAWTWO, Colour.RED);
        table.playCard(currentActionCard);


        assertTrue(userPlayer.isCardValid(actionCard));
    }

    @Test
    public void testCapitalize() {
        assertEquals("Hello", UserPlayer.capitalize("hello"));
        assertEquals("World", UserPlayer.capitalize("world"));
        assertEquals("", UserPlayer.capitalize(""));
        assertNull(UserPlayer.capitalize(null));
    }
}
