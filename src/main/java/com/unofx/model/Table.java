package com.unofx.model;

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
	private UserPlayer User;
	
	private Random rdn = new Random();

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
			if(e.getUsername().equals(Username))
			{
				selected = (UserPlayer) e;
			}
		}
		if(selected == null)
			System.out.println("non esiste nessun giocatore con questo nome");
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


	public Player getCurrentPlayer() {
		return this.currentPlayer;
	}

	public List<Player> getSitDownPlayer() {
		return sitDownPlayer;
	}



	/* Metodo per costruire la lista di giocatori della partita*/
	public void set_player_list(int numberOfPlayer)
	{
		while(numberOfPlayer > 0)
		{
			this.sitDownPlayer.add(new BotPlayer("bot" + numberOfPlayer));
			numberOfPlayer--;
		}
	}
	
	/* Distribuisci le carte per iniziare la partita*/
	public void give_start_card()
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
	public void set_deck()
	{
		this.deck = new Deck();
	}

	public void setCurrentPlayer() {
		this.currentPlayer = this.sitDownPlayer.get(0);
	}

	public void setCurrentCardInformation(Card currentCard) {
		this.currentCard = currentCard;
		this.currentColor = currentCard.getColor();
	}

	/* Metodo da chiamare tutte le volte che un player ha giocato una carta per capire se ha vinto */
	public boolean control_winner()
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

	/* Annuncia il vincitore*/
	private void announce_winner(Player winner) 
	{
		this.sitDownPlayer.removeAll(this.sitDownPlayer);
		this.deck.delete();
		this.currentCard = null;
		this.currentPlayer = null;
		System.out.println(winner + " vince.");
	}


	public synchronized void play_card(Card e)
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
				ActionCard actionCard = (ActionCard) e;
				switch (actionCard.getAction()) {
					case DRAWTWO:
						nextPlayer().drawCard(deck.drawOut());
						nextPlayer().drawCard(deck.drawOut());
						break;
					case DRAWFOUR:
						currentColor = actionCard.getChoice();
						nextPlayer().drawCard(deck.drawOut());
						nextPlayer().drawCard(deck.drawOut());
						nextPlayer().drawCard(deck.drawOut());
						nextPlayer().drawCard(deck.drawOut());
						break;
					case BLOCKTURN:
						nextPlayer().setBlock();
						break;
					case CHANGELAP:
						lap_change();
						break;
					case CHANGECOLOR:
						currentColor = actionCard.getChoice(); // Implementa un metodo per scegliere il colore
						break;
					// Aggiungi altri casi per le altre azioni, se presenti
				}
			}
			if(e != null)
			{
				deck.playCard(e);
				setCurrentCardInformation(e);
			}
		}

		this.controlIfOneCard();
		this.getCurrentPlayer().setOneFalse();

		next_turn();
		System.out.println("\n\n");
	}
	/* Modifica il colore corrente */
	private void change_current_color(Colour color)
	{
		this.currentColor = color;
	}

	// Return the instance of the next player in turns order`
	private Player nextPlayer()
	{
		return sitDownPlayer.get(this.control_index(CurrentIndexPlayer + 1));
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
	public void lap_change()
	{
		Collections.reverse(this.sitDownPlayer);

	}

	
	/* Blocca il turno del giocatore successivo*/
	public void block_turn()
	{
		this.sitDownPlayer.get(this.control_index(CurrentIndexPlayer + 1)).setBlock();
	}
	
	/* Passa al turno al player successivo*/

	public void next_turn()
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
	public int control_index(int index)
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
	
	/* Da chiamare ogni volta che viene attivato un actionlistener di un bottone */
	/* Controlla se è il turno del giocatore che gioca la carta, se lo è può giocare altrimenti invia un messaggio di errore*/
	public boolean is_my_turn()
	{
		if(this.sitDownPlayer.get(CurrentIndexPlayer) instanceof UserPlayer)
		{
			return true;
		}
		return false;
	}
	
	/*Sostituisce la carta corrente sul tavolo*/
	public void replace_current_card(Card e)
	{
		this.currentCard = e;
	}

	public String lastUserCardName() {
		return null;
	}

	public void playTurn(int i, Colour c) {
	}


	// AREA TEST

	public void incrementIndex()
	{
		this.set_deck();
		this.deck.set_initial_card();
		while(true)
		{
			Card e = this.deck.drawOut();
			this.deck.playCard(e);
			System.out.println("PESCATO: " + e.getName().toUpperCase());
		}
	}

	public static void main(String[] args) {
		Table.getInstance().setCurrentCardInformation(new NormalCard(Number.FIVE, Colour.GREEN));

		System.out.println("COLORE CORRENTE:" + Table.getInstance().getCurrentColor() + "CARTA CORRENTE:" + Table.getInstance().getCurrentCard());

		BotPlayer testbot = new BotPlayer("botest");
		testbot.drawCard(new NormalCard(Number.FIVE, Colour.BLUE));
		testbot.drawCard(new NormalCard(Number.EIGHT, Colour.GREEN));
		testbot.drawCard(new NormalCard(Number.THREE, Colour.GREEN));
		testbot.drawCard(new NormalCard(Number.ZERO, Colour.YELLOW));
		testbot.drawCard(new NormalCard(Number.THREE, Colour.GREEN));
		testbot.drawCard(new ActionCard(Caction.CHANGECOLOR, Colour.BLACK));
		testbot.drawCard(new ActionCard(Caction.DRAWFOUR, Colour.BLACK));
		testbot.drawCard(new ActionCard(Caction.DRAWTWO, Colour.RED));

		for(Card e : testbot.getHand())
		{
			if(testbot.isCardValid(e))
				System.out.println("La carta " + e.getName() + " puo essere giocata.");
			else
				System.out.println("Carta " + e.getName() + " NON VALIDA.");
		}

	}

	public Colour getCurrentColor() {
		return this.currentColor;
	}

	public List<BotPlayer> getBotPlayers()
	{
		return sitDownPlayer.stream()
				.filter(e -> e instanceof BotPlayer)
				.map(e -> (BotPlayer) e) // Cast esplicito a BotPlayer
				.toList();
	}

	// AREA TEST

}

