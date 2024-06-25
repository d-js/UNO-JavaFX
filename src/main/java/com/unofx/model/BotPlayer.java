package com.unofx.model;

import java.util.*;
import java.util.stream.Collectors;

public class BotPlayer implements Player
{
	private String username;
	private List<Card> hand;
	public boolean block;
	private Random rdn;
	private boolean one = false;


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

	public boolean isOne() {
		return one;
	}

	public void setOneTrue() {
		this.one = true;
	}

	public void setOneFalse() {
		this.one = false;
	}
	
	public void playCard(int i)
	{
		Iterator<Card> iterator = this.hand.iterator();
		boolean cardPlayed = false;

		while (iterator.hasNext()) {
			Card c = iterator.next();
			if (this.isCardValid(c)) {
				iterator.remove();
				if(c.getColor() == Colour.BLACK)
					((ActionCard)c).setColour(Colour.fromValue(new Random().nextInt(3)));
				Table.getInstance().play_card(c);
				cardPlayed = true;
				if(this.getHand().stream().count() == 1)
					rdn = new Random();
				int condition = rdn.nextInt(5);
					if(condition % 2 == 0 || condition % 3 == 0)
						this.setOneTrue();

				break; // Esci dal ciclo dopo aver giocato una carta
			}
		}

		// Se nessuna carta corrisponde ai criteri di giocabilità
		if (!cardPlayed && i == 0) {
			this.drawCard(Table.getInstance().deck.drawOut());
			this.playCard(1);
		}

		if (!cardPlayed && i == 1) {
			Table.getInstance().play_card(null);
		}
	}

	public Boolean isCardValid(Card cardToPlay) {

		// Gestione dei casi di input non validi
		if (cardToPlay == null || Table.getInstance().getCurrentCard() == null) {
			return false;
		}

		if (cardToPlay instanceof NormalCard && Table.getInstance().getCurrentCard() instanceof NormalCard)
		{
			return isNormalCardValidAgainstNormalCard((NormalCard) cardToPlay, (NormalCard) Table.getInstance().getCurrentCard());
		}
		else if (cardToPlay instanceof ActionCard && Table.getInstance().getCurrentCard() instanceof ActionCard)
		{
			return isActionCardValidAgainstActionCard((ActionCard) cardToPlay, (ActionCard) Table.getInstance().getCurrentCard());
		}
		else if (cardToPlay instanceof NormalCard && Table.getInstance().getCurrentCard() instanceof ActionCard)
		{
			return isNormalCardValidAgainstActionCard((NormalCard) cardToPlay, (ActionCard) Table.getInstance().getCurrentCard());
		}
		else if (cardToPlay instanceof ActionCard && Table.getInstance().getCurrentCard() instanceof NormalCard)
		{
			return isActionCardValidAgainstNormalCard((ActionCard) cardToPlay, (NormalCard) Table.getInstance().getCurrentCard());
		}

		return false; // Caso non gestito
	}

	private Boolean isActionCardValidAgainstNormalCard(ActionCard cardToPlay, NormalCard currentCard)
	{
		if((cardToPlay.getColor() == Colour.BLACK) ||
				(cardToPlay.getColor() == currentCard.getColor()))
		{
			return true;
		}
		return false;
	}

	private Boolean isNormalCardValidAgainstActionCard(NormalCard cardToPlay, ActionCard currentCard)
	{
		if((cardToPlay.getColor() == currentCard.getColor()) ||
				(currentCard.getColor() == Colour.BLACK && (currentCard.getChoice() == cardToPlay.getColor())))
		{
			return true;
		}
		return false;
	}

	private Boolean isActionCardValidAgainstActionCard(ActionCard cardToPlay, ActionCard currentCard)
	{
		if((cardToPlay.getColor() == Colour.BLACK && currentCard.getColor() != Colour.BLACK) ||
				(cardToPlay.getColor() != Colour.BLACK && ((cardToPlay.getAction() == currentCard.getAction()) ||
						(cardToPlay.getColor() == currentCard.getColor())) ||
						(cardToPlay.getColor() == currentCard.getChoice())))
		{
			return true;
		}
		return false;
	}

	private Boolean isNormalCardValidAgainstNormalCard(NormalCard cardToPlay, NormalCard currentCard)
	{
		if(cardToPlay.getNumber() == currentCard.getNumber() || cardToPlay.getColor() == currentCard.getColor())
			return true;

		return false;
	}


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
