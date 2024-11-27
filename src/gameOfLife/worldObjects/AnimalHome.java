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
    protected Set<HomeOwner> inhabitants;
    protected Set<HomeOwner> owners;
    protected EntityTypeID type;

    public AnimalHome() {
        this.inhabitants = new HashSet<>();
        this.owners = new HashSet<>();
    }

    public AnimalHome(HomeOwner animal){
        this();
        this.owners.add(animal);
    }

    public AnimalHome(Set<HomeOwner> animals){
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
            HomeOwner animal = this.getInhabitants().iterator().next();
            Location location = emptyTiles.iterator().next();
            world.setTile(location, animal);
            this.removeInhabitants(animal);
        }

        //kill remaining animals in home that could not be placed
        for(HomeOwner animal : this.getInhabitants()){
            world.delete(animal);
        }

        world.delete(this);
    }


    public Set<HomeOwner> getInhabitants() {
        return inhabitants;
    }

    public Set<HomeOwner> getOwners() {
        return owners;
    }

    public void addInhabitants(Set<HomeOwner> inhabitants) {
        this.inhabitants.addAll(inhabitants);
    }

    public void addInhabitants(HomeOwner animal) {
        this.inhabitants.add(animal);
    }

    public void removeInhabitants(Set<HomeOwner> inhabitants) {
        this.inhabitants.removeAll(inhabitants);
    }

    public void removeInhabitants(HomeOwner animal) {
        this.inhabitants.remove(animal);
    }

    public void addOwners(Set<HomeOwner> owners){
        this.owners.addAll(owners);
    }

    public void addOwners(HomeOwner animal) {
        this.owners.add(animal);
    }

    public void removeOwners(Set<HomeOwner> owners){
        this.owners.removeAll(owners);
    }

    public void removeOwners(HomeOwner animal) {
        this.owners.remove(animal);
    }


}
