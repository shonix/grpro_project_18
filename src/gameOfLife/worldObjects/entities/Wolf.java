package gameOfLife.worldObjects.entities;

import gameOfLife.util.WorldHandler;
import gameOfLife.worldObjects.AnimalHome;
import gameOfLife.worldObjects.HomeOwner;
import gameOfLife.worldObjects.WolfDen;
import gameOfLife.worldObjects.entities.enums.Action;
import gameOfLife.worldObjects.entities.enums.EntityTypeID;
import gameOfLife.worldObjects.entities.enums.Sex;
import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;

import java.awt.*;
import java.util.List;
import java.util.Random;
import java.util.Set;


public class Wolf extends Animal implements HomeOwner, Predator{
    //class fields begin
    private static final int AGE_OF_MATURITY = 40;
    private static final int MATE_SEARCH_RADIUS = 2;
    private static final int SMALL_CARCASS_SUSTENANCE = 5;
    private static final int LARGE_CARCASS_SUSTENANCE = 10;
    //Set up possible displayinformation
    private static final Color WOLF_COLOR = Color.GRAY;
    private static final DisplayInformation WOLF_SMALL = new DisplayInformation(WOLF_COLOR, "wolf-small");
    private static final DisplayInformation WOLF_SMALL_SLEEPING = new DisplayInformation(WOLF_COLOR, "wolf-small-sleeping");
    private static final DisplayInformation WOLF_FUNGI_SMALL = new DisplayInformation(WOLF_COLOR, "wolf-fungi-small");
    private static final DisplayInformation WOLF_FUNGI_SMALL_SLEEPING = new DisplayInformation(WOLF_COLOR, "wolf-fungi-small-sleeping");
    private static final DisplayInformation WOLF = new DisplayInformation(WOLF_COLOR, "wolf");
    private static final DisplayInformation WOLF_SLEEPING = new DisplayInformation(WOLF_COLOR, "wolf-sleeping");
    private static final DisplayInformation WOLF_FUNGI = new DisplayInformation(WOLF_COLOR, "wolf-fungi");
    private static final DisplayInformation WOLF_FUNGI_SLEEPING = new DisplayInformation(WOLF_COLOR, "wolf-fungi-sleeping");

    public boolean insideDen = false;
    public Wolf mother = null;
    private Wolf alpha;
    public AnimalHome wolfDen = null;


    public Wolf(int age, Sex sex, boolean isAwake, boolean isInfected)
    {
        super(age, sex, isAwake, isInfected);

        this.currentEnergy = calculateMaxEnergy();
        this.mother = null;

        //set display image
        updateDisplayInformation();
    }

    @Override
    protected void performAction(World world, Action action) {
        switch (action) {
            case Action.SLEEP:
                sleep();
                break;

            case Action.WAKE_UP:
                wakeUp(world);
                break;

//            case Action.SEEK_ALPHA:
//                seekAlpha(alpha);
//                break;

            case Action.SEEK_SHELTER:
                seekShelter(world);
                break;

            case Action.SEEK_FOOD:
                seekFood(world);
                break;

            case Action.SEEK_MATE:
                seekMateAndCopulate(world);
                break;

            default:
                if (new Random().nextBoolean())moveActor(world, world.getEmptySurroundingTiles(world.getLocation(this)).stream().toList()); // Move randomly / idle
                break;
        }
    }

    @Override
    protected Action determineAction(World world)
    {
        if (world.getCurrentTime() >= 16)
        {
            return Action.SLEEP;
        }

        if (insideDen && world.isNight())
        {
            return Action.SLEEP;
        }

        if (world.getCurrentTime() >= 13)
        {
            return Action.SEEK_SHELTER;
        }

        if (!isAwake)
        {
            return Action.WAKE_UP;
        }

        if (currentEnergy < hungryThreshold)
        {
            return Action.SEEK_FOOD;
        }

        if (!isFemale() && age >= getAgeOfMaturity())
        {
            return Action.SEEK_MATE;
        }

        return Action.DEFAULT;
    }

