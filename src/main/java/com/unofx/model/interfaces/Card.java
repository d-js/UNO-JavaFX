package com.unofx.model.interfaces;

import com.unofx.model.enums.Caction;
import com.unofx.model.enums.Colour;

public interface Card
{
	public Colour getColor();

	public String getName();

	default Caction getAction()
	{
		Caction c = Caction.DRAWTWO;
		return c;
	}


	
}
