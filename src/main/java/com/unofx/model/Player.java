package com.unofx.model;

import java.util.List;
import java.util.Map;

public interface Player {
	
    public String getUsername();
    
    public void playCard(int index);
    
    public void drawCard(Card e);

    public List<Card> getHand();
    
    public void setBlock();

	public boolean is_blocked();

    public void removeBlock();

    public List<String> get_info_hand();

}
