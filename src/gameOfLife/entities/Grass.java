package gameOfLife.entities;

import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

import java.util.Random;
import java.util.Set;

/**
 * Class representing a piece of grass, an extension of a Plant.
 * Grass can be eaten by certain Animals as implemented by them.
 * Grass can grow, increasing the food it provides.
 * Grass can spread instantiating a new piece of grass in a neighbouring location.
 */
public class Grass extends Plant implements NonBlocking {
    public static final double GRASS_SPREAD_CHANCE = 0.10;
    public static final int MIN_FOOD = 1, MAX_FOOD = 10, MAX_AGE = 120;

    private int food;

    public Grass(int food){
        super(true);
        if(food < MIN_FOOD || food > MAX_FOOD) throw new IllegalArgumentException("Invalid amount of food");
        this.food = food;
    }

    /**
     * Returns the amount of food the Grass provides.
     * @return food. Maximum amount provided when eaten.
     */
    public int getFood() {
        return food;
    }

    /**
     * Sets the maximum amount of food the piece of Grass provides when eaten
     * @param food the amount of food
     */
    public void setFood(int food) {
        this.food = food;
    }

    /**
     * Handles spreading of the piece of Grass, by checking if it can spread and then spreading if applicable.
     * @param world facilitates access to the piece of Grass' location
     */
    private void spread(World world){
        Set<Location> empty_neighbours = world.getEmptySurroundingTiles();
        if(empty_neighbours.isEmpty()) return; //if no neighbours: terminate method call

        Random rng = new Random();
        for(Location loc : empty_neighbours){
            if(rng.nextDouble() < GRASS_SPREAD_CHANCE){
                world.setTile(loc, new Grass(1));
            }
        }
    }

    /**
     * Instructs the piece of Grass to act in the world, spreading, dying or aging.
     * @param world providing details of the position on which the actor is currently located and much more.
     */
    @Override
    public void act(World world){
        spread(world); //attempt spreading
        //die if too much has been eaten, or too old
        if(food < MIN_FOOD || age > MAX_AGE){
            die(world);
            return;
        }
        age();
    }

    /**
     * Ages the piece of Grass, incrementing age counter and possibly food counter.
     */
    @Override
    public void age(){
        super.age();
        if(food < MAX_FOOD) food++;
    }

    /**
     * Instructs the piece of Grass to die, deleting it from the world.
     * @param world providing details of the position on which the actor is currently located and much more.
     */
    @Override
    public void die(World world){
        world.delete(this);
    }
}
