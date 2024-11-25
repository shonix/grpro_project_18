package gameOfLifeTest.entitiesTest;

import gameOfLife.util.DataHandler;
import gameOfLife.util.ProgramInitializer;
import gameOfLife.util.WorldHandler;
import gameOfLife.worldObjects.Burrow;
import gameOfLife.worldObjects.entities.Entity;
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
    void k1_1a_testGrassPlacedFromFile(){
        //using ProgramInitialiser to place grass from a custom test file, expecting a world of size 2 and 1 grass.
        ProgramInitializer pi = new ProgramInitializer("test", "K1-1a", 200, 1);
        program = pi.getPrograms().getFirst();
        world = program.getWorld();
        assertEquals(1, world.getEntities().size()); //assert the world has exactly one entity
        assertInstanceOf(Grass.class, world.getEntities().keySet().stream().toList().getFirst()); //assert the entity is of type Grass
    }

    /**
     * K1-1b project specification test
     * Grass can spread test. Although it actually cannot be guaranteed that grass will be spread, as it only
     * has a certain chance to spread, this test runs a large amount of steps, always preventing the Grass entities
     * from dying, it would be incredibly unlikely that there wouldn't be created new pieces of grass.
     */
    @Test
    void k1_1b_testGrassSpread_chance(){
        world.setTile(new Location(0,0), grass);
        world.setCurrentLocation(new Location(0,0));
        assertEquals(1, world.getEntities().size());
        for(int i = 0; i < Integer.MAX_VALUE; i++){
            if(world.getEntities().size() > 1) break;
            program.simulate();
            for(Object o : world.getEntities().keySet()){
                if (o instanceof Grass) ((Grass) o).setAge(0);
            }
        }
        assertTrue(world.getEntities().size() > 1);
    }

    /**
     * Animals can stand on grass test. Tests that an animal can move to a location with a piece of grass.
     */
    @Test
    void k1_1c_testGrassStandOn(){
        Location grassLocation = new Location(2,2);
        Location rabbitLocation = new Location(1,2);
        world.setCurrentLocation(rabbitLocation);
        Rabbit rabbit = new Rabbit();
        world.setTile(grassLocation, grass);
        world.setTile(rabbitLocation, rabbit);

        assertInstanceOf(Rabbit.class, world.getTile(rabbitLocation)); //assert that a rabbit inhabits rabbitLocation
        assertInstanceOf(Grass.class, world.getTile(grassLocation)); //assert that grass inhabits grassLocation
        assertDoesNotThrow(()-> {rabbit.moveActor(world, Arrays.asList(grassLocation));}); //assert movement does not throw an exception
        assertInstanceOf(Rabbit.class, world.getTile(grassLocation)); //assert rabbit has moved to new tile. getTile gets blocking first.
        assertNull(world.getTile(rabbitLocation)); //assert that rabbit does not inhabit original location.
    }

}
