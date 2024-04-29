package com.unofx.model;

public enum Event {
    // blocked utilizzato sia in caso non è il turno del giocatore sia se la carta giocata non è valida
    BLOCKED,
    CHANGECARD,
    ALLDONE
}
