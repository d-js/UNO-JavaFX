package com.unofx.model;

import com.almasb.fxgl.entity.action.Action;

import java.util.*;

public class UserPlayer implements Player 
{
	private String username;
	private List<Card> hand;
	public boolean block;
	private boolean one = false;
	
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
	
	// TODO ricavare la carta in base al path dell'immagine passata
	public void playCard(String path)
	{
		List<Card> l = this.hand.stream().filter(e -> path.contains(capitalize(e.getName()))).toList();
		Card e = l.get(0);
		this.hand.remove(e);
		Table.getInstance().play_card(e);
	}

	public boolean isOne() {
		return one;
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

	public void setOneTrue() {
		this.one = true;
	}

	public void setOneFalse() {
		this.one = false;
	}

	public boolean isPlayable(String path)
	{
		List<Card> l = this.hand.stream().filter(e -> path.contains(capitalize(e.getName()))).toList();

		Card e = l.get(0);
		return this.isCardValid(e);
	}

	public static String capitalize(String str) {
		if (str == null || str.isEmpty()) {
			return str;
		}

		// Get the first character of the input string and convert it to uppercase
		char firstLetter = Character.toUpperCase(str.charAt(0));

		// Get the rest of the string
		String restOfString = str.substring(1);

		// Return the new string with the first character capitalized
		return String.valueOf(firstLetter) + restOfString;
	}


	public Boolean isCardValid(Card cardToPlay)
	{
		// TODO errore nella gestione delle carte action non black su action non black
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

		return false;
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

}
