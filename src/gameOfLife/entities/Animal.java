package gameOfLife.entities;

import itumulator.world.Location;
import itumulator.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Animal {
    private double age;
    private double ageOfMaturity;
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

    public Animal(double age, Sex sex, double ageOfMaturity) {
        this.age = age;
        this.sex = sex;
        this.energyMax = calculateMaxEnergy(age, ageOfMaturity);
        actualEnergy = energyMax;
        isAwake = true;
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


    public void pathToLocation(World world, Location targetLocation) {
        int currentShortestPath = Integer.MIN_VALUE;
        List<Location> tilesToShortestPath = new ArrayList<>();

        for (Location tile : world.getEmptySurroundingTiles()) {
            if (getDistanceToLocation(tile, targetLocation, world) > currentShortestPath) {
                currentShortestPath = getDistanceToLocation(tile, targetLocation, world);
                tilesToShortestPath.add(tile);
            }
        }
        nex
        world.move(this, computeNextRandomMove(world, tilesToShortestPath));
        world.setCurrentLocation(tilesToShortestPath);
    }

    private Location computeNextRandomMove(World world, List<Location> locations) {
        return locations.get((new Random()).nextInt(locations.size()));
    }

    public int getDistanceFromActorToLocation(Location targetLocation, World world) {
        return getDistanceToLocation(world.getLocation(this), targetLocation, world);
    }

    public int getDistanceToLocation(Location startLocation, Location targetLocation, World world) {
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


