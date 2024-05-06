package com.unofx.model;

import javafx.scene.control.Tab;

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
		/*
		// controllo se il giocatore ha inserito un nome utente
		if(this.user.getUsername() != null)
		{
			this.sitDownPlayer.add(this.user);
		}
		// altrimenti aggiungo un utente chiamata 'user'
		else
		{
			this.sitDownPlayer.add(new UserPlayer("user"));
		}
		numberOfPlayer--;

		while(numberOfPlayer > 0)
		{
			this.sitDownPlayer.add(new BotPlayer("bot" + numberOfPlayer));
			numberOfPlayer--;
		}
		*/
		 this.sitDownPlayer.add(new UserPlayer("Marco"));
		 this.sitDownPlayer.add(new BotPlayer("bot1"));
		this.sitDownPlayer.add(new BotPlayer("bot2"));
		this.sitDownPlayer.add(new BotPlayer("bot3"));
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
	
	/* Costruisce il tavolo e inizia la partita */
	// TODO modificare il metodo in base a se la partita è a squadre o meno
	public void start_game(int numberOfPlayer, boolean squad)
	{

		this.set_deck();
		this.currentCard = this.deck.drawOut();
		this.deck.playCard(currentCard);
		this.set_player_list(numberOfPlayer);
		this.give_start_card();
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
		if(this.currentPlayer.getHand() == null)
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

	/* TODO passo -1 se il player corrente è un bot */
	private Card select_card(int index)
	{
		Card selected_card;
		if(this.currentPlayer instanceof BotPlayer)
		{
			int rand = 0; //rdn.nextInt(0, this.currentPlayer.getHand().size());
			selected_card = this.currentPlayer.playCard(rand);
		}
		else 
		{
			selected_card = this.currentPlayer.playCard(index);
		}
		
		return selected_card;
	}


	// TODO se ritorna false stampare un avviso che dice che la carta non può essere giocata (nella grafica)
	public Event play_card(Card e)
	{
		// controllo se il player è bloccato
		if(this.currentPlayer.is_blocked()){
			this.currentPlayer.removeBlock();
			this.next_turn();
			return Event.BLOCKED;
		}

		// controllo se è il mio turno
		//if(!this.is_my_turn())
		//	return Event.BLOCKED;
		
		/* controllo se la carta può essere giocata */
		if (e instanceof NormalCard && this.currentCard instanceof NormalCard) {
			NormalCard normal_played = (NormalCard)e;
			NormalCard normal_current = (NormalCard)this.currentCard;
			// Controllo se la carta può essere giocata
			if(normal_played.getNumber() != normal_current.getNumber() &&
					normal_played.getColor() != this.currentColor)
				return Event.CHANGECARD;
		}

		if(e instanceof ActionCard && this.currentCard instanceof ActionCard) {
			ActionCard action_played = (ActionCard)this.currentCard;
			ActionCard action_current = (ActionCard)this.currentCard;
			// Controllo se la carta può essere giocata
			if(action_played.getColor() != this.currentColor &&
					action_played.getAction() != action_current.getAction())
				return Event.CHANGECARD;
			else if(action_played.getColor() == Colour.BLACK &&
					action_current.getColor() == Colour.BLACK)
				return Event.CHANGECARD;
		}

		
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
			this.block_turn();
		}
		else if(e.getAction() == Caction.CHANGELAP)
		{
			this.lap_change();
		}

		//Annuncio il vincitore
		if(this.control_winner())
			this.announce_winner(this.currentPlayer);

		this.next_turn();
		this.replace_current_card(e);
		return Event.ALLDONE;
	}
	/* Modifica il colore corrente */
	private void change_current_color(Colour color)
	{
		this.currentColor = color;
	}

	// Return the instance of the next player in turns order
	private Player nextPlayer()
	{
		return this.sitDownPlayer.get(Table.CurrentIndexPlayer +1);
	}


	
	/* Cambia giro, inverte l'ordine dei giocatori nella lista*/
	public void lap_change()
	{
		// prendo la persona prima e la rimetto al posto giusto dopo aver invertito il giro
		Player p = this.sitDownPlayer.get(CurrentIndexPlayer);
		//TODO error in line 268
		Collections.reverse(sitDownPlayer);
		Table.CurrentIndexPlayer = this.sitDownPlayer.indexOf(p);
	}
	
	/* Blocca il turno del giocatore successivo*/
	public void block_turn()
	{
		this.sitDownPlayer.get(this.control_index(CurrentIndexPlayer + 1)).setBlock();
	}
	
	/* Passa al turno al player successivo*/
	public void next_turn()
	{
		/* continuo ad incrementare fino quando il giocatore successivo non è blocked */
		if(Table.CurrentIndexPlayer < (this.sitDownPlayer.size() -1))
		{
			while(this.sitDownPlayer.get(CurrentIndexPlayer).is_blocked())
				Table.CurrentIndexPlayer++;
			this.currentPlayer = this.sitDownPlayer.get(Table.CurrentIndexPlayer);
		}
		else
		{
			Table.CurrentIndexPlayer = 0;
			while(this.sitDownPlayer.get(CurrentIndexPlayer).is_blocked())
				Table.CurrentIndexPlayer++;
			this.currentPlayer = this.sitDownPlayer.get(Table.CurrentIndexPlayer);
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
}

