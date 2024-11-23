package gameOfLife.worldObjects.entities;

import itumulator.world.World;

/**
 * Abstract representation of a plant, which is to be inherited from, that implements Actor interface to be able to act in the world
 */
public abstract class Plant extends Entity implements Edible{
    protected boolean isEdible;
    protected int providedSustenance;

    public Plant(int age, boolean isEdible){
        super(age);
        this.isEdible = isEdible;
    }

    /**
     * Returns the boolean representing if the Plant is edible by an animal
     * @return isEdible
     */
    public boolean isEdible() {
        return isEdible;
    }

    /**
     * Sets the boolean representing if the Plant is edible by an animal.
     * To be used if a plant can gain/lose their edibility
     * @param edible
     */
    public void setEdible(boolean edible) {
        isEdible = edible;
    }

    /**
     * Returns the amount of providedSustenance the Grass provides.
     * @return providedSustenance. Maximum amount provided when eaten.
     */
    public int getProvidedSustenance() {
        return providedSustenance;
    }

    /**
     * Sets the maximum amount of providedSustenance the piece of Grass provides when eaten
     * @param sustenance the amount of sustenance the piece of grass provides
     */
    public void setProvidedSustenance(int sustenance) {
        this.providedSustenance = sustenance;
    }

}
