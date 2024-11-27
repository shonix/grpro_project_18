package gameOfLife.worldObjects.entities;

import gameOfLife.worldObjects.entities.Edible;
import gameOfLife.worldObjects.entities.Entity;
import gameOfLife.worldObjects.entities.enums.EntityTypeID;
import gameOfLife.worldObjects.UpdatableDisplayInformation;
import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.NonBlocking;
import itumulator.world.World;

import java.awt.*;

/**
 * Represents a carcass left by a dead animal. Can rot and be eaten by predators
 */
public class Carcass extends Entity implements DynamicDisplayInformationProvider, UpdatableDisplayInformation, Edible {
    // class fields begin
    // define all possible images for carcasses
    private static final Color carcassColor = new Color(119,54,54); //color picked from carcass image
    private static final DisplayInformation SMALL_CARCASS = new DisplayInformation(carcassColor, "carcass-small");
    private static final DisplayInformation LARGE_CARCASS = new DisplayInformation(carcassColor, "carcass");

    private static final int SMALL_CARCASS_LIMIT = 2,  DETERIORATION_RATE = 10, ROTS_AT_AGE = 60;
    // class fields end

    // instance fields begin
    private int providedSustenance;
    private DisplayInformation currentDisplayInformation;
    private EntityTypeID entityTypeID;

    // instance fields end

    public Carcass(int sustenance, EntityTypeID entityTypeID) {
        super(0);
        if(sustenance <= 0) throw new IllegalArgumentException("sustenance must be positive");
        this.entityTypeID = entityTypeID;
        providedSustenance = sustenance;
        updateDisplayInformation();
    }

    /**
     * Return the providedSustenance of this piece carcass
     * @return providedSustenance integer representing the amount of sustenance provided, when eaten
     */
    @Override
    public int getProvidedSustenance(){return providedSustenance;}

    /**
     * Return the isEdible status
     * @return isEdible boolean
     */
    @Override
    public boolean isEdible() {
        return true;
    }

    /**
     * Ages the carcass, possibly reducing the sustenance it provides
     * @param world the world in which the entity exists.
     */
    @Override
    public void age(World world) {
        super.age(world);
        if(age % DETERIORATION_RATE == 0) providedSustenance--;
    }

    /**
     * Instructs the carcass to act in the world when called upon by Simulator. Represents rotting.
     * @param world providing details of the position on which the entity is currently located and much more.
     */
    @Override
    public void act(World world) {
        age(world);
        if(providedSustenance <= 0 || age >= ROTS_AT_AGE) {
            die(world);
            return;
        }
        updateDisplayInformation();
    }

    /**
     * @param world providing details of the position on which the entity is currently located and much more.
     */
    @Override
    public void die(World world) {
        world.delete(this);
    }

    /**
     * @return
     */
    @Override
    public EntityTypeID getEntityTypeID() {
        return entityTypeID;
    }


    /**
     * Updates the currentDisplayInformation field of the carcass.
     */
    @Override
    public void updateDisplayInformation() {
        if(providedSustenance < SMALL_CARCASS_LIMIT) {
            currentDisplayInformation = SMALL_CARCASS;
        }else{
            currentDisplayInformation = LARGE_CARCASS;
        }
    }

    /**
     * Get the DisplayInformation associated with this instance
     * @return DisplayInformation
     */
    @Override
    public DisplayInformation getInformation() {
        return currentDisplayInformation;
    }
}
