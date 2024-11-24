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
    private World world;

    public static <T> List<T> getEntitiesByType(World world, Class<T> entityType) {
        return world.getEntities().keySet().stream()
                    .filter(entityType::isInstance) //filters by isInstance
                    .map(entityType::cast) //type casts
                    .collect(Collectors.toList()); //puts it in mutable list
    }

    public static List<Grass> getGrass(World world) {
        return getEntitiesByType(world, Grass.class);
    }

    public static List<Animal> getAnimals(World world) {
        return getEntitiesByType(world, Animal.class);
    }

    public static List<Burrow> getBurrows(World world) {
        return getEntitiesByType(world, Burrow.class);
    }

    public static <T> T getClosestOfEntity(World world,  Class<T> entityType, Entity actor) {
        List<T> entities = getEntitiesByType(world, entityType);
        int currShortest = Integer.MAX_VALUE;
        T foundEntity = null;

        for (T entity : entities) {
            int distance = actor.getDistanceToLocation(world, world.getLocation(actor), world.getLocation(entity));
            if(distance < currShortest)
            {
                currShortest = distance;
                foundEntity = entity;
            }
        } return foundEntity;
    }

    /** TODO
    public static boolean checkIfEntityOnTile(){

    } */
}

