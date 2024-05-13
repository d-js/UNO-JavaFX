package com.unofx.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class Deck 
{
	//Valutare se sono utili due mazzi creati nella classe mazzo
	private final List<Card> coverDeck;
	private final List<Card> uncoverDeck;


	//Creazione di un mazzo da gioco
	public Deck()
	{
		this.uncoverDeck = new ArrayList<>();
		this.coverDeck = new ArrayList<>();
		this.setGameDeck();
	}


	//generazione mazzo di carte (DA RIVEDERE / OTTIMIZZARE)
	private void setGameDeck()
	{
		for(Colour c : Colour.values())
		{
		    if(c != Colour.BLACK)
		    {
				coverDeck.add(new NormalCard(Number.ZERO,c));

				coverDeck.add(new ActionCard(Caction.BLOCKTURN,c));
				coverDeck.add(new ActionCard(Caction.BLOCKTURN,c));
				coverDeck.add(new ActionCard(Caction.CHANGELAP,c));
				coverDeck.add(new ActionCard(Caction.CHANGELAP,c));
				coverDeck.add(new ActionCard(Caction.DRAWTWO,c));
				coverDeck.add(new ActionCard(Caction.DRAWTWO,c));

    		    for(Number e : Number.values())
    		    {
					if(e != Number.ZERO) {
						coverDeck.add(new NormalCard(e, c));
						coverDeck.add(new NormalCard(e, c));
					}
    		    }
		    }
		    else
		    {
				coverDeck.add(new ActionCard(Caction.CHANGECOLOR,c));
				coverDeck.add(new ActionCard(Caction.CHANGECOLOR,c));
				coverDeck.add(new ActionCard(Caction.DRAWFOUR,c));
				coverDeck.add(new ActionCard(Caction.DRAWFOUR,c));
		    }

			this.shuffle();
		}
	}
	
	//Pesca la prima carta del mazzo
	public Card drawOut()
	{
		if(this.coverDeck.isEmpty())
			this.drawDown();

		if(this.coverDeck.stream().findFirst().isPresent()) {
			Card e = this.coverDeck.stream().findFirst().get();
			coverDeck.remove(e);
			return (e);
		}
		else
		{
			return null;
		}

	}

	//Ripristina il mazzo mescolalo e pesca una carta
	public void drawDown()
	{
		this.coverDeck.addAll(this.uncoverDeck);
		this.uncoverDeck.clear();
		this.shuffle();
	}
	
	//Inserisci una carta nel mazzo
	public void playCard(Card e)
	{
		this.uncoverDeck.add(e);
	}
	
	private void shuffle()
	{
		Collections.shuffle(coverDeck);
	}

	public Card set_initial_card()
	{
		Card e = this.drawOut();
		this.playCard(e);
		return e;
	}

	public Card getLastUncover()
	{
		return this.uncoverDeck.get(this.uncoverDeck.size() - 1);
	}

	public void delete()
	{
		this.coverDeck.clear();
		this.uncoverDeck.clear();
	}

	public List<Card> getCoverDeck() {
		return this.coverDeck;
	}
}
