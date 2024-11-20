package gameOfLifeTest.entitiesTest;

import gameOfLife.entities.Animal;
import itumulator.world.Location;
import itumulator.world.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AnimalTest {
    World world;
    Animal animal;
    Location initialLocation;

    @BeforeEach
    public void setUp() {
       world = new World(15);
    }


    private void setupAnimalInWorld() {
        animal = new Animal(1, Animal.Sex.MALE, 2);
        initialLocation = new Location(0, 0);
        world.setDay();
        world.setCurrentLocation(initialLocation);
        world.setTile(initialLocation, animal);
    }


    @Test
    void pathToLocation_actorMoves_actorMovesAsExpected() {
        setupAnimalInWorld();
        animal.pathToLocation(world, (new Location(10,10)));
        assert(world.getLocation(animal).getX() == 1 && world.getLocation(animal).getY() == 0 ||
                world.getLocation(animal).getX() == 0 && world.getLocation(animal).getY() == 1 ||
                world.getLocation(animal).getX() == 1 && world.getLocation(animal).getY() == 1);

    }

    @Test
    void getDistanceFromActorToLocation_distanceCheck_distanceIsAccurate() {
        setupAnimalInWorld();
        assertEquals(animal.getDistanceFromActorToLocation((new Location(0,1)), world), 1);
        assertEquals(animal.getDistanceFromActorToLocation((new Location(10,10)), world), 10);

    }
}

