package gameOfLife.worldObjects.entities;

import gameOfLife.util.WorldHandler;
import gameOfLife.worldObjects.entities.enums.Action;
import gameOfLife.worldObjects.entities.enums.EntityTypeID;
import gameOfLife.worldObjects.entities.enums.Sex;
import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.World;

import java.util.*;

public abstract class Animal extends Entity implements DynamicDisplayInformationProvider {
    protected Sex sex;
    protected double energyMax;
    protected double currentEnergy;
    protected boolean isAwake, isInfected, isPregnant;
    protected Object target;
    protected Animal currentMate;
    protected DisplayInformation currentDisplayInformation;
    protected double hungryThreshold;
    protected Set<EntityTypeID> edibles;

    /*
    TODO consider moving hungryThreshold back to individual animal implementations and as a class constant,
    TODO such that different animal species has a different hunger threshold, some animals will do something earlier than
    TODO others.
     */

    public Animal(int age, Sex sex, boolean isAwake, boolean isInfected) {
        super(age);
        this.sex = sex;
        this.isAwake = isAwake;
        this.isInfected = isInfected;
        this.isPregnant = false;
        this.energyMax = 100;
        this.hungryThreshold = energyMax / 2;
    }

    public Animal(int age, Sex sex, boolean isAwake) {
        this(age, sex, isAwake, false);
    }

    public Animal() {
        this(0, Sex.FEMALE, true, false);
    }

    /*
    START OF ABSTRACT METHODS
     */

    /**
     * Calculates the maximum energy level of an animal.
     */
    protected abstract double calculateMaxEnergy();

    /**
     * Get entity's age of maturity
     *
     * @return ageOfMaturity
     */
    public abstract int getAgeOfMaturity();

    /**
     * Method to update the entity's currentDisplayInformation
     */
    public abstract void updateDisplayInformation();

    /**
     * Method to instruct animal to sleep
     */
    public abstract void sleep();

    /**
     * Method for performing an action. To be called in the act method
     *
     * @param world  the world in which the Animal exitsts
     * @param action the action to be taken
     */
    protected abstract void performAction(World world, Action action);

    /**
     * Determines what action the animal must take.
     *
     * @param world
     * @return Action. an Action enum signifying the action the animal is about to take.
     */
    protected abstract Action determineAction(World world);

    /**
     * find a mate for the animal within a given radius
     *
     * @param world the animal exists in
     * @return Animal the mate for this animal. SHOULD BE OF THE SAME TYPE!
     */
    protected abstract Animal findMate(World world);

    /**
     * Method for instructing the animal to seek a mate and if possible, procreate.
     *
     * @param world in which the animal exists
     */
    protected abstract void seekMateAndCopulate(World world);

    /**
     * Method for instructing the animal to eat
     *
     * @param world
     */
    protected abstract void seekFood(World world);

    /**
     * Instructs the animal to give birth to new animals
     *
     * @param world in which the animal exists
     */
    protected abstract void giveBirth(World world);

    /**
     * Instructs the animal to wake up in the world
     *
     * @param world in which the animal exists
     */
    protected abstract void wakeUp(World world);

    /*
    END OF ABSTRACT METHODS
     */

    /**
     * Return true if animal is female.
     *
     * @return boolean for if animal is female.
     */
    public boolean isFemale() {
        return sex.equals(Sex.FEMALE);
    }

    /**
     * Get the isPregnant status
     *
     * @return isPregnant boolean
     */
    public boolean isPregnant() {
        return isPregnant;
    }

    /**
     * Get whether the entity is awake or not
     *
     * @return isAwake
     */
    public boolean isAwake() {
        return isAwake;
    }

    /**
     * Set isAwake status
     *
     * @param awake boolean
     */
    public void setAwake(boolean awake) {
        isAwake = awake;
    }

    /**
     * Set isInfected status
     *
     * @param infected boolean
     */
    public void setInfected(boolean infected) {
        isInfected = infected;
    }

    /**
     * Return the isInfected status.
     *
     * @return isInfected boolean
     */
    public boolean isInfected() {
        return isInfected;
    }

    /**
     * Get the target of the entity.
     *
     * @return Object which can be a location, another entity
     * or anything the entity might be interested in knowing where is
     */
    public Object getTarget() {
        return target;
    }

    /**
     * Get the reproductive sex of the entity, female or male.
     *
     * @return Sex
     */
    public Sex getSex() {
        return sex;
    }

    /**
     * Get the maximum amount of energy the entity can have
     *
     * @return energyMax
     */
    public double getEnergyMax() {
        return energyMax;
    }

    /**
     * Get the energy the entity currently posses.
     *
     * @return currentEnergy
     */
    public double getCurrentEnergy() {
        return currentEnergy;
    }


    /**
     * Moves actor and
     * @param world
     * @param locations
     */
    public void moveActor(World world, List<Location> locations) {
        if (world
                .getEmptySurroundingTiles()
                .isEmpty()) {
            world.move(this, world.getLocation(this));
        } else {
            Location nextLocation = computeNextRandom(locations);
            world.move(this, nextLocation);
            world.setCurrentLocation(nextLocation);

        }
    }

    public List<Location> findNextTileInShortestPath(World world, Location targetLocation) {
        int currentShortestPathLength = Integer.MAX_VALUE;
        Map<Location, Integer> mapOfShortestLocation = new HashMap<>();

        for (Location tile : world.getEmptySurroundingTiles()) {
            if (getDistanceToLocation(world, targetLocation, tile) < currentShortestPathLength) {
                currentShortestPathLength = getDistanceToLocation(world, targetLocation, tile);
                mapOfShortestLocation.put(tile, currentShortestPathLength);
            }
        }
        if (world
                .getEmptySurroundingTiles()
                .isEmpty() || (currentShortestPathLength >= getDistanceFromActorToLocation(world, targetLocation))) {
            return List.of(world.getLocation(this)); //does not move if locked in, or movement increases distance to target
        }

        int finalCurrentShortestPathLength = currentShortestPathLength;

        return mapOfShortestLocation
                .entrySet()
                .stream()
                .filter(entry -> (entry.getValue() == finalCurrentShortestPathLength)) // filter condition
                .map(Map.Entry::getKey) // extract the key
                .toList(); //returns immutable list
    }

    public Edible getClosestEdible(World world) {
        Set<Edible> listOfEdibles = WorldHandler.getEntitiesByType(world, Edible.class);
        Set<Edible> listOfValidEdibles = new HashSet<>();

        for (Edible   edible : listOfEdibles) {
            if (edibles.contains(edible.getEntityTypeID())) {
                listOfValidEdibles.add(edible);
            }
        }
        return WorldHandler.getClosestOfEntityFromList(world, listOfValidEdibles, this);
    }


    /**
     * Begins pathing towards a desired target by choosing the adjacent tile that leads
     * to the shortest path - (stays still if moving would create greater distance between animal and target Location)
     *
     * @param world game world
     */
    public void seekTarget(World world, Location target) {
        this.target = target;
        Location targetLocation = world.getLocation(this.getTarget());
        moveActor(world, findNextTileInShortestPath(world, targetLocation));
    }

    @Override
    public DisplayInformation getInformation() {
        return currentDisplayInformation;
    }
}


