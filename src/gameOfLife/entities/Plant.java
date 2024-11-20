package gameOfLife.entities;

import itumulator.simulator.Actor;
import itumulator.world.World;

/**
 * Abstract representation of a plant, which is to be inherited from, that implements Actor interface to be able to act in the world
 */
public abstract class Plant implements Actor {
    protected int age;
    protected boolean isEdible;

    public Plant(boolean isEdible){
        this.age = 0;
        this.isEdible = isEdible;
    }

    /**
     * Returns the Plant's age
     * @return age
     */
    public int getAge() {
        return age;
    }

    /**
     * Set the Plant's age
     * @param age to be set
     */
    public void setAge(int age) {
        this.age = age;
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
     * Ages the Plant by one step
     */
    protected void age(){
        age++;
    }

    /**
     * Instructs the Plant to act in the simulation step
     * @param world providing details of the position on which the actor is currently located and much more.
     */
    public abstract void act(World world);

    /**
     * Instructs the Plant to die, i.e. delete it from the world
     * @param world providing details of the position on which the actor is currently located and much more.
     */
    public abstract void die(World world);

}
