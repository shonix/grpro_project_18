package gameOfLife.worldObjects.entities;

import gameOfLife.worldObjects.UpdatableDisplayInformation;
import gameOfLife.worldObjects.entities.enums.EntityTypeID;
import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.World;

import java.awt.*;

/**
 * A class representing a bush, which can grow berries and possibly provide sustenance for animals eating it
 */
public class BerryBush extends Plant implements DynamicDisplayInformationProvider, UpdatableDisplayInformation, Edible {
    //class fields begin
    public static final int PROVIDED_SUSTENANCE_WITH_BERRIES = 2, STEPS_TO_REGROW_BERRIES = 20;

    //display options
    private static final Color BERRY_BUSH_COLOR = Color.MAGENTA;
    private static final DisplayInformation BUSH_WITH_BERRIES = new DisplayInformation(BERRY_BUSH_COLOR, "bush-berries");
    private static final DisplayInformation BUSH_NO_BERRIES = new DisplayInformation(BERRY_BUSH_COLOR, "bush");

    //class fields end
    //instance fields begin
    private int stepsSinceLastEaten = 0;
    private DisplayInformation currentDisplayInformation;

    /**
     * Constructor for BerryBush
     * @param age the number of steps this BerryBush has existed
     * @param isEdible is used to represent whether the BerryBush have berries or not.
     */
    public BerryBush(int age, boolean isEdible) {
        super(age, isEdible);
        updateDisplayInformation();
    }

    /**
     * Default constructor for BerryBush, with age 0 (zero) and isEdible true.
     */
    public BerryBush(){
        this(0, true);
    }

    /**
     * Gives the amount of sustenance provided by this BerryBush
     * @return some amount int if isEdible, 0 otherwise.
     */
    @Override
    public int getProvidedSustenance(){
        return isEdible ? PROVIDED_SUSTENANCE_WITH_BERRIES : 0;
    }

    /**
     * Method for when a BerryBush is eaten from. To be used in animal classes when they eat from bush.
     * Method needed if BerryBush has already acted this turn when called upon randomly by simulator, to ensure
     * its state changes appropriately to the act of being eaten.
     */
    public void beEaten(){
        if(!isEdible) throw new IllegalStateException("Animal should not have tried eating from bush without berries!");
        isEdible = false;
        stepsSinceLastEaten = 0;
        updateDisplayInformation();
    }

    /**
     * Method to determine what the BerryBush should display when called by simulator.
     */
    @Override
    public void updateDisplayInformation() {
        currentDisplayInformation = isEdible ? BUSH_WITH_BERRIES : BUSH_NO_BERRIES;
    }

    /**
     * Instructs the BerryBush to act in the world, aging it and changing display information if applicable
     * @param world providing details of the position on which the entity is currently located and much more.
     */
    @Override
    public void act(World world) {
        age(world);
        updateDisplayInformation();
    }

    /**
     * Ages the BerryBush incrementing its age and checks if it has berries (isEdible), if it doesn't it will
     * check if the number of steps since it was last eaten equals or exceeds a global integer to regrow berries and if
     * these conditions are met, it will make it edible once again and reset the counter for number of steps since it
     * was last eaten to 0 (zero)
     * @param world the world in which the entity exists.
     */
    @Override
    public void age(World world){
        super.age(world);
        if(!isEdible && stepsSinceLastEaten >= STEPS_TO_REGROW_BERRIES){
            isEdible = true;
            stepsSinceLastEaten = 0;
        }else{
            stepsSinceLastEaten++;
        }
    }

    /**
     * @param world providing details of the position on which the entity is currently located and much more.
     */
    @Override
    public void die(World world) {
        world.delete(this);
    }

    /**
     * @return EntityTypeID enum associated with a BerryBush, depending on its state.
     */
    @Override
    public EntityTypeID getEntityTypeID() {
        return isEdible ? EntityTypeID.BERRY_BUSH_NO_BERRIES : EntityTypeID.BERRY_BUSH_WITH_BERRIES;
    }

    /**
     * @return DisplayInformation associated with a BerryBush, depending on its state.
     */
    @Override
    public DisplayInformation getInformation() {
        return currentDisplayInformation;
    }

    /**
     * Get the number of steps since the last time the berryBush was eaten
     * @return int stepsSinceLastEaten
     */
    public int getStepsSinceLastEaten() {
        return stepsSinceLastEaten;
    }

    /**
     * Set the number of steps since the last time the BerryBush was eaten.
     * @param stepsSinceLastEaten number of steps since last eaten
     */
    public void setStepsSinceLastEaten(int stepsSinceLastEaten) {
        this.stepsSinceLastEaten = stepsSinceLastEaten;
    }

}
