package gameOfLife.util;

import gameOfLife.worldObjects.Burrow;
import gameOfLife.worldObjects.entities.Animal;
import gameOfLife.worldObjects.entities.Entity;
import gameOfLife.worldObjects.entities.Grass;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


public class WorldHandler {


    public static <T> List<T> getEntitiesByType(World world, Class<T> entityType) {
        return world
                .getEntities()
                .keySet()
                .stream()
                .filter(entityType::isInstance) //filters by isInstance
                .map(entityType::cast) //type casts
                .collect(Collectors.toList()); //puts it in mutable list
    }


    public static <T> T getClosestOfEntity(World world, Class<T> entityType, Entity actor) {
        List<T> entities = getEntitiesByType(world, entityType);
        int currShortest = Integer.MAX_VALUE;
        T foundEntity = null;

        for (T entity : entities) {
            int distance = actor.getDistanceToLocation(world, world.getLocation(actor), world.getLocation(entity));
            if (distance < currShortest) {
                currShortest = distance;
                foundEntity = entity;
            }
        }
        return foundEntity;
    }


    public static <T> boolean checkIfEntityOnTile(World world, Location location, Class<T> type) {
        List<T> listOfEntities = getEntitiesByType(world, type);
        if (!listOfEntities.isEmpty()) {
            return getEntitiesByType(world, type)
                    .stream()
                    .anyMatch(entity -> world.getLocation(entity).equals(location));
        }
        return false;
    }

    public static Set<Location> getAllTiles(World world){
        Location midpoint = new Location(world.getSize()/2, world.getSize()/2);
        Set<Location> out = world.getSurroundingTiles(midpoint, world.getSize()+1);
        out.add(midpoint);
        return out;
    }
}

