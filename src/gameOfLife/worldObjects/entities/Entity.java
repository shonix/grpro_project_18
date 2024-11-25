package gameOfLife.worldObjects.entities;

import gameOfLife.worldObjects.models.EntityID;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Abstract class to represent the different kind of entities and actors that can
 * populate a world. Some methods are provided that won't be logically applicable
 * to every kind of entity, but supports modularity and future feature expansion.
 */
public abstract class Entity implements Actor{
    protected int age;


    public Entity(int age) {
        this.age = age;
    }

    /**
     * Instructs the entity to age. Argument world, doesn't do anything by default, but
     * is provided to allow functionality where applicable in subclasses.
     * @param world the world in which the entity exists.
     */
    public abstract void age(World world);

    /**
     * Instructs the entity to act in the world.
     * @param world providing details of the position on which the entity is currently located and much more.
     */
    public abstract void act(World world);

    /**
     * Instructs the entity to die, deleting it from the world. Argument world
     * does nothing by default, but is provided to allow functionality where applicable
     * in subclasses, for example to allow Animals to leave a corpse on the world map.
     * @param world providing details of the position on which the entity is currently located and much more.
     */
    public abstract void die(World world);

    /**
     * Get the entity's age
     * @return age of the entity
     */
    public int getAge() {
        return age;
    }

    public abstract EntityID getEntityID();

    /**
     * Set the age of the entity.
     * @param age the entity is set to have
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Gets empty surrounding tiles at transforms them from a Set to a List
     * @return list of locations
     */
    public List<Location> getEmptyAdjacentTiles(World world) {
        Set<Location> neighbours = world.getEmptySurroundingTiles();
        return neighbours.stream().toList();
    }

    /**
     * Takes a list of Locations and returns a random one
     * @param locations list of locations
     */
    public Location computeNextRandom(List<Location> locations) {
        return locations.get((new Random()).nextInt(locations.size()));
    }

    public int getDistanceFromActorToLocation(World world, Location targetLocation) {
        return getDistanceToLocation(world, world.getLocation(this), targetLocation);
    }

    public int getDistanceToLocation(World world, Location startLocation, Location targetLocation) {
        if (startLocation.equals(targetLocation)) { return 0;}

        boolean locationFound = false;
        int distanceToTarget = 1;

        while (!locationFound) {
            if (world.getSurroundingTiles(startLocation, distanceToTarget).contains(targetLocation)) {
                locationFound = true;
            } else {
                distanceToTarget++;
            }
        } return distanceToTarget;
    }



    /**
     * Returns the closest edible object.
     * @param world world object.
     * @param startLocation From where the check is made
     * @return Closest edible object.
     */
    public Edible getClosestEdible(World world, Location startLocation)
    {
        int currShortest = Integer.MAX_VALUE;
        Object foundGrass = null;
        for ( Object o : world.getEntities().keySet())
        {
            if(o instanceof Edible)
            {
                int distance = getDistanceToLocation(world, startLocation,world.getEntities().get(o));
                if(distance < currShortest)
                {
                    currShortest = distance;
                    foundGrass = o;
                }
            }
        }
        return (Edible) foundGrass;
    }



}
