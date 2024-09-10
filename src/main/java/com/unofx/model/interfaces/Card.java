package com.unofx.model.interfaces;

import com.unofx.model.enums.Caction;
import com.unofx.model.enums.Colour;

public interface Card
{
	Colour getColor();

	String getName();

	default Caction getAction()
	{
        return Caction.DRAWTWO;
	}


	
}
