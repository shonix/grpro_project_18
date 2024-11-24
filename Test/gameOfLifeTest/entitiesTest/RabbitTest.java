package gameOfLifeTest.entitiesTest;

import gameOfLife.worldObjects.Burrow;
import gameOfLife.worldObjects.entities.Rabbit;
import itumulator.world.Location;
import itumulator.world.World;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RabbitTest {
    private Rabbit r1, r2, r3, r4, r5;
    private Burrow burrow;
    private World world;

    @BeforeEach
    void setUp() {
        r1 = new Rabbit();
        r2 = new Rabbit();
        r3 = new Rabbit();
        r4 = new Rabbit();
        r5 = new Rabbit();
        burrow = new Burrow(2);
        world = new World(5);
    }

    @AfterEach
    void tearDown() {
        r1 = null;
        r2 = null;
        r3 = null;
        r4 = null;
        r5 = null;
        burrow = null;
        world = null;
    }

    @Test
    void testExitBurrow(){
        r1.setBurrow(burrow);
        r1.setAwake(false);
        r1.setHiding(true);
        burrow.addOwner(r1);
        burrow.addRabbit(r1);
        world.setTile(new Location(2,2), burrow);
        world.add(r1);
        assertEquals(new Location(2, 2), world.getLocation(burrow));
        assertTrue(world.contains(r1));
        assertTrue(burrow.getRabbitsInHole().contains(r1));

        r1.exitBurrow(world, new Location(2,2));
        assertFalse(burrow.getRabbitsInHole().contains(r1));
        assertEquals(new Location(2, 2), world.getLocation(r1));
    }

    @Test
    void testExitBurrow2(){
        world = new World(2);
        world.setTile(new Location(0,0), burrow);
        world.setTile(new Location(0,0), r1);
        world.setTile(new Location(0,1), r2);
        world.setTile(new Location(1,0), r3);
        world.add(r4);
        world.add(r5);
        burrow.addOwner(r4);
        burrow.addRabbit(r4);
        burrow.addOwner(r5);
        burrow.addRabbit(r5);
        burrow.destroyBurrow(world);
        assertEquals(5, world.getEntities().size());
    }
}
