package net.multineural.minecraft.persist;

public class PlayerPojo {

    private int numLives = 3; //TODO make this configurable

    public int getNumLives() {
        return numLives;
    }

    public void setNumLives(int numLives) {
        this.numLives = numLives;
    }

}
