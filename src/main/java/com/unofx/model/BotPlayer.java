package com.unofx.model;

import com.almasb.fxgl.entity.action.Action;

import java.util.*;
import java.util.stream.Collectors;

public class BotPlayer implements Player
{
	private String username;
	private List<Card> hand;
	public boolean block;
	private Random rdn;


	public BotPlayer(String username)
	{
		this.block = false;
		this.username = username;
		this.hand = new ArrayList<>();
		this.rdn = new Random();
	}

	@Override
	public String getUsername()
	{
		return this.username;
	}


	@Override
	public void playCard(int index)
	{
		Iterator<Card> iterator = this.hand.iterator();
		boolean cardPlayed = false;

		while (iterator.hasNext()) {
			Card c = iterator.next();
			if (this.isCardValid(c)) {
				iterator.remove();
				Table.getInstance().play_card(c);
				cardPlayed = true;
				break; // Esci dal ciclo dopo aver giocato una carta
			}
		}

		// Se nessuna carta corrisponde ai criteri di giocabilità
		if (!cardPlayed) {
			Table.getInstance().play_card(null);
		}
	}

	public List<String> infoHand()
	{
		List<String> thishand = this.hand.stream().map(Card::getName).collect(Collectors.toList());
		return thishand;
	}

	public boolean isCardValid(Card cardToPlay) {
		if (cardToPlay == null || Table.getInstance().getCurrentCard() == null) {
			return false; // Gestione dei casi di input non validi
		}

		Card currentCard = Table.getInstance().getCurrentCard();
		Colour currentColor = Table.getInstance().getCurrentColor();

		if (cardToPlay instanceof NormalCard && currentCard instanceof NormalCard) {
			NormalCard normalCardToPlay = (NormalCard) cardToPlay;
			NormalCard normalCurrentCard = (NormalCard) currentCard;
			// Una carta normale può essere giocata se ha un numero diverso e un colore diverso dalla carta corrente
			return normalCardToPlay.getNumber() != normalCurrentCard.getNumber()
					&& !normalCardToPlay.getColor().getColour().equals(currentColor.getColour());
		} else if (cardToPlay instanceof ActionCard && currentCard instanceof ActionCard) {
			ActionCard actionCardToPlay = (ActionCard) cardToPlay;
			ActionCard actionCurrentCard = (ActionCard) currentCard;
			// Una carta di azione può essere giocata se ha un colore diverso dalla carta corrente
			// e un'azione diversa dalla carta corrente, oppure se entrambe le carte sono nere
			return !actionCardToPlay.getColor().getColour().equals(currentColor.getColour())
					&& !actionCardToPlay.getAction().getAction().equals(actionCurrentCard.getAction().getAction())
					|| (actionCardToPlay.getColor().getColour().equals(Colour.BLACK.getColour())
					&& actionCurrentCard.getColor().getColour().equals(Colour.BLACK.getColour()));
		} else if (cardToPlay instanceof NormalCard && currentCard instanceof ActionCard) {
			NormalCard normalCardToPlay = (NormalCard) cardToPlay;
			// Una carta normale può essere giocata se ha un colore diverso dalla carta di azione corrente
			return !normalCardToPlay.getColor().getColour().equals(currentColor.getColour());
		} else if (cardToPlay instanceof ActionCard && currentCard instanceof NormalCard) {
			ActionCard actionCardToPlay = (ActionCard) cardToPlay;
			// Una carta di azione può essere giocata se ha un colore diverso dalla carta normale corrente
			return !actionCardToPlay.getColor().getColour().equals(currentColor.getColour());
		}

		return false; // Caso non gestito
	}
	/*public Boolean isCardValid(Card cardToPlay) {
		if (cardToPlay == null || Table.getInstance().getCurrentCard() == null) {
			return false; // Gestione dei casi di input non validi
		}

		if (cardToPlay instanceof NormalCard && Table.getInstance().getCurrentCard() instanceof NormalCard) {
			return isNormalCardValidAgainstNormalCard((NormalCard) cardToPlay, (NormalCard) Table.getInstance().getCurrentCard());
		} else if (cardToPlay instanceof ActionCard && Table.getInstance().getCurrentCard() instanceof ActionCard) {
			return isActionCardValidAgainstActionCard((ActionCard) cardToPlay, (ActionCard) Table.getInstance().getCurrentCard());
		} else if (cardToPlay instanceof NormalCard && Table.getInstance().getCurrentCard() instanceof ActionCard) {
			return isNormalCardValidAgainstActionCard((NormalCard) cardToPlay, (ActionCard) Table.getInstance().getCurrentCard());
		} else if (cardToPlay instanceof ActionCard && Table.getInstance().getCurrentCard() instanceof NormalCard) {
			return isActionCardValidAgainstNormalCard((ActionCard) cardToPlay, (NormalCard) Table.getInstance().getCurrentCard());
		}

		return false; // Caso non gestito
	}

	private boolean isNormalCardValidAgainstNormalCard(NormalCard cardToPlay, NormalCard currentCard) {
		return (cardToPlay.getNumber() != currentCard.getNumber()) &&
				!cardToPlay.getColor().getColour().equals(Table.getInstance().getCurrentColor().getColour());
	}

	private boolean isActionCardValidAgainstActionCard(ActionCard cardToPlay, ActionCard currentCard) {
		return !cardToPlay.getColor().getColour().equals(Table.getInstance().getCurrentColor().getColour()) &&
				!cardToPlay.getAction().getAction().equals(currentCard.getAction().getAction()) ||
				(cardToPlay.getColor().getColour().equals(Colour.BLACK.getColour()) &&
						currentCard.getColor().getColour().equals(Colour.BLACK.getColour()));
	}

	private boolean isNormalCardValidAgainstActionCard(NormalCard cardToPlay, ActionCard currentCard) {
		return !cardToPlay.getColor().getColour().equals(Table.getInstance().getCurrentColor().getColour());
	}

	private boolean isActionCardValidAgainstNormalCard(ActionCard cardToPlay, NormalCard currentCard) {
		return !cardToPlay.getColor().getColour().equals(Table.getInstance().getCurrentColor().getColour());
	}*/


	@Override
	public void drawCard(Card e)
	{
		hand.add(e);
	}

	public List<Card> getHand()
	{
		return this.hand;
	}

	public void setBlock()
	{
		this.block = true;
	}

	public boolean is_blocked()
	{
		return this.block;
	}

	public void removeBlock(){
		this.block = false;
	}

	@Override
	public List<String> get_info_hand() {

		return this.hand.stream().map(Card::getName).collect(Collectors.toList());
	}
}
