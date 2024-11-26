package gameOfLife.worldObjects.entities;

import gameOfLife.util.WorldHandler;
import gameOfLife.worldObjects.Burrow;
import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Rabbit extends Animal {
    //class fields begin
    private static final int AGE_OF_MATURITY = 60; //3 simulation days
    private static final int MAX_AGE = 240; // 12 simulation days
    private static final double DAILY_ENERGY_REDUCTION = 0.1;
    private int mateSearchRadius; //TODO consider changing to class constant
    private static final Set<Class<?>> Edibles = Set.of(Grass.class);

    //define on the class, all possible images a rabbit can have
    public static final Color RABBIT_COLOR = new Color(218, 205, 184); //color on top-down world view
    private static final DisplayInformation SMALL_RABBIT = new DisplayInformation(RABBIT_COLOR, "rabbit-small");
    private static final DisplayInformation SMALL_RABBIT_SLEEPING = new DisplayInformation(RABBIT_COLOR, "rabbit-small-sleeping");
    private static final DisplayInformation SMALL_RABBIT_FUNGI = new DisplayInformation(RABBIT_COLOR, "rabbit-fungi-small");
    private static final DisplayInformation SMALL_RABBIT_FUNGI_SLEEPING = new DisplayInformation(RABBIT_COLOR, "rabbit-fungi-small-sleeping");
    private static final DisplayInformation LARGE_RABBIT = new DisplayInformation(RABBIT_COLOR, "rabbit-large");
    private static final DisplayInformation LARGE_RABBIT_SLEEPING = new DisplayInformation(RABBIT_COLOR, "rabbit-sleeping");
    private static final DisplayInformation LARGE_RABBIT_FUNGI = new DisplayInformation(RABBIT_COLOR, "rabbit-fungi");
    private static final DisplayInformation LARGE_RABBIT_FUNGI_SLEEPING = new DisplayInformation(RABBIT_COLOR, "rabbit-fungi-sleeping");
    private static final DisplayInformation LARGE_RABBIT_PREGNANT = new DisplayInformation(RABBIT_COLOR, "rabbit-large-pregnant");
    private static final DisplayInformation LARGE_RABBIT_PREGNANT_SLEEPING = new DisplayInformation(RABBIT_COLOR, "rabbit-large-pregnant-sleeping");
    private static final Map<Integer, DisplayInformation> DISPLAY_INFORMATION_MAP = Map.of(
            0, SMALL_RABBIT_FUNGI_SLEEPING,
            1, SMALL_RABBIT_FUNGI,
            2, SMALL_RABBIT_SLEEPING,
            3, SMALL_RABBIT,
            4, LARGE_RABBIT_FUNGI_SLEEPING,
            5, LARGE_RABBIT_FUNGI,
            6, LARGE_RABBIT_SLEEPING,
            7, LARGE_RABBIT,
            8, LARGE_RABBIT_PREGNANT,
            9, LARGE_RABBIT_PREGNANT_SLEEPING

    );

    //class fields end

    //instance fields begin
    private Burrow burrow;
    private Rabbit currentMate; //TODO consider changing type to Animal and move to Animal class
    private boolean isHiding;
    private boolean isPregnant = false; //TODO consider moving to Animal class
    private Rabbit mother;

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
        this.currentEnergy = calculateMaxEnergy();
        this.mateSearchRadius = 2;
        this.mother = null;
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
     * Return the burrow the rabbit is associated with. If rabbit is not associated with a burrow returns null.
     * @return burrow if not null, otherwise null
     */
    public Burrow getBurrow() {return burrow;}

    /**
     * Sets the burrow of the rabbit to a new burrow
     * @param burrow
     */
    public void setBurrow(Burrow burrow) {this.burrow = burrow;}

    /**
     * Sets the isHiding status
     * @param isHiding boolean
     */
    public void setHiding(boolean isHiding) {this.isHiding = isHiding;}

    /**
     * Returns the isHiding status.
     * @return boolean
     */
    public boolean isHiding() {
        return isHiding;
    }

    /**
     * Calculates the maximum energy level of an animal as a parabolic function,
     * with the vertex representing the age at which the rabbit is fully matured.
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
        super.age(world);
        currentEnergy -= DAILY_ENERGY_REDUCTION;
        if(age > MAX_AGE || currentEnergy <= 0){
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

    /**
     * Method for rabbit to dertermine what action it needs to perform, returning an Action enum corresponding to the
     * first applicable conditions of the rabbit, according to a specific priority of actions
     * @param world in which the rabbit exists
     * @return Action enum signifying what action the rabbit must take
     */
    @Override
    protected Action determineAction(World world){
        if(world.getCurrentTime() >= 12)
            return Action.SLEEP;
        else if (isHiding && world.isNight()) {
            return Action.SLEEP;
        }
        else if (world.getCurrentTime() >= 10)
            return Action.SEEK_SHELTER;

        if(!isAwake)
            return Action.WAKE_UP;

        if(currentEnergy < hungryThreshold)
            return Action.SEEK_FOOD;

        if(!isFemale() && age >= getAgeOfMaturity())
        {
            if(currentMate != null)
            {
                return Action.SEEK_MATE;
            }
            this.currentMate = findMate(world, mateSearchRadius);
        }

        return Action.DEFAULT;
    }

    /**
     * Instructs the rabbit to perform a specific action, calling other methods to do the actual actions.
     * @param world the world in which the Animal exists
     * @param action the action to be taken
     */
    @Override
    protected void performAction(World world, Action action){
        switch (action){
            case Action.SLEEP:
                sleep();
                break;

            case Action.WAKE_UP:
                wakeUp(world);
                if(isPregnant)
                {
                    giveBirth(world);
                }
                break;
            case Action.SEEK_SHELTER:
                seekShelter(world);
            case Action.SEEK_FOOD:
                //seekFood(world);
                break;

            case Action.SEEK_MATE:
                seekMateAndCopulate(world);
                break;

            default:
                if(new Random().nextBoolean())
                    moveActor(world, world.getEmptySurroundingTiles(world.getLocation(this)).stream().toList()); // Move randomly / idle
                break;
        }
    }

    private void seekShelter(World world) {
        if (burrow == null) {
            createBurrow(world);
            hideInBurrow(world, burrow);
        } else if (this.getDistanceToLocation(world, world.getLocation(this), world.getLocation(burrow)) == 0) {
            hideInBurrow(world, burrow);
        } else {
            this.moveActor(world, findNextTileInShortestPath(world, world.getLocation(burrow)));

        }
    }

    private void createBurrow(World world) {
        if (isBurrowOneTile(world)) {
            this.moveActor(world, this.getEmptyAdjacentTiles(world));
        } else if (isGrassOneTile(world)) {
            world.delete(WorldHandler.getClosestOfEntity(world, Grass.class, (this)));
            burrow = (new Burrow(this));
            world.setTile(world.getLocation(this), burrow);
        } else {
            burrow = (new Burrow(3, this));
            world.setTile(world.getLocation(this), burrow);
        }
    }

    private void hideInBurrow(World world, Burrow burrow) {
        if (burrow == null) {
            createHole();
        }
        burrow.addRabbit(this);
        world.remove(this);
        isHiding = true;
    }

    private boolean isGrassOneTile(World world) {
        return WorldHandler.checkIfEntityOnTile(world, world.getLocation(this), Grass.class);
    }

    private boolean isBurrowOneTile(World world) {
        return WorldHandler.checkIfEntityOnTile(world, world.getLocation(this), Burrow.class);
    }

    /**
     * TODO
     */
    private void createHole() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> void seekFood(World world, Class<T> entityType){
        super.seekFood(world, entityType);
    }



    protected void seekFood(World world)
    {
        Edible closestEdible = WorldHandler.getClosestOfEntity(world, Grass.class , this);
        if(closestEdible != null)
        {
            Location locEdible = world.getLocation(closestEdible);
            Location curr = world.getLocation(this);
            if (locEdible.equals(curr))
            {
                eatGrass(world, (Plant) closestEdible);
            }
            else
            {
                List<Location> path = findNextTileInShortestPath(world, world.getLocation(closestEdible));
                world.move(this, path.getFirst());
            }
        }
        else
        {
            if(new Random().nextInt(100) > 80)
                moveActor(world,world.getEmptySurroundingTiles(world.getLocation(this)).stream().toList()); // Move randomly / idle
        }
    }

    /**
     * Will eat a piece of grass, increasing the rabbit's currentEnergy according to the sustenance the grass provides.
     * It will furthermore instruct the piece of grass to die
     * @param world
     * @param grass
     */
    private void eatGrass(World world, Plant grass) {
        if (grass.getProvidedSustenance() + currentEnergy > energyMax) {
            currentEnergy = energyMax;
        } else {
            currentEnergy += grass.getProvidedSustenance();
        }
        grass.die(world);
    }

    /**
     * TODO
     * @param world
     * @param radius
     * @return
     */
    @Override
    protected Rabbit findMate(World world, int radius)
    {
        for(Object o : world.getEntities().keySet())
        {
            if(o instanceof Rabbit rabbit) //I don't like this at all - "Pattern variable". (Might want to ask TA about this).
            {
                if(rabbit.isFemale() && getDistanceFromActorToLocation(world,world.getLocation(o)) <= radius && rabbit.age > rabbit.getAgeOfMaturity()) //Pretty long oneliner.. Basically checks if rabbit is female, and is withing given search radius.
                {
                    return rabbit;
                }
            }
        }
        return null;
    }

    /**
     * TODO
     * @param world
     */
    @Override
    protected void seekMateAndCopulate(World world)
    {
        if(world.getEntities().containsKey(currentMate))
        {
            if(getDistanceFromActorToLocation(world,world.getLocation(currentMate)) <= 1)
            {
                currentMate.setPregnant(true);
                currentMate = null;
            }
            else {
                List<Location> path = findNextTileInShortestPath(world, world.getLocation(currentMate));
                world.move(this, path.getFirst());
            }
        }
        else {
            currentMate = null;
        }
    }

    /**
     * If the rabbit is pregnant and has an empty tile next to it, it will give birth and create a new rabbit.
     * @param world in which the rabbit exists.
     */
    @Override
    protected void giveBirth(World world)
    {
        List<Location> emptySurroundingTiles = world.getEmptySurroundingTiles(world.getLocation(this)).stream().toList();
        if(!emptySurroundingTiles.isEmpty()) {
            world.setTile(emptySurroundingTiles.getFirst(), new Rabbit(0, new Random().nextBoolean()?Animal.Sex.FEMALE: Animal.Sex.MALE, false, this.isInfected));
            isPregnant = false;
        }
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
     * Instructs the rabbit to attempt waking up. If the rabbit is hiding, it will also be asleep and in a whole,
     * therefore it must be checked that it can exit the burrow it is residing in. If it can't nothing happens and in
     * effect the rabbit will stay asleep in its burrow. Otherwise it will either just wake up from the tile it was
     * standing on, or wake up and exit the burrow it was in.
     * @param world the world the rabbit exists in
     */
    @Override
    protected void wakeUp(World world) {
        if(isHiding){
            Set<Location> emptyTiles = world.getSurroundingTiles(world.getLocation(burrow));
            if(world.isTileEmpty(world.getLocation(burrow))) emptyTiles.add(world.getLocation(burrow));
            if(!emptyTiles.isEmpty()){
                exitBurrow(world, emptyTiles);
            }
            //if it is hiding, but no tile is empty, nothing happens this step

        }else{
            isAwake = true;
        }
    }

    /**
     * OVERLOADED METHOD, sets the rabbit on a random tile free tiles around the burrow
     * @param world in which the rabbit exists
     * @param neighbors Set of locations to be set down around. Given as an argument to allow functionality that
     *                  mutates this set.
     */
    public void exitBurrow(World world, Set<Location> neighbors) {
        if(!isHiding) throw new IllegalStateException("Rabbit is not hidden!");
        if(neighbors.isEmpty()) throw new IllegalStateException("There are no available tiles!");
        world.setTile(neighbors.iterator().next(), this);
        burrow.removeRabbit(this);
        isHiding = false;
        isAwake = true;
    }

    /**
     * OVERLOADED METHOD, sets the rabbit on a specified location
     * @param world in which the rabbit exists
     * @param location on which the rabbit is to be set
     */
    public void exitBurrow(World world, Location location) {
        if(!isHiding) throw new IllegalStateException("Rabbit is not hidden!");
        if(!world.isTileEmpty(location)) throw new IllegalStateException("Tile is not available!");
        world.setTile(location, this);
        burrow.removeRabbit(this);
        isHiding = false;
        isAwake = true;
    }

    /**
     * Updates the currentDisplayInformation field of the rabbit.
     * Current implementation is hard to read, but did away with nested if statements
     */
    @Override
    public void updateDisplayInformation() {
        int state = 0;
        if(age >= AGE_OF_MATURITY) state +=4;
        if(!isInfected) state +=2;
        if(isAwake) state += 1;
        if(isPregnant) state +=1;
        if(isPregnant && !isAwake) state +=2;
        currentDisplayInformation = DISPLAY_INFORMATION_MAP.get(state);
    }

}
