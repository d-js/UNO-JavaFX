package com.unofx.model;

import com.almasb.fxgl.entity.action.Action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserPlayer implements Player 
{
	private String username;
	private List<Card> hand;
	public boolean block;
	
	
	public UserPlayer(String username)
	{
		this.block = false;
		this.username = username;
		this.hand = new ArrayList<>(); //meglio fare una linkedlist, in questo modo quando si elimina una carte le carte si riassegnano automaticamente l'indice corretto
	}

	@Override
	public String getUsername() 
	{
		return this.username;
	}
	
	@Override
	//Per rimuovere la carta giocata forse è meglio farsi ritornare l'indice dal pulsante e in base a quello eliminare e giocare la carta
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


	public List<String> get_info_hand()
	{
		List<String> hand_info = new ArrayList<>();
		this.hand.forEach(e -> hand_info.add(e.getName()));
		return hand_info;
	}

}