    @Override
    protected double calculateMaxEnergy()
    {
        return Math.pow(age, 2) + AGE_OF_MATURITY * age + 5;
    }

    @Override
    public int getAgeOfMaturity()
    {
        return AGE_OF_MATURITY;
    }

    @Override
    public void updateDisplayInformation()
    {
        if(age < AGE_OF_MATURITY){
            if(isAwake){
                currentDisplayInformation = isInfected ? WOLF_FUNGI_SMALL : WOLF_SMALL;
            }else{
                currentDisplayInformation = isInfected ? WOLF_FUNGI_SMALL_SLEEPING : WOLF_SMALL_SLEEPING;
            }
        }else{
            if(isAwake){
                currentDisplayInformation = isInfected ? WOLF_FUNGI : WOLF;
            }else{
                currentDisplayInformation = isInfected ? WOLF_FUNGI_SLEEPING : WOLF_SLEEPING;
            }
        }
    }

    @Override
    public void sleep() {
        isAwake = false;
    }

    @Override
    protected Animal findMate(World world)
    {
        Set<Wolf> setOfPotentialMates = WorldHandler.getEntitiesByType(world, Wolf.class);

        for(Wolf wolf : setOfPotentialMates)
        {
            if(wolf.getSex() == Sex.FEMALE)
            {
                if(getDistanceToLocation(world, world.getLocation(this), world.getLocation(wolf))<= MATE_SEARCH_RADIUS)
                {
                    setOfPotentialMates.add(wolf);
                }
            }
        }

        if (!setOfPotentialMates.isEmpty()) {
            return WorldHandler.getClosestOfEntityFromList(world, setOfPotentialMates, this);
        }

        return null;
    }

    @Override
    protected void seekMateAndCopulate(World world)
    {
        if (world.getEntities().containsKey(currentMate))
        {
            if (getDistanceFromActorToLocation(world, world.getLocation(currentMate)) <= 1)
            {
                currentMate.setPregnant();
                currentMate = null;
            }

            else
            {
                this.moveActor(world, findNextTileInShortestPath(world, world.getLocation(currentMate)));
            }
        }

        else
        {
            currentMate = findMate(world);
            if (currentMate != null)
            {
                this.moveActor(world, findNextTileInShortestPath(world, world.getLocation(currentMate)));
            }
        }
    }

    @Override
    protected void seekShelter(World world) {
        if (wolfDen == null) {
            createWolfDen(world);
            hideInWolfDen(world, wolfDen);
        } else if (this.getDistanceToLocation(world, world.getLocation(this), world.getLocation(wolfDen)) <= 0) {
            hideInWolfDen(world, wolfDen);
        } else {
            this.moveActor(world, findNextTileInShortestPath(world, world.getLocation(wolfDen)));

        }
    }

    @Override
    protected void seekFood(World world)
    {
        Carcass closestEdible = WorldHandler.getClosestOfEntity(world, Carcass.class, this);
        if (closestEdible != null) {
            Location locEdible = world.getLocation(closestEdible);
            Location curr = world.getLocation(this);
            if (locEdible.equals(curr))
            {
                eatCarcass(world, closestEdible);
            }

            else
            {
                List<Location> path = findNextTileInShortestPath(world, world.getLocation(closestEdible));
                world.move(this, path.getFirst());
            }
        }
        else
        {
            Animal target = locateTarget(world);
            if(target != null)
            {

                if (!(getDistanceFromActorToLocation(world, world.getLocation(target)) <= 1))
                {
                    huntTarget(world, target);
                }
                else
                {
                    attackTarget(world, target);
                }
                return;
            }

            if (new Random().nextInt(100) > 80)
            {
                moveActor(world, world.getEmptySurroundingTiles(world.getLocation(this)).stream().toList()); // Move randomly / idle
            }
        }
    }

