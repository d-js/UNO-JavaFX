package com.unofx.model;

import com.almasb.fxgl.entity.action.Action;

import java.util.*;

public class UserPlayer implements Player 
{
	private String username;
	private List<Card> hand;
	public boolean block;
	
	
	public UserPlayer(String username)
	{
		this.block = false;
		this.username = username;
		this.hand = new LinkedList<>(); //meglio fare una linkedlist, in questo modo quando si elimina una carte le carte si riassegnano automaticamente l'indice corretto
	}

	@Override
	public String getUsername() 
	{
		return this.username;
	}
	
	@Override
	//Per rimuovere la carta giocata forse è meglio farsi ritornare l'indice dal pulsante e in base a quello eliminare e giocare la carta
	public void playCard(int index)
	{
		Card selectedCard = hand.get(index);
		hand.remove(index);
		Table.getInstance().play_card(selectedCard);
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

	// TODO idea se serve fare un metodo per ricavare la carta vedendo se il getname e' contenuto nel filepath passato dalla view
	public Card getCardFromSpec(int index)
	{
		Card e = this.hand.get(index);
		this.hand.remove(e);
		return e;
	}


	public List<String> get_info_hand()
	{
		List<String> hand_info = new ArrayList<>();
		this.hand.forEach(e -> hand_info.add(e.getName()));
		return hand_info;
	}

}
