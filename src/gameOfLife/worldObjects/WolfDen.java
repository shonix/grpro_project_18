package gameOfLife.worldObjects;

import gameOfLife.worldObjects.entities.Rabbit;
import gameOfLife.worldObjects.entities.Wolf;
import itumulator.world.NonBlocking;

import java.util.HashSet;

public class WolfDen extends AnimalHome implements NonBlocking {
    public int capacity;

    public WolfDen(int capacity)
    {
        if(capacity < 1) throw new IllegalArgumentException("capacity must be greater than 0!");
        this.capacity = capacity;
        this.inhabitants = new HashSet<>();
        this.owners = new HashSet<>();
    }

    public WolfDen(int capacity, Wolf wolf)
    {
        this(capacity);
        owners.add(wolf);
    }

    public WolfDen(Wolf wolf)
    {
        this(3, wolf);
    }

    public WolfDen(){
        this(3);
    }
}
