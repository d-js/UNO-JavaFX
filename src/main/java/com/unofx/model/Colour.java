package com.unofx.model;

public enum Colour {

	RED("RED"),
	YELLOW("YELLOW"),
	GREEN("GREEN"),
	BLUE("BLUE"),
	BLACK("BLACK");

	private String colour;

	Colour(String colour) {
		this.colour = colour;
	}

	public String getColour() {
		return colour;
	}

	public static Colour fromValue(int value) {
		for (Colour colour : Colour.values()) {
			if (colour.ordinal() == value) {
				return colour;
			}
		}
		return null;
	}
}
