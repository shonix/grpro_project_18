package gameOfLife.worldObjects;

import itumulator.world.World;

public interface HomeOwner {
    void enterHome(World world);
    void exitHome(World world);
    AnimalHome getHome();
    void setHome(AnimalHome home);
}
