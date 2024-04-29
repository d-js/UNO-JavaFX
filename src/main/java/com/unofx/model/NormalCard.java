package com.unofx.model;

public class NormalCard implements Card {

	private String cardName;
	private int num;
	private Colour c;
	
	
	public NormalCard(Number num, Colour c) {
		setNumber(num.ordinal());
		setColor(c);
		this.cardName = num.getNumberString().toLowerCase() + this.getColor().getColour().toLowerCase();
	}

	@Override
	public String getName() {
		return this.cardName;
	}

	public void setName(String s) {
		this.cardName = s;
	}
	public int getNumber() {
		return num;
	}
	private void setNumber(int num) {
		num = num;
	}
	public Colour getColor() {
		return c;
	}
	private void setColor(Colour c) {
		this.c = c;
	}

}
