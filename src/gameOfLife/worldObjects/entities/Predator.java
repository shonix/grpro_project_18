package gameOfLife.worldObjects.entities;

import itumulator.world.World;

public interface Predator {
    public void huntTarget(World world, Animal animal);
    public Animal locateTarget(World world);
    public void attackTarget(World world, Animal animal);
    public void eatCarcass(World world, Carcass carcass);
}
