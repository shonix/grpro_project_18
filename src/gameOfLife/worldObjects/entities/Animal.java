package gameOfLife.worldObjects.entities;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

import java.util.*;

public abstract class Animal extends Entity implements DynamicDisplayInformationProvider {
    protected Sex sex;
    protected double energyMax;
    protected double actualEnergy;
    protected boolean isAwake, isInfected;
    protected Object target;
    protected DisplayInformation currentDisplayInformation;


    public enum Sex {
        MALE, FEMALE;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

    public Animal(int age, Sex sex, boolean isAwake, boolean isInfected) {
        super(age);
        this.sex = sex;
        this.isAwake = isAwake;
        this.isInfected = isInfected;
    }

    public Animal(int age, Sex sex, boolean isAwake){
        this(age, sex, isAwake, false);
    }

    public Animal(){
        this(0, Sex.FEMALE, true, false);
    }

    /*
    START OF ABSTRACT METHODS
     */

    /**
     * Calculates the maximum energy level of an animal.
     */
    protected abstract double calculateMaxEnergy(double age);

    /**
     * Get entity's age of maturity
     * @return ageOfMaturity
     */
    public abstract int getAgeOfMaturity();

    /*
    END OF ABSTRACT METHODS
     */

    /**
     * Get the target of the entity.
     * @return Object which can be a location, another entity
     * or anything the entity might be interested in knowing where is
     */
    public Object getTarget() {return target;}

    /**
     * Get the reproductive sex of the entity, female or male.
     * @return Sex
     */
    public Sex getSex() { return sex; }

    /**
     * Get the maximum amount of energy the entity can have
     * @return energyMax
     */
    public double getEnergyMax() { return energyMax; }

    /**
     * Get the energy the entity currently posses.
     * @return actualEnergy
     */
    public double getActualEnergy() { return actualEnergy; }

    /**
     * Get whether the entity is awake or not
     * @return isAwake
     */
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
    
    @Override
    public DisplayInformation getInformation(){
        return currentDisplayInformation;
    }

    public abstract void updateDisplayInformation();
}


