package com.unofx.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
		Card selectedCard = hand.get(index);
		hand.remove(index);
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
		return null;
	}
}
