package com.unofx;

public class GameState {
    private static GameState instance;
    private String choosenMode;

    private GameState() {
        this.choosenMode = "Single";  // Modalità di default
    }

    public static GameState getInstance() {
        if (instance == null) {
            instance = new GameState();
        }
        return instance;
    }

    public String getChoosenMode() {
        return choosenMode;
    }

    public void setChoosenMode(String choosenMode) {
        this.choosenMode = choosenMode;
    }
}

