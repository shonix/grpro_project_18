package gameOfLife.entities;

import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public abstract class Entities {

    /**
     * Given a list and an actor, it moves the actor to a random location within that list and updates its current location.
     * @param actor current actor
     * @param locations list of locations
     */



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


    public int getDistanceFromActorToLocation(Location targetLocation, World world) {
        return getDistanceToLocation(world.getLocation(this), targetLocation, world);
    }


    public int getDistanceToLocation(Location startLocation, Location targetLocation, World world) {
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

}
