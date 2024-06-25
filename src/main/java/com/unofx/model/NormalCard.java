package com.unofx.model;

import javafx.beans.value.ObservableNumberValue;

public class NormalCard implements Card {

	private String cardName;
	private Number num;
	private Colour c;

	
	public NormalCard(Number num, Colour c) {
		setNumber(num);
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
	public Number getNumber() {
		return num;
	}
	private void setNumber(Number num) {
		this.num = num;
	}
	public Colour getColor() {
		return c;
	}
	private void setColor(Colour c) {
		this.c = c;
	}

}
