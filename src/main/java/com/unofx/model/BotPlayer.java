package com.unofx.model;

import java.net.CookieHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class BotPlayer implements Player
{
	private String username;
	private List<Card> hand;
	public boolean block;
	
	
	public BotPlayer(String username)
	{
		this.block = false;
		this.username = username;
		this.hand = new ArrayList<>();
	}

	@Override
	public String getUsername() 
	{
		return this.username;
	}

	@Override
	public Card playCard(int index)
	{
		Random rdn = new Random();
		Card selectedCard = hand.get(rdn.nextInt(hand.size()));
		System.out.println("La carta giocata dal bot e': " + selectedCard.getName());
		if(selectedCard.getAction() == Caction.CHANGECOLOR ||
			selectedCard.getAction() == Caction.DRAWFOUR)
		{
			Colour randomColour = Colour.values()[rdn.nextInt(Colour.values().length)];
			((ActionCard)selectedCard).setChoosenColour(randomColour);
		}
		hand.remove(selectedCard);
		return selectedCard;
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

		return this.hand.stream().map(e -> e.getName()).collect(Collectors.toList());
	}
}
