package com.unofx.model;

import org.controlsfx.control.action.Action;

import java.util.*;

public class Table
{


	private static Table INSTANCE = null;
	private static int CurrentIndexPlayer = 0;
	public Deck deck;
	private Card currentCard;
	private Colour currentColor = Colour.BLUE;
	private Player currentPlayer;
	private List<Player> sitDownPlayer = new LinkedList<>();



	// Ritorno l'istanza della classe Table
	public static synchronized Table getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new Table();
		}

		return INSTANCE;
	}


	public void addPlayerInTable(Player e)
	{
		this.sitDownPlayer.add(e);
	}


	public UserPlayer getNextUserPlayer() {
		UserPlayer nextuser = new UserPlayer("error");
		for(Player e : this.sitDownPlayer)
		{
			if(e instanceof UserPlayer){
				nextuser = (UserPlayer) e;

			}
		}
		return nextuser;
	}


	public UserPlayer getUserPlayerByUsername(String Username)
	{
		UserPlayer selected = null;
		for(Player e : this.sitDownPlayer)
		{
			if(e instanceof UserPlayer && e.getUsername().equalsIgnoreCase(Username))
			{
				selected = (UserPlayer) e;
				break;
			}
		}
		if(selected == null)
			System.out.println("Nessun giocatore trovato con questo nome");
		return selected;
	}


	public List<UserPlayer> getAllUser()
	{
		List<UserPlayer> allUser = new LinkedList<>();
		this.sitDownPlayer.stream().forEach((e) ->
		{
			if(e instanceof UserPlayer){
				allUser.add((UserPlayer) e);
			}
		});

		return allUser;
	}


	public Player getCurrentPlayer()
	{
		return this.currentPlayer;
	}


	/* Metodo per costruire la lista di giocatori della partita*/
	public void setPlayerList(int numberOfPlayer)
	{
		while(numberOfPlayer > 0)
		{
			this.sitDownPlayer.add(new BotPlayer("bot" + numberOfPlayer));
			numberOfPlayer--;
		}
	}


	/* Distribuisci le carte per iniziare la partita*/
	public void giveStartCard()
	{
		for(Player e : this.sitDownPlayer)
		{
				e.drawCard(this.deck.drawOut());
				e.drawCard(this.deck.drawOut());
				e.drawCard(this.deck.drawOut());
				e.drawCard(this.deck.drawOut());
				e.drawCard(this.deck.drawOut());
				e.drawCard(this.deck.drawOut());
				e.drawCard(this.deck.drawOut());
				e.drawCard(this.deck.drawOut());
		}
	}

	public Card getCurrentCard() {
		return currentCard;
	}


	/* Costruisce i mazzi */
	public void setDeck()
	{
		this.deck = new Deck();
	}


	public void setCurrentPlayer() {
		this.currentPlayer = this.sitDownPlayer.get(0);
	}


	public void setCurrentCardInformation(Card currentCard)
	{
		this.currentCard = currentCard;
		this.currentColor = currentCard.getColor();
	}


	/* Metodo da chiamare tutte le volte che un player ha giocato una carta per capire se ha vinto */
	public boolean controlWinner()
	{
		for(Player cPlayer : this.sitDownPlayer)
		{
			if(cPlayer.getHand().isEmpty())
			{
				return true;
			}
		}
		return false;
	}


	public synchronized void playCard(Card e)
	{
		System.out.println(this.currentPlayer.getUsername().toUpperCase() + " STA GIOCANDO...\n");
		System.out.println("La carta sul tavolo e' " + this.currentCard.getName());

		if (e == null) {
			System.out.println(this.currentPlayer.getUsername() + " non ha una carta giocabile.'");
		} else {
			System.out.println(this.currentPlayer.getUsername() + " gioca " + e.getName());
		}

		System.out.println("La mano rimanente di " + this.currentPlayer.getUsername() + this.currentPlayer.get_info_hand());

		// Controllo se il player è bloccato
		if (this.currentPlayer.is_blocked()) {
			this.currentPlayer.removeBlock();
		}
		else
		{
			// TODO i blocchi sono permanenti e l'utente non pesca le carte
			// Gestione delle carte di azione

			if (e instanceof ActionCard) {
				switch (e.getAction()) {
					case DRAWTWO:
						nextPlayer().drawCard(deck.drawOut());
						nextPlayer().drawCard(deck.drawOut());
						break;
					case DRAWFOUR:
						this.currentColor = ((ActionCard) e).getChoice();
						nextPlayer().drawCard(deck.drawOut());
						nextPlayer().drawCard(deck.drawOut());
						nextPlayer().drawCard(deck.drawOut());
						nextPlayer().drawCard(deck.drawOut());

						break;
					case BLOCKTURN:
						this.blockNext();
						break;
					case CHANGELAP:
						changeLap();
						break;
					case CHANGECOLOR:
						this.currentColor = ((ActionCard) e).getChoice(); // Implementa un metodo per scegliere il colore
						break;

				}
			}



			if(e != null)
			{
				deck.playCard(e);
				if(this.currentColor != null)
				{
					if (e.getAction().equals(Caction.CHANGECOLOR)) {
                        ActionCard actionCard = new ActionCard(Caction.CHANGECOLOR, this.currentColor);
						setCurrentCardInformation(actionCard);
                    }
					else if(e.getAction().equals(Caction.DRAWFOUR))
					{
						ActionCard actionCard = new ActionCard(Caction.DRAWFOUR, this.currentColor);
						setCurrentCardInformation(actionCard);
					}
					else
					{
						setCurrentCardInformation(e);
					}
				}
			}
		}

		this.controlIfOneCard();
		this.getCurrentPlayer().setOneFalse();

		passTurn();
		System.out.println("\n\n");
	}


	// Return the instance of the next player in turns order`
	private Player nextPlayer()
	{
		return sitDownPlayer.get(this.controlIndex(CurrentIndexPlayer + 1));
	}


	public boolean controlIfOneCard()
	{
		if(this.getCurrentPlayer().getHand().stream().count() == 1 &&
			!this.getCurrentPlayer().isOne())
		{
			this.getCurrentPlayer().drawCard(this.deck.drawOut());
			this.getCurrentPlayer().drawCard(this.deck.drawOut());
		}
		return false;
	}


	/* Cambia giro, inverte l'ordine dei giocatori nella lista*/
	public void changeLap()
	{
		Player e = this.sitDownPlayer.get(Table.CurrentIndexPlayer);
		Collections.reverse(this.sitDownPlayer);
		Table.CurrentIndexPlayer = this.sitDownPlayer.indexOf(e);
	}

	/* Blocca il turno del giocatore successivo*/
	public void blockNext()
	{
		nextPlayer().setBlock();
	}


	/* Passa al turno al player successivo*/
	public void passTurn()
	{
		List<Player> blocked_players = new ArrayList<>();
		if(!this.sitDownPlayer.isEmpty())
		{
			int nextPlayerIndex = (Table.CurrentIndexPlayer + 1) % this.sitDownPlayer.size(); // Calcola l'indice del prossimo giocatore

			while (this.sitDownPlayer.get(nextPlayerIndex).is_blocked()) {
				blocked_players.add(this.sitDownPlayer.get(nextPlayerIndex));
				nextPlayerIndex = (nextPlayerIndex + 1) % this.sitDownPlayer.size(); // Avanza fino a trovare un giocatore non bloccato
			}

			// libero i giocatori bloccati che hanno passato il blocco
			for(Player e : blocked_players)
			{
				e.removeBlock();
			}

			Table.CurrentIndexPlayer = nextPlayerIndex; // Imposta l'indice del nuovo giocatore corrente
			this.currentPlayer = this.sitDownPlayer.get(Table.CurrentIndexPlayer); // Imposta il nuovo giocatore corrente
		}
		else
		{
			System.out.println("La lista di giocatori e' vuota, non si sa come");
		}
	}


	/* Verifico che l'indice passato per parametro rimanga entro il max e il min */
	public int controlIndex(int index)
	{
		int verified_index;
		if(index <= (this.sitDownPlayer.size() - 1))
		{
			verified_index = index;
		}
		else
		{
			verified_index = 0;
		}
		
		return verified_index;
	}


	public void eraseAll()
	{
		this.deck.delete();
		this.sitDownPlayer.clear();

	}

}

