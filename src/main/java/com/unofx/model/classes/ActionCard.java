package com.unofx.model.classes;

import com.unofx.model.enums.Caction;
import com.unofx.model.enums.Colour;
import com.unofx.model.interfaces.Card;

public class ActionCard implements Card
{
	private String cardName;
	private Caction action;
	private Colour c;
	private Colour choice;
	

	public ActionCard(Caction action, Colour colour) {
		this.setAction(action);
		this.c = colour;
		if(colour == Colour.BLACK)
			this.cardName = this.getAction().getAction().toLowerCase();
		else
			this.cardName = this.getAction().getAction().toLowerCase() + this.getColor().getColour().toLowerCase();


	}

	public ActionCard(Caction action, Colour colour, Colour choice) {
		this.setAction(action);
		this.c = colour;
		this.choice = choice;
		this.cardName = this.getAction().getAction().toLowerCase() + this.getChoice().getColour().toLowerCase();
	}

	public Colour getChoice() {
		return choice;
	}

	public void setChoice(Colour choosenColour) {
		this.choice = choosenColour;
	}

	public Caction getAction() {
		return action;
	}

	public Colour getColor() {
		return c;
	}

	@Override
	public String getName() {
		return this.cardName;
	}

	private void setAction(Caction action) {
		this.action = action;
	}


}
