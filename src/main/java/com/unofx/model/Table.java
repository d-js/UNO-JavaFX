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
	private UserPlayer user = new UserPlayer("diego");
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


	public UserPlayer getUserPlayer() {
		return user;
	}

	public void setUser(UserPlayer user) {
		this.user = user;
	}

	public Player getCurrentPlayer() {
		return this.currentPlayer;
	}

	public List<Player> getSitDownPlayer() {
		return sitDownPlayer;
	}

	public String getUsername() {
		return user.getUsername();
	}

	/* Metodo per costruire la lista di giocatori della partita*/
	public void set_player_list(int numberOfPlayer)
	{

		// controllo se il giocatore ha inserito un nome utente


		while(numberOfPlayer > 0)
		{
			this.sitDownPlayer.add(new BotPlayer("bot" + numberOfPlayer));
			numberOfPlayer--;
		}

		if(this.user.getUsername() != null)
		{
			this.sitDownPlayer.add(this.user);
		}
		// altrimenti aggiungo un utente chiamata 'user'
		else
		{
			this.sitDownPlayer.add(new UserPlayer("user"));
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

	/* Metodo per impostare il nome nel caso lo volesse fare l'utente*/
	public void set_username(String username)
	{
		this.user = new UserPlayer(username);
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


	public void play_card(Card e)
	{
		System.out.println(this.currentPlayer.getUsername().toUpperCase() + " STA GIOCANDO...\n");
		System.out.println("La carta sul tavolo e' " + this.currentCard.getName());

		if (e == null) {
			System.out.println(this.currentPlayer.getUsername() + " non ha una carta giocabile.'");
			e = this.currentCard;
		} else {
			System.out.println(this.currentPlayer.getUsername() + " gioca " + e.getName());
		}

		System.out.println("La mano rimanente di " + this.currentPlayer.getUsername() + this.currentPlayer.get_info_hand());

		// Gestione delle carte di azione
		if (e instanceof ActionCard) {
			ActionCard actionCard = (ActionCard) e;
			switch (actionCard.getAction()) {
				case DRAWTWO:
					nextPlayer().drawCard(deck.drawOut());
					nextPlayer().drawCard(deck.drawOut());
					break;
				case DRAWFOUR:
					//TODO il colore selzionato nei +4 e è2 non e' dinamico ma pre il momento ritorna sempre BLUE, (da cambiare)
					actionCard.setChoosenColour(Colour.BLUE); // Implementa un metodo per scegliere il colore
					currentColor = actionCard.getChoosenColour();
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
					currentColor = Colour.BLUE; // Implementa un metodo per scegliere il colore
					break;
				// Aggiungi altri casi per le altre azioni, se presenti
			}
		}

		// Controllo se il player è bloccato
		if (this.currentPlayer.is_blocked()) {
			this.currentPlayer.removeBlock();
			e = currentCard;
		}

		deck.playCard(e);
		setCurrentCardInformation(e);
		next_turn();
		System.out.println("\n\n");
	}
	/* Modifica il colore corrente */
	private void change_current_color(Colour color)
	{
		this.currentColor = color;
	}


	//TODO fare il metodo pesca che pesca la carta e passa il turno

	// Return the instance of the next player in turns order`
	private Player nextPlayer()
	{
		return this.sitDownPlayer.get(this.control_index(this.sitDownPlayer.indexOf(this.currentPlayer) + 1));
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
		if(!this.sitDownPlayer.isEmpty())
		{
			int nextPlayerIndex = (Table.CurrentIndexPlayer + 1) % this.sitDownPlayer.size(); // Calcola l'indice del prossimo giocatore

			while (this.sitDownPlayer.get(nextPlayerIndex).is_blocked()) {
				nextPlayerIndex = (nextPlayerIndex + 1) % this.sitDownPlayer.size(); // Avanza fino a trovare un giocatore non bloccato
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
		Table.getInstance().incrementIndex();
	}

	public Colour getCurrentColor() {
		return this.currentColor;
	}

	// AREA TEST
}

