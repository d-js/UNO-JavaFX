package com.unofx.model.classes;

import com.unofx.model.enums.Colour;
import com.unofx.model.interfaces.Card;
import com.unofx.model.interfaces.Player;

import java.util.*;
import java.util.stream.Collectors;

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

	public void setUsername(String username){
		this.username = username;
	}

	
	public void playCard(String path, Colour choice)
	{
		List<Card> l = this.hand.stream().filter(e -> path.contains(capitalize(e.getName()))).collect(Collectors.toList());

		Card e = l.get(0);
		System.out.println(e);


		this.hand.remove(e);

		if(choice != null && e instanceof ActionCard)
			((ActionCard) e).setChoice(choice);

		TableImpl.getInstance().playCard(e);
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
		List<Card> l = this.hand.stream().filter(e -> path.toLowerCase().contains(e.getName())).collect(Collectors.toList());

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
