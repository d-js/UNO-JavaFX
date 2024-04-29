package com.unofx.model;

public interface Card
{
	public Colour getColor();

	public String getName();

	default Caction getAction()
	{
		Caction c = Caction.DROWTWO;
		return c;
	}


	
}
