package com.unofx.model.classes;

import com.unofx.model.enums.Caction;
import com.unofx.model.enums.Colour;
import com.unofx.model.enums.Number;
import com.unofx.model.interfaces.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Deck 
{
	//Valutare se sono utili due mazzi creati nella classe mazzo
	private final List<Card> coverDeck;
	public final List<Card> uncoverDeck;


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
				coverDeck.add(new NormalCard(com.unofx.model.enums.Number.ZERO,c));

				coverDeck.add(new ActionCard(Caction.BLOCKTURN,c));
				coverDeck.add(new ActionCard(Caction.BLOCKTURN,c));
				coverDeck.add(new ActionCard(Caction.CHANGELAP,c));
				coverDeck.add(new ActionCard(Caction.CHANGELAP,c));
				coverDeck.add(new ActionCard(Caction.DRAWTWO,c));
				coverDeck.add(new ActionCard(Caction.DRAWTWO,c));

    		    for(com.unofx.model.enums.Number e : com.unofx.model.enums.Number.values())
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
	public synchronized Card  drawOut()
	{
		if(this.coverDeck.isEmpty())
			this.drawDown();

		if(this.coverDeck.stream().findFirst().isPresent())
		{
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
	
	public void shuffle()
	{
		Collections.shuffle(coverDeck);
	}

	public Card setInitialCard()
	{
		Card e = this.coverDeck.stream().filter(c -> c instanceof NormalCard).findFirst().orElseGet(() -> this.setInitialCard());

		this.coverDeck.remove(e);
		this.uncoverDeck.add(e);

		return e;
	}

	public void delete()
	{
		this.coverDeck.clear();
		this.uncoverDeck.clear();
	}


	public Card getLastUncover()
	{
		return this.uncoverDeck.get(this.uncoverDeck.size() - 1);
	}

	public List<Card> getCoverDeck()
	{
		return this.coverDeck;
	}
}
