package gameOfLifeTest.entitiesTest;

import gameOfLife.util.ProgramInitializer;
import gameOfLife.worldObjects.Burrow;
import gameOfLife.worldObjects.entities.Grass;
import gameOfLife.worldObjects.entities.Rabbit;
import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class BurrowTest {
    Burrow burrow;
    World world;
    Rabbit rabbit;
    Program program;

    @BeforeEach
    void setUp() {
        burrow = new Burrow();
        rabbit = new Rabbit();
        program = new Program(20, 200, 1);
        world = program.getWorld();
    }

    @AfterEach
    void tearDown() {
        burrow = null;
        rabbit = null;
        world = null;
        program = null;
    }

    /**
     * K1-3a project specification test.
     */
    @Test
    void testBurrowPlacedFromFile(){
        //using ProgramInitialiser to place burrow from a custom test file, expecting a world of size 2 and 1 Burrow.
        ProgramInitializer pi = new ProgramInitializer("test", "K1-3a", 200, 1);
        program = pi.getPrograms().getFirst();
        world = program.getWorld();
        assertEquals(1, world.getEntities().size()); //assert the world has exactly one entity
        assertInstanceOf(Burrow.class, world.getEntities().keySet().stream().toList().getFirst()); //assert the element on the map is a Burrow
    }

    /**
     * K1-3b project specification test.
     * Test that a rabbit can dig a new burrow
     */
    @Test
    void k1_3b_testBurrowDugByRabbit(){
        world.setTile(new Location(0,0),rabbit);
        assertNull(rabbit.getHome()); //assert rabbit does not have a burrow already.
        for(int i = 0; i<20; i++){
            program.simulate();
        }
        assertNotNull(rabbit.getHome()); //assert that the rabbit now has a burrow which means it must have dug it.
    }
    /**
     * K1-3c project specification test.
     * Test that an animal can stand on top of the Burrow
     */
    @Test
    void testBurrowStandOn(){

        Location burrowLocation = new Location(2,2);
        Location rabbitLocation = new Location(1,2);
        world.setCurrentLocation(rabbitLocation);
        world.setTile(burrowLocation, burrow);
        world.setTile(rabbitLocation, rabbit);

        assertInstanceOf(Rabbit.class, world.getTile(rabbitLocation)); //assert that a rabbit inhabits rabbitLocation
        assertInstanceOf(Burrow.class, world.getTile(burrowLocation)); //assert that burrow inhabits burrowLocation
        assertDoesNotThrow(()-> {rabbit.moveActor(world, Arrays.asList(burrowLocation));}); //assert movement does not throw an exception
        assertInstanceOf(Rabbit.class, world.getTile(burrowLocation)); //assert rabbit has moved to new tile. getTile gets blocking first.
        assertNull(world.getTile(rabbitLocation)); //assert that rabbit does not inhabit original location.

    }

}
