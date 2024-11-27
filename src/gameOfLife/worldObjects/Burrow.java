package gameOfLife.worldObjects;

import gameOfLife.worldObjects.entities.Rabbit;
import gameOfLife.worldObjects.entities.enums.EntityTypeID;
import itumulator.world.NonBlocking;

import java.util.HashSet;

/**
 * Represents a burrow a rabbit can hide in
 */
public class Burrow extends AnimalHome implements NonBlocking {

    private int capacity;

    /**
     * Burrow constructor
     * @param capacity the initial maximum amount of rabbits the burrow can encompass as any given time
     */
    public Burrow(int capacity) {
        if(capacity < 1) throw new IllegalArgumentException("capacity must be greater than 0!");
        this.capacity = capacity;
        this.inhabitants = new HashSet<>();
        this.owners = new HashSet<>();
    }

    public Burrow(int capacity, Rabbit rabbit) {
        this(capacity);
        owners.add(rabbit);
    }

    public Burrow(Rabbit rabbit){
        this(3, rabbit);
    }

    public Burrow(){
        this(3);
    }

    /**
     * Returns the capacity of the burrow
     * @return capacity
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Sets the capacity of the burrow
     * @param capacity the current maximum number of rabbits that can be inside burrow.
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Get the EntityTypeID associated with burrows
     * @return EntityTypeID.BURROW
     */
    public EntityTypeID getEntityTypeID() {return EntityTypeID.BURROW;}


}
