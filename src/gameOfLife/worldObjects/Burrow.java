package gameOfLife.worldObjects;

import gameOfLife.worldObjects.entities.Rabbit;
import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a burrow a rabbit can hide in
 */
public class Burrow implements NonBlocking, DynamicDisplayInformationProvider {
    // class fields begin

    private static final int SMALL_HOLE_LIMIT = 2; //the limit after which burrow is displayed as big

    //possible images for burrows
    private static final DisplayInformation SMALL_HOLE = new DisplayInformation(Color.green, "hole-small");
    private static final DisplayInformation BIG_HOLE = new DisplayInformation(Color.green, "hole");

    // class fields end

    // instance fields begin

    private Set<Rabbit> rabbitsInBurrow, owners;
    private int capacity;
    private DisplayInformation currentDisplayInformation;

    // instance fields end

    /**
     * Burrow constructor
     * @param capacity the initial maximum amount of rabbits the burrow can encompass as any given time
     */
    public Burrow(int capacity) {
        if(capacity < 1) throw new IllegalArgumentException("capacity must be greater than 0!");
        this.capacity = capacity;
        this.rabbitsInBurrow = new HashSet<>();
        this.owners = new HashSet<>();
        if(capacity > SMALL_HOLE_LIMIT){
            currentDisplayInformation = BIG_HOLE;
        }else {
            currentDisplayInformation = SMALL_HOLE;
        }
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
     * Returns the set representing the rabbits currently residing in the burrow
     * @return Set<Rabbit> rabbitsInHole
     */
    public Set<Rabbit> getRabbitsInHole() {
        return rabbitsInBurrow;
    }

    /**
     * Sets the capacity of the burrow
     * @param capacity the current maximum number of rabbits that can be inside burrow.
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
     * Attempts to add a rabbit to the burrow
     * @param rabbit the rabbit to be added to the burrow
     */
    public void addRabbit(Rabbit rabbit){
        rabbitsInBurrow.add(rabbit);
    }

    /**
     * Attempts to remove a rabbit from the burrow
     * @param rabbit the rabbit to be removed from the burrow
     */
    public void removeRabbit(Rabbit rabbit) {
        rabbitsInBurrow.remove(rabbit);
    }

    /**
     * Adds to the set of owners the rabbit in question and sets the rabbits burrow to this burrow
     * @param rabbit
     */
    public void addOwner(Rabbit rabbit) {
        owners.add(rabbit);
        rabbit.setBurrow(this);
    }
    /**
     * Removes from the set of owners the rabbit in question, and sets the rabbits burrow to null
     * @param rabbit
     */
    public void removeOwner(Rabbit rabbit) {
        owners.remove(rabbit);
        rabbit.setBurrow(null);
    }

    /**
     * Destroys the Burrow, deleting it from the world and attempts to place any rabbits inside around Burrow. Any
     * not placed are killed. All rabbits associated with burrow gets their burrow set to null
     * @param world
     */
    public void destroyBurrow(World world){

        Set<Location> emptyTiles = world.getEmptySurroundingTiles(world.getLocation(this));

        //find number of rabbits to place and place them
        int rabbitsToPlace = Math.min(rabbitsInBurrow.size(), emptyTiles.size());
        Rabbit rabbit;
        Location location;
        for(int i = 1; i<= rabbitsToPlace; i++){
            rabbit = rabbitsInBurrow.iterator().next();
            location = emptyTiles.iterator().next();
            rabbit.exitBurrow(world, location);
            emptyTiles.remove(location);
            rabbitsInBurrow.remove(rabbit);
        }
        //kill any remaining rabbits
        for(Rabbit rabbitToDie : rabbitsInBurrow){
            world.delete(rabbitToDie);
        }
        for(Rabbit r : owners){
            r.setBurrow(null);
        }
        world.delete(this);
    }

    /**
     * Method to get the current DisplayInformation for the burrow, which is subject to change
     * @return currentDisplayInformation current display information to be given when burrow is drawn
     */
    @Override
    public DisplayInformation getInformation() {
        return currentDisplayInformation;
    }
}
