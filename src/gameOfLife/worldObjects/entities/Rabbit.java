package gameOfLife.worldObjects.entities;

import gameOfLife.worldObjects.Burrow;
import itumulator.executable.DisplayInformation;
import itumulator.world.World;

import java.awt.*;
import java.util.Map;

public class Rabbit extends Animal {
    //class fields begin
    public static final double AGE_OF_MATURITY = 60;
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
            0, SMALL_RABBIT_FUNGI_SLEEPING, 1, SMALL_RABBIT_FUNGI,
            2, SMALL_RABBIT_SLEEPING, 3, SMALL_RABBIT,
            4, LARGE_RABBIT_FUNGI_SLEEPING, 5, LARGE_RABBIT_FUNGI,
            6, LARGE_RABBIT_SLEEPING, 7, LARGE_RABBIT
    );
    //class fields end

    //instance fields begin
    private Burrow burrow;
    private boolean isHiding;

    //instance fields end


    public Rabbit(int age, Sex sex, boolean isAwake, boolean isInfected){
        super(age, sex, isAwake, isInfected);
        /*Rabbits right now are created without a burrow associated with it
        but could be subject to change, if child rabbits are allocated their parents' burrow
         */
        this.burrow = null;
        this.isHiding = false;

        //set display image
        updateDisplayInformation();
    }

    public Rabbit(int age, Sex sex, boolean isAwake){
        this(age, sex, isAwake, false);
    }

    public Rabbit() {
        this(0, Sex.FEMALE, true, false);

    }

    /**
     * Calculates the maximum energy level of an animal as a parabolic function,
     * with the vertex representing the age at which the animal is fully matured.
     */
    @Override
    protected double calculateMaxEnergy(double age) {
        return Math.pow(age, 2) + AGE_OF_MATURITY * age;
    }

    @Override
    public void age(World world){

    }

    @Override
    public int getAgeOfMaturity() {
        return 0;
    }

    @Override
    public void act(World world){

    }

    @Override
    public void die(World world){
        world.delete(this);
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