package com.unofx.model.classes;

import com.unofx.model.enums.Colour;
import com.unofx.model.interfaces.Card;
import com.unofx.model.interfaces.Player;

import java.util.*;


public class UserPlayer implements Player
{
	private String username;
	private final List<Card> hand;
	public boolean block;
	private boolean one = false;
	
	public UserPlayer(String username)
	{
		this.block = false;
		this.username = username;
		this.hand = new LinkedList<>();
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
		List<Card> l = this.hand.stream().filter(e -> path.contains(capitalize(e.getName()))).toList();

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
		List<Card> l = this.hand.stream().filter(e -> path.toLowerCase().contains(e.getName())).toList();

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
		return (firstLetter) + restOfString;
	}

}
