package com.unofx.model.enums;

public enum Caction {
	
	CHANGELAP("CHANGELAP"),
	BLOCKTURN("BLOCKTURN"),
	CHANGECOLOR("CHANGECOLOR"),
	DRAWFOUR("CHANGECOLORFOUR"),
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
