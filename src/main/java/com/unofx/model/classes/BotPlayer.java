package com.unofx.model.classes;

import com.unofx.model.enums.Colour;
import com.unofx.model.interfaces.Card;
import com.unofx.model.interfaces.Player;

import java.util.*;
import java.util.stream.Collectors;

public class  BotPlayer implements Player
{
	private final String username;
	private final List<Card> hand;
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
					((ActionCard)c).setChoice(Colour.fromValue(new Random().nextInt(3)));
				TableImpl.getInstance().playCard(c);
				cardPlayed = true;
				if(this.getHand().size() == 1)
					rdn = new Random();
				int condition = rdn.nextInt(5);
					if(condition % 2 == 0 || condition % 3 == 0)
						this.setOneTrue();

				break; // Esci dal ciclo dopo aver giocato una carta
			}
		}

		// Se nessuna carta corrisponde ai criteri di giocabilità
		if (!cardPlayed && i == 0) {
			this.drawCard(TableImpl.getInstance().deck.drawOut());
			this.playCard(1);
		}else if (!cardPlayed && i == 1) {
			TableImpl.getInstance().playCard(null);
		}
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

	public boolean isBlocked()
	{
		return this.block;
	}

	public void removeBlock(){
		this.block = false;
	}

	@Override
	public List<String> getInfoHand() {

		return this.hand.stream().map(Card::getName).collect(Collectors.toList());
	}
}
