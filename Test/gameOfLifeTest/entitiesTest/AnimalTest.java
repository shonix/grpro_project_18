package gameOfLifeTest.entitiesTest;

import gameOfLife.worldObjects.entities.Animal;
import gameOfLife.worldObjects.entities.Rabbit;
import itumulator.world.Location;
import itumulator.world.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AnimalTest {
    World world;
    Animal animal;
    Location initialLocation;

    @BeforeEach
    public void setUp() {
       world = new World(15);
    }


    private void setupAnimalInWorld() {
        animal = new Rabbit();
        initialLocation = new Location(0, 0);
        world.setDay();
        world.setCurrentLocation(initialLocation);
        world.setTile(initialLocation, animal);
    }


    @Test
    void pathToLocation_actorMoves_actorMovesAsExpected() {
        setupAnimalInWorld();
        animal.moveActor(world, animal.findNextTileInShortestPath(world, (new Location(10, 10))));
        assert(world.getLocation(animal).getX() == 1 && world.getLocation(animal).getY() == 1);
    }

    @Test
    void getDistanceFromActorToLocation_distanceCheck_distanceIsAccurate() {
        setupAnimalInWorld();
        assertEquals(3, animal.getDistanceFromActorToLocation(world, (new Location(3,3))));
        assertEquals(1, animal.getDistanceFromActorToLocation(world, (new Location(1,1))));
        assertEquals(0, animal.getDistanceFromActorToLocation(world, (new Location(0,0))));
        animal.moveActor(world, List.of(new Location(1,0)));
        assertEquals(3, animal.getDistanceFromActorToLocation(world, (new Location(3,3))));
        animal.moveActor(world, List.of(new Location(1,1)));
        assertEquals(2, animal.getDistanceFromActorToLocation(world, (new Location(3,3))));

    }
}

