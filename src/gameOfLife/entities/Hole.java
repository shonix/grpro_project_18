package gameOfLife.entities;

import itumulator.world.NonBlocking;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a hole a rabbit can hide in
 */
public class Hole implements NonBlocking {
    private Set<Rabbit> rabbitsInHole;
    private int capacity;

    public Hole(int capacity) {
        if(capacity < 1) throw new IllegalArgumentException("capacity must be greater than 0!");
        this.capacity = capacity;
        this.rabbitsInHole = new HashSet<Rabbit>();
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
     * @param capacity
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Attempts to add a rabbit to the hole
     * @param rabbit
     */
    public void addRabbit(Rabbit rabbit){
        rabbitsInHole.add(rabbit);
    }

    /**
     * Attempts to remove a rabbit from the hole
     * @param rabbit
     */
    public void removeRabbit(Rabbit rabbit) {
        rabbitsInHole.remove(rabbit);
    }
}
