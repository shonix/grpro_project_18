package gameOfLife.util;

import gameOfLife.worldObjects.Burrow;
import gameOfLife.worldObjects.entities.*;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;


public class WorldHandler {


    public static <T> Set<T> getEntitiesByType(World world, Class<T> entityType) {
        return world
                .getEntities()
                .keySet()
                .stream()
                .filter(entityType::isInstance) //filters by isInstance
                .map(entityType::cast) //type casts
                .collect(Collectors.toSet()); //puts it in mutable list
    }

    public static <T> T getClosestOfEntityFromList(World world, Set<T> entities, Entity actor) {
        int currentShortest = Integer.MAX_VALUE;
        T foundEntity = null;

        for (T entity : entities) {
            int distance = actor.getDistanceToLocation(world, world.getLocation(actor), world.getLocation(entity));
            if (distance < currentShortest) {
                currentShortest = distance;
                foundEntity = entity;
            }
        }
        return foundEntity;
    }

    /**
     * A method for locating the generic entity of the type supplied, to the actor calling this method.
     * @param world world.
     * @param entityType Class of the entity which is being located.
     * @param actor Actor which are looking for the closest entity
     * @return The closest entity, of the class which has been supplied as entityType
     * @param <T> Generic class of type Entity
     */
    public static <T> T getClosestOfEntity(World world, Class<T> entityType, Entity actor) {
        Set<T> entities = getEntitiesByType(world, entityType);
        return getClosestOfEntityFromList(world, entities, actor);
    }



    public static <T> boolean checkIfEntityOnTile(World world, Location location, Class<T> type) {
        Set<T> listOfEntities = getEntitiesByType(world, type);
        if (!listOfEntities.isEmpty()) {
            return getEntitiesByType(world, type)
                    .stream()
                    .anyMatch(entity -> world.getLocation(entity).equals(location));
        }
        return false;
    }

    /**
     * Returns a set of all locations in the world
     * @param world
     * @return Set<Location>
     */
    public static Set<Location> getAllTiles(World world){
        Location midpoint = new Location(world.getSize()/2, world.getSize()/2);
        Set<Location> out = world.getSurroundingTiles(midpoint, world.getSize()+1);
        out.add(midpoint);
        return out;
    }

    public static Set<Location> getAllTiles(World world, Location location, int radius) {
        Set<Location> out = world.getSurroundingTiles(location, radius);
        out.add(location);
        return out;
    }

    public static Location getRandomLocation(World world) {
        Random rand = new Random();
        return new Location(rand.nextInt(world.getSize()),rand.nextInt(world.getSize()));
    }

    public  static Location getRandomValidLocation(World world) {
        Location location = getRandomLocation(world);

        while (!world.isTileEmpty(location)) {
            location = getRandomLocation(world);
        }
        return location;
    }
}

