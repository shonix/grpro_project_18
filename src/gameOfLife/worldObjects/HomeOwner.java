package gameOfLife.worldObjects;

import itumulator.world.World;

public interface HomeOwner {
    AnimalHome getHome();
    void setHome(AnimalHome home);
}
