package gameOfLife.worldObjects.entities;

import gameOfLife.util.WorldHandler;
import gameOfLife.worldObjects.entities.enums.Action;
import gameOfLife.worldObjects.entities.enums.EntityTypeID;
import gameOfLife.worldObjects.entities.enums.Sex;
import itumulator.world.Location;
import itumulator.world.World;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;


public class Bear extends Animal implements Predator {

    private final int ageOfMaturity = 48;



    private Set<Location> territory;

    public Bear(int age, Sex sex, boolean isAwake, boolean isInfected) {
        super(age, sex, isAwake, isInfected);
    }


    public void createTerritory(World world){
        if (WorldHandler.getEntitiesByType(world, Bear.class).isEmpty()) {
            world.setTile(findValidBearSpawn(world), this);
            territory = world.getSurroundingTiles(WorldHandler.getRandomValidLocation(world));
        } else {
            territory = world.getSurroundingTiles(findValidBearSpawn(world));
            world.setTile(findValidBearSpawn(world), this);
        }
    }


    private Location findValidBearSpawn(World world) {
        Set<Location> setOfBearTerritories = WorldHandler.getEntitiesByType(world, Bear.class)
                                                         .stream()
                                                         .flatMap(bear -> bear.getTerritory().stream()) //create a stream for each territory
                                                         .collect(Collectors.toSet());

        if (setOfBearTerritories.size() >= ((world.getSize() + 1)  * (world.getSize() + 1))) { //guards for the entire world being inhabited by bears
            return null;
        }

        Location newLocation = WorldHandler.getRandomLocation(world);

        while ((setOfBearTerritories.stream().noneMatch(newLocation::equals))) {
          newLocation = WorldHandler.getRandomLocation(world);
        }
        return newLocation;
    }






    @Override
    protected double calculateMaxEnergy() {
        return Math.pow(age, 2) + ageOfMaturity * age + 5;
    }


    @Override
    public int getAgeOfMaturity() {
        return ageOfMaturity;
    }

    @Override
    public void updateDisplayInformation() {

    }

    @Override
    public void sleep() {

    }

    @Override
    protected void performAction(World world, Action action) {

    }

    @Override
    protected Action determineAction(World world) {
        return null;
    }

    @Override
    protected Animal findMate(World world) {
        return null;
    }

    @Override
    protected void seekMateAndCopulate(World world) {

    }

    @Override
    protected void seekShelter(World world) {

    }

    @Override
    protected void seekFood(World world) {

    }

    @Override
    protected void giveBirth(World world) {

    }

    @Override
    public void setPregnant() {

    }

    @Override
    protected void wakeUp(World world) {

    }

    @Override
    public void act(World world) {

    }

    @Override
    public void die(World world) {

    }

    @Override
    public EntityTypeID getEntityTypeID() {
        return null;
    }

    @Override
    public void huntTarget(World world, Animal animal) {

    }

    @Override
    public Animal locateTarget(World world) {
        return null;
    }

    @Override
    public void attackTarget(World world, Animal animal) {

    }

    @Override
    public void eatCarcass(World world, Carcass carcass) {

    }

    public Set<Location> getTerritory() {
        return territory;
    }

    public void setTerritory(Set<Location> territory) {
        territory = territory;
    }

}
