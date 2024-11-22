package gameOfLife.worldObjects.entities;

import gameOfLife.worldObjects.Burrow;
import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;

import java.awt.*;
import java.util.Map;
import java.util.Set;

public class Rabbit extends Animal {
    //class fields begin
    public static final int AGE_OF_MATURITY = 60; //3 simulation days
    public static final int MAX_AGE = 240; // 12 simulation days
    public static final double DAILY_ENERGY_REDUCTION = 0.1;
    public static final double HUNGRY_THRESHOLD = 2.0;

    //define on the class, all possible images a rabbit can have
    public static final Color RABBIT_COLOR = new Color(218, 205, 184); //color on top-down world view
    private static final DisplayInformation SMALL_RABBIT = new DisplayInformation(RABBIT_COLOR, "rabbit-small");
    private static final DisplayInformation SMALL_RABBIT_SLEEPING = new DisplayInformation(RABBIT_COLOR, "rabbit-small-sleeping");
    private static final DisplayInformation SMALL_RABBIT_FUNGI = new DisplayInformation(RABBIT_COLOR, "rabbit-fungi-small");
    private static final DisplayInformation SMALL_RABBIT_FUNGI_SLEEPING = new DisplayInformation(RABBIT_COLOR, "rabbit-fungi-small-sleeping");
    private static final DisplayInformation LARGE_RABBIT = new DisplayInformation(RABBIT_COLOR, "rabbit");
    private static final DisplayInformation LARGE_RABBIT_SLEEPING = new DisplayInformation(RABBIT_COLOR, "rabbit-sleeping");
    private static final DisplayInformation LARGE_RABBIT_FUNGI = new DisplayInformation(RABBIT_COLOR, "rabbit-fungi");
    private static final DisplayInformation LARGE_RABBIT_FUNGI_SLEEPING = new DisplayInformation(RABBIT_COLOR, "rabbit-fungi-sleeping");
    private static final Map<Integer, DisplayInformation> DISPLAY_INFORMATION_MAP = Map.of(
            0, SMALL_RABBIT_FUNGI_SLEEPING,
            1, SMALL_RABBIT_FUNGI,
            2, SMALL_RABBIT_SLEEPING,
            3, SMALL_RABBIT,
            4, LARGE_RABBIT_FUNGI_SLEEPING,
            5, LARGE_RABBIT_FUNGI,
            6, LARGE_RABBIT_SLEEPING,
            7, LARGE_RABBIT
    );

    private enum Action{
        SLEEP,
        SEEK_SHELTER,
        SEEK_FOOD,
        SEEK_MATE,
        DEFAULT
    }
    //class fields end

    //instance fields begin
    private Burrow burrow;
    private boolean isHiding;

    //instance fields end

    /**
     * Constructor for rabbit with full parameter control.
     * @param age the number of steps this rabbit has been alive for
     * @param sex reproductive female or male
     * @param isAwake if it is awake
     * @param isInfected if it is infected with fungi
     */
    public Rabbit(int age, Sex sex, boolean isAwake, boolean isInfected){
        super(age, sex, isAwake, isInfected);
        /*Rabbits right now are created without a burrow associated with it
        but could be subject to change, if child rabbits are allocated their parents' burrow
         */
        this.burrow = null;
        this.isHiding = false;
        this.actualEnergy = calculateMaxEnergy();

        //set display image
        updateDisplayInformation();
    }

    /**
     * Constructor for Rabbit that doesn't take infection argument.
     * Calls the constructor with full parameters, with isInfected set to false.
     * @param age the number of steps this rabbit has been alive for
     * @param sex reproductive female or male
     * @param isAwake if it is awake
     */
    public Rabbit(int age, Sex sex, boolean isAwake){
        this(age, sex, isAwake, false);
    }

    /**
     * Constructor for Rabbit, that doesn't any arguments, which means the rabbit gets default parameters
     * Calls the constructor with full parameters, with age set to 0, sex set to female, isAwake set to true and isInfected set to false
     */
    public Rabbit() {
        this(0, Sex.FEMALE, true, false);

    }

    /**
     * Calculates the maximum energy level of an animal as a parabolic function,
     * with the vertex representing the age at which the animal is fully matured.
     */
    @Override
    protected double calculateMaxEnergy() {
        return Math.pow(age, 2) + AGE_OF_MATURITY * age + 5;
    }

    /**
     * Method that ages the rabbit and lowers it energy
     * then checks if it is too old or has no energy and kills accordingly
     * @param world the world in which the entity exists.
     */
    @Override
    public void age(World world){
        age++;
        actualEnergy -= DAILY_ENERGY_REDUCTION;
        if(age > MAX_AGE || actualEnergy <= 0){
            die(world);
        }
    }

    /**
     * Returns the Rabbit class field for age at which rabbits are mature adults.
     * @return AGE_OF_MATURITY
     */
    @Override
    public int getAgeOfMaturity() {
        return AGE_OF_MATURITY;
    }

    /**
     * Method to be called by simulator to instruct the rabbit to act and age.
     * @param world providing details of the position on which the entity is currently located and much more.
     */
    @Override
    public void act(World world){
        performAction(world, determineAction(world));
        age(world);
        updateDisplayInformation();
    }

    private Action determineAction(World world){
        if(world.getCurrentTime() >= 12){
            return Action.SLEEP;
        } else if (world.getCurrentTime() >= 8) {
            return Action.SEEK_SHELTER;
        }
        if(actualEnergy < HUNGRY_THRESHOLD) return Action.SEEK_FOOD;
        if(foundPossibleMate(world)) return Action.SEEK_MATE;
        return Action.DEFAULT;
    }

    private void performAction(World world, Action action){
        switch (action){
            case Action.SLEEP:
                sleep();
                break;
            case Action.SEEK_SHELTER:
                throw new UnsupportedOperationException("Not supported yet.");
            case Action.SEEK_FOOD:
                throw new UnsupportedOperationException("Not supported yet.");
            case Action.SEEK_MATE:
                throw new UnsupportedOperationException("Not supported yet.");
            default:
                //stand still
                break;
        }
    }

    private boolean foundPossibleMate(World world){
        Set<Location> tiles = world.getSurroundingTiles(3);
        for(Location loc : tiles){
            if (world.getTile(loc) instanceof Rabbit) {
                if (((Rabbit) world.getTile(loc)).isFemale()) {
                    target = ((Rabbit) world.getTile(loc));
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * Method instructing rabbit to die, deleting it from the world.
     * @param world providing details of the position on which the entity is currently located and much more.
     */
    @Override
    public void die(World world){
        world.delete(this);
    }

    /**
     * Instructs the rabbit to sleep, changing isAwake to false
     */
    @Override
    public void sleep() {
        isAwake = false;
    }

    /**
     * Updates the currentDisplayInformation field of the rabbit.
     * Current implementation is hard to read, but did away with nested if statements
     */
    @Override
    public void updateDisplayInformation() {
        int state = 0;
        if(age >= AGE_OF_MATURITY) state+=4;
        if(!isInfected) state+=2;
        if(isAwake) state += 1;
        currentDisplayInformation = DISPLAY_INFORMATION_MAP.get(state);
    }

}
