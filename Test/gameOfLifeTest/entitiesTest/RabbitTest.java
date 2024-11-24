package gameOfLifeTest.entitiesTest;

import gameOfLife.worldObjects.Burrow;
import gameOfLife.worldObjects.entities.Animal;
import gameOfLife.worldObjects.entities.Rabbit;
import itumulator.world.Location;
import itumulator.world.World;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.Set;

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
        assertEquals(1, world.getEntities().size()); // a burrow has been added to the map and thus world
        world.setTile(new Location(0,0), r1);
        assertEquals(2, world.getEntities().size()); // a rabbit has been added to the world map
        world.setTile(new Location(0,1), r2);
        assertEquals(3, world.getEntities().size()); // yet another rabbit has been added
        world.setTile(new Location(1,0), r3);
        assertEquals(4, world.getEntities().size()); // a final rabbit has been added to the world map
        world.add(r4);
        assertEquals(5, world.getEntities().size()); // a rabbit is added to the world but not the map
        world.add(r5);
        assertEquals(6, world.getEntities().size()); // a second rabbit is added to the world but not the map
        burrow.addOwner(r4);
        burrow.addRabbit(r4);
        r4.setHiding(true);
        burrow.addOwner(r5);
        burrow.addRabbit(r5);
        r5.setHiding(true);
        assertEquals(2, burrow.getRabbitsInHole().size()); // assert burrow has exactly 2 rabbits
        burrow.destroyBurrow(world);
        assertEquals(4, world.getEntities().size()); // a rabbit and a burrow have been removed
        int allRabbits = world.getAll(Rabbit.class, world.getSurroundingTiles(new Location(0,0), world.getSize()+1)).size(); //all rabbits except on tile 0,0
        if(world.getTile(new Location(0,0)) instanceof Rabbit) allRabbits++; //does a rabbit exist on tile 0,0?
        assertEquals(4, allRabbits); // assert that there only exists 4 rabbits in the whole world
    }

    /**
     * K1-2a project specification test.
     */
    @Test
    void testRabbitPlacedFromFile(){
        //TODO
    }

    /**
     * K1-2b project specification test.
     */
    @Test
    void testRabbitDeath(){
        //TODO
    }

    /**
     * K1-2c project specification test.
     */
    @Test
    void testRabbitEatGrass1(){
        //TODO
    }

    /**
     * K1-2c project specification test.
     */
    @Test
    void testRabbitDieFromStarvation(){}

    /**
     *
     */
    @Test
    void testRabbitDieFromOldAge(){
        //TODO
    }

    /**
     *
     */
    @Test
    void testRabbitDieFromPredator(){
        //TODO
    }

    /**
     * K1-2d project specification test.
     */
    @Test
    void testRabbitAging(){
        //TODO
    }

    /**
     * K1-2e project specification test.
     */
    @Test
    void testRabbitProcreation1(){
        //TODO
    }

    /**
     * K1-2e project specification test.
     */
    @Test
    void testRabbitProcreation2(){
        //TODO
    }

    /**
     * K1-2f project specification test.
     */
    @Test
    void testRabbitDigBurrow1(){
        //TODO
    }

    /**
     * K1-2f project specification test.
     */
    @Test
    void testRabbitDigBurrow2(){
        //TODO
    }

    /**
     * K1-2f project specification test.
     */
    @Test
    void testRabbitShareBurrow(){
        //TODO
    }

    /**
     * K1-2f project specification test.
     */
    @Test
    void testRabbitHasOneBurrow1(){
        //TODO
    }

    /**
     * K1-2f project specification test.
     */
    @Test
    void testRabbitHasOneBurrow2(){
        //TODO
    }

    /**
     * K1-2g project specification test.
     */
    @Test
    void testRabbitSeekBurrow1(){
        //TODO
    }


    /**
     * K1-2g project specification test.
     */
    @Test
    void testRabbitSeekBurrow2(){
        //TODO
    }


    /**
     * K1-2g project specification test.
     */
    @Test
    void testRabbitSeekBurrow3(){
        //TODO
    }
}
