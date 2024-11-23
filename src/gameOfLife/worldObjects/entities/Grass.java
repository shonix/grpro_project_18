package gameOfLife.worldObjects.entities;

import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

import java.util.Set;

/**
 * Class representing a piece of grass, an extension of a Plant.
 * Grass can be eaten by certain Animals as implemented by them.
 * Grass can grow, increasing the providedSustenance it provides.
 * Grass can spread instantiating a new piece of grass in a neighbouring location.
 */
public class Grass extends Plant implements NonBlocking {
    public static final double GRASS_SPREAD_CHANCE = 0.01;
    public static final int MIN_PROVIDED_SUSTENANCE = 1, MAX_PROVIDED_SUSTENANCE = 10, MAX_AGE = 120;

    public Grass(int sustenance){
        super(0,true);
        if(sustenance < MIN_PROVIDED_SUSTENANCE || sustenance > MAX_PROVIDED_SUSTENANCE) throw new IllegalArgumentException("Invalid amount of sustenance");
        this.providedSustenance = sustenance;
    }

    /**
     * Handles spreading of the piece of Grass, by checking if it can spread and then spreading if applicable.
     * @param world facilitates access to the piece of Grass' location
     */
    private void spread(World world){
        Set<Location> neighbours = world.getSurroundingTiles();
        if(neighbours.isEmpty()) return; //if no neighbours: terminate method call
        for(Location loc : neighbours){
            if(!world.containsNonBlocking(loc) && Math.random() < GRASS_SPREAD_CHANCE){
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
        age(world); //age and possibly die or increase in sustenance provided
    }

    /**
     * Ages the piece of Grass, incrementing age counter and possibly providedSustenance counter, or kills it if too old or too much
     * has been eaten.
     */
    @Override
    public void age(World world){
        age++;
        //die if too much has been eaten, or too old
        if (providedSustenance < MIN_PROVIDED_SUSTENANCE || age > MAX_AGE) {
            die(world);
        } else if(providedSustenance < MAX_PROVIDED_SUSTENANCE) providedSustenance++;
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