    @Override
    protected void giveBirth(World world)
    {
        List<Location> emptySurroundingTiles = world.getEmptySurroundingTiles(world.getLocation(this)).stream().toList();
        if (!emptySurroundingTiles.isEmpty())
        {
            world.setTile(emptySurroundingTiles.getFirst(), new Wolf(0, new Random().nextBoolean() ? Sex.FEMALE : Sex.MALE, false, this.isInfected));
            isPregnant = false;
        }
    }

    @Override
    public void setPregnant() {
        this.isPregnant = true;
    }

    @Override
    protected void wakeUp(World world)
    {
        if (insideDen)
            {
                Set<Location> emptyTiles = world.getSurroundingTiles(world.getLocation(wolfDen));

                if (world.isTileEmpty(world.getLocation(wolfDen)))
                {
                    emptyTiles.add(world.getLocation(wolfDen));
                }

                if (!emptyTiles.isEmpty())
                {
                    wolfDen.exitHome(world, emptyTiles, this);
                    insideDen = false;
                }
            }

        else
        {
            isAwake = true;
        }

        if (isPregnant)
        {
            giveBirth(world);
        }
    }

    @Override
    public void act(World world) {
        performAction(world, determineAction(world));
        age(world);
        updateDisplayInformation();
    }

    @Override
    public void die(World world) {
        if(world.isOnTile(this)){
            Carcass carcass = new Carcass(age < AGE_OF_MATURITY ? SMALL_CARCASS_SUSTENANCE : LARGE_CARCASS_SUSTENANCE, EntityTypeID.CARCASS_WOLF);
            Location location = world.getLocation(this);
            world.delete(this);
            world.setTile(location, carcass);
        }else{
            world.delete(this);
        }
    }

    @Override
    public EntityTypeID getEntityTypeID() {
        return EntityTypeID.WOLF;
    }

    @Override
    public AnimalHome getHome() {
        return wolfDen;
    }

    @Override
    public void setHome(AnimalHome home) {
        this.wolfDen = home;
    }

    @Override
    public void huntTarget(World world, Animal animal) {
        this.moveActor(world, findNextTileInShortestPath(world, world.getLocation(animal)));
    }

    @Override
    public Animal locateTarget(World world) {
        return WorldHandler.getClosestOfEntity(world, Rabbit.class, this);
    }

    @Override
    public void attackTarget(World world, Animal animal) {
        if(getDistanceFromActorToLocation(world, world.getLocation(this)) <= 1)
        {
            animal.die(world);
        }
    }

    @Override
    public void eatCarcass(World world, Carcass carcass) {
        if (carcass.getProvidedSustenance() + currentEnergy > energyMax) {
            currentEnergy = energyMax;
        } else {
            currentEnergy += carcass.getProvidedSustenance();
        }
        carcass.die(world);
    }

    private void createWolfDen(World world) {
        if (isGrassOnTile(world))
        {
            world.delete(WorldHandler.getClosestOfEntity(world, Grass.class, (this)));
            wolfDen = (new WolfDen(this));
            world.setTile(world.getLocation(this), wolfDen);
        }

        else if (isNonBlockingOnTile(world))
        {
            this.moveActor(world, this.getEmptyAdjacentTiles(world));
        }

        else
        {
            wolfDen = (new WolfDen(3, this));
            world.setTile(world.getLocation(this), wolfDen);
        }
    }

    private void hideInWolfDen(World world, AnimalHome wolfDen) {
        if (wolfDen == null) {
            createWolfDen(world);
        }
        if (wolfDen != null) {
            this.wolfDen.enterHome(world, this);
            insideDen = true;
        }

    }

    /**
     * TODO: Duplicated code, move to AnimalHome or Animal
     * @param world
     * @return
     */
    private boolean isNonBlockingOnTile(World world) {
        Location location = world.getLocation(this);
        return world.containsNonBlocking(location);
    }

    /**
     * TODO: Duplicated code, move to AnimalHome or Animal
     * @param world
     * @return
     */
    private boolean isGrassOnTile(World world) {
        return WorldHandler.checkIfEntityOnTile(world, world.getLocation(this), Grass.class);
    }
}
