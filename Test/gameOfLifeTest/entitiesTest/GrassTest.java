package gameOfLifeTest.entitiesTest;

import gameOfLife.util.DataHandler;
import gameOfLife.util.WorldHandler;
import gameOfLife.worldObjects.entities.Grass;
import gameOfLife.worldObjects.entities.Rabbit;
import itumulator.display.Canvas;
import itumulator.executable.Program;
import itumulator.simulator.Simulator;
import itumulator.world.Location;
import itumulator.world.World;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests on the Grass class. Several tests have been made and tests for project specifications K1-1a-c are also included
 */
public class GrassTest {
    Grass grass;
    World world;
    Program program;
    int typicalFood = Grass.MAX_PROVIDED_SUSTENANCE - Grass.MIN_PROVIDED_SUSTENANCE;


    @BeforeEach
    public void setUp() {
        grass = new Grass(1);
        program = new Program(20, 800, 1);
        world = program.getWorld();
    }

    @AfterEach
    public void tearDown() {
        grass = null;
        world = null;
        program = null;
    }

    /**
     * Tests instantiation of Grass objects
     */
    @Test
    public void testGrass() {
        //test min-1
        assertThrows(IllegalArgumentException.class, () -> {
            new Grass(Grass.MIN_PROVIDED_SUSTENANCE-1);
        });
        //test max+1
        assertThrows(IllegalArgumentException.class, () -> {
            new Grass(Grass.MAX_PROVIDED_SUSTENANCE +1);
        });
        //test max
        assertDoesNotThrow(() -> { new Grass(Grass.MAX_PROVIDED_SUSTENANCE); });
        //test min
        assertDoesNotThrow(() -> { new Grass(Grass.MIN_PROVIDED_SUSTENANCE); });
        //test typical value
        assertDoesNotThrow(() -> { new Grass(typicalFood); });
    }

    @Test
    public void testAge() {
        assertEquals(0, grass.getAge());
        assertEquals(1, grass.getProvidedSustenance());

        World world = new World(3);

        for(int i = 1; i < Grass.MAX_PROVIDED_SUSTENANCE; i++){
            grass.age(world);
            assertEquals(i+1, grass.getProvidedSustenance());
            assertEquals(i, grass.getAge());
        }

        grass.age(world);
        assertEquals(Grass.MAX_PROVIDED_SUSTENANCE, grass.getProvidedSustenance());
        grass.age(world);
        assertEquals(Grass.MAX_PROVIDED_SUSTENANCE, grass.getProvidedSustenance());
    }

    /**
     * K1-1a project specification test.
     * Grass placed from input file on a random location.
     */
    @Test
    void testGrassPlacedFromFile(){
        //TODO
    }

    /**
     * K1-1b project specification test
     * Grass can spread
     */
    @Test
    void k1_1b_testGrassSpread_chance(){
        int numberOfGrassCreated = 0;
        int numberOfGrassSpreadOpportunities = 0;
        Set<Grass> grasses;
        world.setTile(new Location(world.getSize()/2, world.getSize()/2), grass); //set grass in the middle of world.
        for(int i = 0; i<100000; i++){
            grasses = world.getAll(Grass.class, WorldHandler.getAllTiles(world));
            for(Grass grass : grasses){
                numberOfGrassSpreadOpportunities += world.getEmptySurroundingTiles(world.getLocation(grass)).size();
                grass.act(world);
                numberOfGrassCreated +=
            }
        }
    }
    /**
     * K1-1b project specification test
     * Grass can spread
     */
    @Test
    void testGrassSpread2(){
        //TODO
    }
    /**
     * K1-1b project specification test
     * Grass can spread
     */
    @Test
    void testGrassSpread3(){
        //TODO
    }

    /**
     * Animals can stand on grass test. Tests that an animal can move to a location with a piece of grass.
     */
    @Test
    void k1_1c_testGrassStandOn(){
        Location burrowLocation = new Location(2,2);
        Location rabbitLocation = new Location(1,2);
        world.setCurrentLocation(rabbitLocation);
        Rabbit rabbit = new Rabbit();
        world.setTile(burrowLocation, grass);
        world.setTile(rabbitLocation, rabbit);

        assertInstanceOf(Rabbit.class, world.getTile(rabbitLocation)); //assert that a rabbit inhabits rabbitLocation
        assertInstanceOf(Grass.class, world.getTile(burrowLocation)); //assert that burrow inhabits burrowLocation
        assertDoesNotThrow(()-> {rabbit.moveActor(world, Arrays.asList(burrowLocation));}); //assert movement does not throw an exception
        assertInstanceOf(Rabbit.class, world.getTile(burrowLocation)); //assert rabbit has moved to new tile. getTile gets blocking first.
        assertNull(world.getTile(rabbitLocation)); //assert that rabbit does not inhabit original location.

    }

}
