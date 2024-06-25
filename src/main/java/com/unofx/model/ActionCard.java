package com.unofx.model;

public class ActionCard implements Card
{
	private String cardName;
	private Caction action;
	private Colour c;
	private Colour choice;
	

	public ActionCard(Caction action, Colour c) {
		this.setAction(action);
		this.c = c;
		this.cardName = this.getAction().getAction().toLowerCase() + this.getColor().getColour().toLowerCase();
	}

	public Colour getChoice() {
		return choice;
	}

	public void setColour(Colour choosenColour) {
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

	public void setName(String s) {
		this.cardName = s;
	}

	private void setAction(Caction action) {
		this.action = action;
	}


}
