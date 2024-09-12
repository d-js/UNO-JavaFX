package com.unofx.model.interfaces;

import com.unofx.model.classes.ActionCard;
import com.unofx.model.classes.NormalCard;
import com.unofx.model.classes.TableImpl;
import com.unofx.model.enums.Colour;

import java.util.List;

public interface Player {
	
    String getUsername();

    void drawCard(Card e);

    List<Card> getHand();
    
    void setBlock();

	boolean isBlocked();

    void removeBlock();

    List<String> getInfoHand();

    void setOneTrue();

    void setOneFalse();

    boolean isOne();

    default boolean isCardValid(Card cardToPlay)
    {
        // Gestione dei casi di input non validi
        if (cardToPlay == null || TableImpl.getInstance().getCurrentCard() == null) {
            return false;
        }

        if (cardToPlay instanceof NormalCard && TableImpl.getInstance().getCurrentCard() instanceof NormalCard)
        {
            return isNormalCardValidAgainstNormalCard((NormalCard) cardToPlay, (NormalCard) TableImpl.getInstance().getCurrentCard());
        }
        else if (cardToPlay instanceof ActionCard && TableImpl.getInstance().getCurrentCard() instanceof ActionCard)
        {
            return isActionCardValidAgainstActionCard((ActionCard) cardToPlay, (ActionCard) TableImpl.getInstance().getCurrentCard());
        }
        else if (cardToPlay instanceof NormalCard && TableImpl.getInstance().getCurrentCard() instanceof ActionCard)
        {
            return isNormalCardValidAgainstActionCard((NormalCard) cardToPlay, (ActionCard) TableImpl.getInstance().getCurrentCard());
        }
        else if (cardToPlay instanceof ActionCard && TableImpl.getInstance().getCurrentCard() instanceof NormalCard)
        {
            return isActionCardValidAgainstNormalCard((ActionCard) cardToPlay, (NormalCard) TableImpl.getInstance().getCurrentCard());
        }

        return false; // Caso non gestito
    }

    default Boolean isActionCardValidAgainstNormalCard(ActionCard cardToPlay, NormalCard currentCard)
    {
        return (cardToPlay.getColor() == Colour.BLACK) ||
                (cardToPlay.getColor() == currentCard.getColor());
    }

    default Boolean isNormalCardValidAgainstActionCard(NormalCard cardToPlay, ActionCard currentCard)
    {
        return (cardToPlay.getColor() == currentCard.getColor()) ||
                (currentCard.getColor() == Colour.BLACK && (currentCard.getChoice() == cardToPlay.getColor()));
    }

    default Boolean isActionCardValidAgainstActionCard(ActionCard cardToPlay, ActionCard currentCard)
    {
        return (cardToPlay.getColor() == Colour.BLACK && currentCard.getColor() != Colour.BLACK) ||
                (cardToPlay.getColor() != Colour.BLACK && ((cardToPlay.getAction() == currentCard.getAction()) ||
                        (cardToPlay.getColor() == currentCard.getColor())) ||
                        (cardToPlay.getColor() == currentCard.getChoice()));
    }

    default Boolean isNormalCardValidAgainstNormalCard(NormalCard cardToPlay, NormalCard currentCard)
    {
        return cardToPlay.getNumber() == currentCard.getNumber() || cardToPlay.getColor() == currentCard.getColor();
    }


}
