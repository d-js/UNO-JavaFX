package com.unofx.model;

public enum Number {
    ZERO("ZERO"),
    ONE("ONE"),
    TWO("TWO"),
    THREE("THREE"),
    FOUR("FOUR"),
    FIVE("FIVE"),
    SIX("SIX"),
    SEVEN("SEVEN"),
    EIGHT("EIGHT"),
    NINE("NINE");

    private String number;
    Number(String number)
    {
        this.number = number;
    }

    public String getNumberString() {
        return number;
    }
}
