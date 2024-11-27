package gameOfLife.worldObjects;

import gameOfLife.util.WorldHandler;
import gameOfLife.worldObjects.entities.Animal;
import gameOfLife.worldObjects.entities.enums.EntityTypeID;
import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

import java.util.HashSet;
import java.util.Set;

public abstract class AnimalHome implements NonBlocking {
    protected Set<Animal> inhabitants;
    protected Set<Animal> owners;
    protected EntityTypeID type;

    public AnimalHome() {
        this.inhabitants = new HashSet<>();
        this.owners = new HashSet<>();
    }

    public AnimalHome(Animal animal){
        this();
        this.owners.add(animal);
    }

    public AnimalHome(Set<Animal> animals){
        this();
        this.inhabitants.addAll(animals);
    }

    /**
     * Destroys the Burrow, deleting it from the world and attempts to place any rabbits inside around Burrow. Any
     * not placed are killed. All rabbits associated with burrow gets their burrow set to null
     * @param world
     */
    public void destroy(World world){
        Location thisTile = world.getLocation(this);
        Set<Location> emptyTiles = world.getEmptySurroundingTiles(thisTile);
        if(world.isTileEmpty(thisTile)) emptyTiles.add(thisTile);

        this.removeOwners(this.getInhabitants()); //remove home from every animal associated with this home
        int toPlace = Math.min(emptyTiles.size(), this.getInhabitants().size());

        //Place as many animals as possible
        for(int i = 1; i <= toPlace; i++){
            Animal animal = this.getInhabitants().iterator().next();
            Location location = emptyTiles.iterator().next();
            world.setTile(location, animal);
            this.removeInhabitants(animal);
        }

        //kill remaining animals in home that could not be placed
        for(Animal animal : this.getInhabitants()){
            world.delete(animal);
        }

        world.delete(this);
    }

    public void exitHome(World world, Set<Location> tiles, Animal animal){
        if(tiles.isEmpty()) throw new IllegalStateException("Animal shouldn't have tried to exit when no tile is available!");
        if(!inhabitants.contains(animal)) throw new IllegalStateException("Animal not home!");
        inhabitants.remove(animal);
        world.setTile(tiles.stream().toList().getFirst(), animal);
        animal.setAwake(true);
    }

    public void enterHome(World world, Animal animal){
        if(!world.isOnTile(animal)) throw new IllegalStateException("Animal not on map!");
        inhabitants.add(animal);
        world.remove(animal);
    }

    public Set<Animal> getInhabitants() {
        return inhabitants;
    }

    public Set<Animal> getOwners() {
        return owners;
    }

    public void addInhabitants(Set<Animal> inhabitants) {
        this.inhabitants.addAll(inhabitants);
    }

    public void addInhabitants(Animal animal) {
        this.inhabitants.add(animal);
    }

    public void removeInhabitants(Set<Animal> inhabitants) {
        this.inhabitants.removeAll(inhabitants);
    }

    public void removeInhabitants(Animal animal) {
        this.inhabitants.remove(animal);
    }

    public void addOwners(Set<Animal> owners){
        this.owners.addAll(owners);
    }

    public void addOwners(Animal animal) {
        this.owners.add(animal);
    }

    public void removeOwners(Set<Animal> owners){
        this.owners.removeAll(owners);
    }

    public void removeOwners(Animal animal) {
        this.owners.remove(animal);
    }


}
