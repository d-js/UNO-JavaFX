package com.unofx.model;

public enum Caction {
	
	CHANGELAP("CHANGELAP"),
	BLOCKTURN("BLOCKTURN"),
	CHANGECOLOR("CHANGECOLOR"),
	DRAWFOUR("DRAWFOUR"),
	DROWTWO("DRAWTWO");

	private String action;
	Caction(String action)
	{
		this.action = action;
	}

	public String getAction() {
		return action;
	}
}
