package com.unofx.model;

import java.util.*;

public class Table
{
	private static Table INSTANCE = null;
	private static int CurrentIndexPlayer = 0;
	public Deck deck;
	private Card currentCard;
	private Colour currentColor;
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

	public String getLastUserCard()
	{
		return this.currentPlayer.get_info_hand().get(this.currentPlayer.get_info_hand().size() - 1);
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

	public List<String> get_user_info_card()
	{
		// TODO memorizzare nel table l'istanza dell'user
		for(Player e : this.sitDownPlayer)
		{
			if(e instanceof UserPlayer)
			{
				return e.get_info_hand();
			}

		}
		return null;
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

	public void setCurrentCard(Card currentCard) {
		this.currentCard = currentCard;
	}

	/* Metodo da chiamare tutte le volte che un player ha giocato una carta per capire se ha vinto */
	public boolean control_winner()
	{
		if(this.currentPlayer.getHand().stream().count() == 0)
		{
			return true;
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
		System.out.println("\n\n\nCARTA CORRENTE: " + this.getCurrentCard().getName().toUpperCase() + " | COLORE CORRENTE: " + this.getCurrentColor() + "\n");

		System.out.println("E IL TURNO DI: " + this.currentPlayer.getUsername().toUpperCase());
		System.out.println(this.currentPlayer.getUsername().toUpperCase() + " GIOCA " + e.getName().toUpperCase());
		// controllo se il player è bloccato
		if(this.currentPlayer.is_blocked()){
			this.currentPlayer.removeBlock();

			//TODO da modificare
			e = currentCard;
		}



		// controllo se è il mio turno
		//if(!this.is_my_turn())
		//	return Event.BLOCKED;
		


		
		/* Se sono qui significa che la carta può essere giocata */
		
		/* SE LA CARTA E NERA */
		if(e.getColor() == Colour.BLACK)
		{
			this.change_current_color(((ActionCard)e).getChoosenColour());
			if((e.getAction() == Caction.DRAWFOUR)) {
				this.nextPlayer().drawCard(this.deck.drawOut());
				this.nextPlayer().drawCard(this.deck.drawOut());
				this.nextPlayer().drawCard(this.deck.drawOut());
				this.nextPlayer().drawCard(this.deck.drawOut());
			}
		}
		/* SE LA CARTA NON E NERA */
		else if(e.getAction() == Caction.DRAWTWO)
		{
			this.nextPlayer().drawCard(this.deck.drawOut());
			this.nextPlayer().drawCard(this.deck.drawOut());
		}
		else if(e.getAction() == Caction.BLOCKTURN)
		{
			this.nextPlayer().setBlock();
		}
		else if(e.getAction() == Caction.CHANGELAP)
		{
			this.lap_change();
		}

		this.next_turn();
		this.replace_current_card(e);
		this.change_current_color(e.getColor());

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
		//return this.sitDownPlayer.get(this.control_index(CurrentIndexPlayer + 1));
		//int nextIndex = (CurrentIndexPlayer + 1) % this.sitDownPlayer.size(); // Calcola l'indice del prossimo giocatore
		//return this.sitDownPlayer.get(nextIndex); // Restituisce il giocatore successivo
		return this.sitDownPlayer.get(this.control_index(this.sitDownPlayer.indexOf(this.currentPlayer) + 1));
	}


	
	/* Cambia giro, inverte l'ordine dei giocatori nella lista*/
	public void lap_change()
	{
		// prendo la persona prima e la rimetto al posto giusto dopo aver invertito il giro
		/*
		Player p = this.sitDownPlayer.get(CurrentIndexPlayer);
		//TODO error in line 268
		Collections.reverse(sitDownPlayer);
		Table.CurrentIndexPlayer = this.sitDownPlayer.indexOf(p);
		*/


		Collections.reverse(this.sitDownPlayer);

	}

	
	/* Blocca il turno del giocatore successivo*/
	public void block_turn()
	{
		this.sitDownPlayer.get(this.control_index(CurrentIndexPlayer + 1)).setBlock();
	}
	
	/* Passa al turno al player successivo*/
	// TODO non funziona
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
		int i = 0;
		Table.getInstance().set_player_list(8);
		Table.getInstance().set_deck();
		Table.getInstance().give_start_card();
		this.setCurrentPlayer();

		while(i < 18)
		{
			//Table.getInstance().play_card(new NormalCard(Number.EIGHT, Colour.BLUE));
			Table.getInstance().next_turn();
			this.currentPlayer = this.sitDownPlayer.get(Table.CurrentIndexPlayer);
			System.out.println(this.getCurrentPlayer());
			i++;
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

