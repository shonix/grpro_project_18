package gameOfLife.entities;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.NonBlocking;

import java.awt.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Represents a hole a rabbit can hide in
 */
public class Hole implements NonBlocking, DynamicDisplayInformationProvider {
    private static final int SMALL_HOLE_LIMIT = 2; //the limit after which hole is displayed as big
    //possible images for holes
    private static final DisplayInformation SMALL_HOLE = new DisplayInformation(Color.green, "hole-small");
    private static final DisplayInformation BIG_HOLE = new DisplayInformation(Color.green, "hole");

    private Set<Rabbit> rabbitsInHole;
    private int capacity;
    private DisplayInformation currentDisplayInformation;

    public Hole(int capacity) {
        if(capacity < 1) throw new IllegalArgumentException("capacity must be greater than 0!");
        this.capacity = capacity;
        this.rabbitsInHole = new HashSet<>();
        if(capacity > SMALL_HOLE_LIMIT){
            currentDisplayInformation = BIG_HOLE;
        }else {
            currentDisplayInformation = SMALL_HOLE;
        }
    }

    /**
     * Returns the capacity of the hole
     * @return capacity
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Returns the set representing the rabbits currently residing in the hole
     * @return Set<Rabbit> rabbitsInHole
     */
    public Set<Rabbit> getRabbitsInHole() {
        return rabbitsInHole;
    }

    /**
     * Sets the capacity of the hole
     * @param capacity the current maximum number of rabbits that can be inside hole.
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
        if(capacity > SMALL_HOLE_LIMIT){
            currentDisplayInformation = BIG_HOLE;
        }else {
            currentDisplayInformation = SMALL_HOLE;
        }
    }

    /**
     * Attempts to add a rabbit to the hole
     * @param rabbit the rabbit to be added to the hole
     */
    public void addRabbit(Rabbit rabbit){
        rabbitsInHole.add(rabbit);
    }

    /**
     * Attempts to remove a rabbit from the hole
     * @param rabbit the rabbit to be removed from the hole
     */
    public void removeRabbit(Rabbit rabbit) {
        rabbitsInHole.remove(rabbit);
    }

    /**
     * Method to get the current DisplayInformation for the hole, which is subject to change
     * @return currentDisplayInformation current display information to be given when hole is drawn
     */
    @Override
    public DisplayInformation getInformation() {
        return currentDisplayInformation;
    }
}
