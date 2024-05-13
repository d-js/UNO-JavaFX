package com.unofx.model;

import java.net.CookieHandler;
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
	public Card playCard(int index)
	{

		Iterator<Card> iterator = this.hand.iterator();
		while (iterator.hasNext()) {
			Card c = iterator.next();
			if (this.isCardValid(c)) {
				System.out.println(Table.getInstance().getCurrentColor() + Table.getInstance().getCurrentCard().getName());
				iterator.remove();
				return c;
			}

			if(this.hand.indexOf(c) == this.hand.size()-1)
				this.drawCard(Table.getInstance().deck.drawOut());
		}
		System.out.println("Nessuna carta corrisponde ai criteri di giocabilita'");
		return null;
    }

	public List<String> infoHand()
	{
		List<String> thishand = this.hand.stream().map(Card::getName).collect(Collectors.toList());
		return thishand;
	}


	public Boolean isCardValid(Card e)
	{
		/* controllo se la carta può essere giocata */
		if (e instanceof NormalCard && Table.getInstance().getCurrentCard() instanceof NormalCard) {
			NormalCard normal_played = (NormalCard) e;
			NormalCard normal_current = (NormalCard) Table.getInstance().getCurrentCard();
			// Controllo se la carta può essere giocata
			if (normal_played.getNumber() != normal_current.getNumber() && normal_played.getColor() != Table.getInstance().getCurrentColor())
				return false;
		}

		if(e instanceof ActionCard && Table.getInstance().getCurrentCard() instanceof ActionCard) {
			ActionCard action_played = (ActionCard)Table.getInstance().getCurrentCard();
			ActionCard action_current = (ActionCard)Table.getInstance().getCurrentCard();
			// Controllo se la carta può essere giocata
			if(action_played.getColor() != Table.getInstance().getCurrentColor() &&
					action_played.getAction() != action_current.getAction())
				return false;
			else if(action_played.getColor() == Colour.BLACK &&
					action_current.getColor() == Colour.BLACK)
				return false;
		}
		return true;
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
