package com.unofx.model.interfaces;

import java.util.List;

public interface Player {
	
    public String getUsername();

    public void drawCard(Card e);

    public List<Card> getHand();
    
    public void setBlock();

	public boolean is_blocked();

    public void removeBlock();

    public List<String> get_info_hand();

    public void setOneTrue();

    public void setOneFalse();

    public boolean isOne();
}
