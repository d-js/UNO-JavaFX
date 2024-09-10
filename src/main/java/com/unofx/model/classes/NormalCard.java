package com.unofx.model.classes;

import com.unofx.model.enums.Colour;
import com.unofx.model.enums.Number;
import com.unofx.model.interfaces.Card;

public class NormalCard implements Card {

	private String cardName;
	private com.unofx.model.enums.Number num;
	private Colour c;

	
	public NormalCard(com.unofx.model.enums.Number num, Colour c) {
		setNumber(num);
		setColor(c);
		this.cardName = num.getNumberString().toLowerCase() + this.getColor().getColour().toLowerCase();
	}

	@Override
	public String getName() {
		return this.cardName;
	}

	public com.unofx.model.enums.Number getNumber() {
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
