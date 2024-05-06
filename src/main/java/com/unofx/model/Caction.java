package com.unofx.model;

public enum Caction {
	
	CHANGELAP("CHANGELAP"),
	BLOCKTURN("BLOCKTURN"),
	CHANGECOLOR("CHANGECOLOR"),
	DRAWFOUR("DRAWFOUR"),
	DRAWTWO("DRAWTWO");

	private String action;
	Caction(String action)
	{
		this.action = action;
	}

	public String getAction() {
		return action;
	}
}
