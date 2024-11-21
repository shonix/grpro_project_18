package gameOfLife.entities;

import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

import java.util.*;

public class Animal extends Entities implements Actor {
    private int age;
    private int ageOfMaturity;
    private Sex sex;
    private double energyMax;
    private double actualEnergy;
    private boolean isAwake;
    private Object target; // can be hole or grass


    public enum Sex {
        MALE, FEMALE;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

    public Animal(int age, Sex sex, double ageOfMaturity) {
        this.age = age;
        this.sex = sex;
        this.energyMax = calculateMaxEnergy(age, ageOfMaturity);
        actualEnergy = energyMax;
        isAwake = true;
    }

    @Override
    public void act(World world){

    }

    /**
     * Calculates the maximum energy level of an animal as a parabolic function,
     * with the vertex representing the age at which the animal is fully matured.
     */
    private double calculateMaxEnergy(double age, double ageOfMaturity) {
        return Math.pow(age, 2) + ageOfMaturity * age;
    }

    public Object getTarget() {return target;}


    public double getAge() { return age; }
    public double getAgeOfMaturity() { return ageOfMaturity; }
    public Sex getSex() { return sex; }
    public double getEnergyMax() { return energyMax; }
    public double getActualEnergy() { return actualEnergy; }
    public boolean isAwake() { return isAwake; }


    public void moveActor(World world, Actor actor, List<Location> locations) {
        if (world.getEmptySurroundingTiles().isEmpty()) {
            world.move(actor, world.getLocation(actor));
        } else {
            Location nextLocation = computeNextRandom(locations);
            world.move(actor, nextLocation);
            world.setCurrentLocation(nextLocation);

        }
    }

    public List<Location> findNextTileInShortestPath(World world, Location targetLocation) {
        int currentShortestPathLength = Integer.MAX_VALUE;
        Map<Location, Integer> mapOfShortestLocation = new HashMap<>(); //has to maintain a map instead of Location variable

        for (Location tile : world.getEmptySurroundingTiles()) {
            if (getDistanceToLocation(tile, targetLocation, world) < currentShortestPathLength) {
                currentShortestPathLength = getDistanceToLocation(tile, targetLocation, world);
                mapOfShortestLocation.put(tile, currentShortestPathLength);
            }
        }
        if (world.getEmptySurroundingTiles().isEmpty()) {
            if (currentShortestPathLength <= getDistanceFromActorToLocation(targetLocation, world)) {
                return List.of(world.getLocation(this));
            }
        }
        int finalCurrentShortestPathLength = currentShortestPathLength;
        return mapOfShortestLocation.entrySet().stream()
                .filter(entry -> (entry.getValue() > finalCurrentShortestPathLength)) // Filter condition
                .map(Map.Entry::getKey) // Extract the key
                .toList();
    }
    

}


