package gameOfLifeTest.entitiesTest;

import gameOfLife.util.DataHandler;
import gameOfLife.util.ProgramInitializer;
import gameOfLife.worldObjects.Burrow;
import gameOfLife.worldObjects.entities.Animal;
import gameOfLife.worldObjects.entities.Grass;
import gameOfLife.worldObjects.entities.Rabbit;
import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class RabbitTest {
    private Rabbit r1, r2, r3, r4, r5;
    private Grass grass;
    private Burrow burrow;
    private World world;
    private Program program;

    @BeforeEach
    void setUp() {
        ProgramInitializer pi = new ProgramInitializer("test", "setup_test", 800, 10);
        program = pi.getPrograms().getFirst();
        world = program.getWorld();
        r1 = new Rabbit();
        r2 = new Rabbit();
        r3 = new Rabbit();
        r4 = new Rabbit();
        r5 = new Rabbit();
        grass = new Grass(1);
        burrow = new Burrow(2);
        world = new World(5);
    }

    @AfterEach
    void tearDown() {
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
    void k1_2a_testRabbitPlacedFromFile(){
        //Arrange
        ProgramInitializer pi = new ProgramInitializer("test", "k1_2a_test", 800, 10);
        Program localProgram = pi.getPrograms().getFirst();
        World localWorld = localProgram.getWorld();
        var entities = localWorld.getEntities();

        //Act


        //Assert
        int expectedRabbits = 5;
        assertEquals(expectedRabbits, entities.size(), "Expected: "+ expectedRabbits+" - Actual: "+ entities.size());

    }

    /**
     * K1-2b project specification test.
     */
    @Test
    void testRabbitDeathAge(){
        //Arrange
        world.add(r1);
        assertTrue(world.contains(r1), "World contains Rabbit");

        //Act
        r1.die(world);

        //Assert
        assertFalse(world.contains(r1), "World does not contain Rabbit");
    }

    /**
     * K1-2c project specification test.
     */
    @Test
    void testRabbitEatGrass1(){
        world.setTile(new Location(0,0), r1);
        world.setTile(new Location(0,0), grass);

        for(int i = 0; i < 20; i++)
        {
            r1.act(world);
        }

        assertTrue(world.contains(grass));
    }

    /**
     * K1-2c project specification test.
     */
    @Test
    void testRabbitDieFromStarvation()
    {
        world.setTile(new Location(0,0), r1);
        for(int i = 0; i < Rabbit.MAX_AGE; i++)
        {
            if(!world.contains(r1))
            {
                break;
            }
            r1.act(world);
        }
        assertFalse(world.contains(r1), "Rabbit has starved to death");
    }

    /**
     *
     */
    @Test
    void testRabbitDieFromOldAge()
    {
        world.setTile(new Location(0,0), r1);
        world.setTile(new Location(0,0), new Grass(Grass.MAX_PROVIDED_SUSTENANCE));
        for(int i = 0; i < Rabbit.MAX_AGE+1; i++)
        {
            if(!world.contains(r1))
            {
                break;
            }
            r1.act(world);
        }
        assertFalse(world.contains(r1), "Rabbit is still alive");
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
        int rabbitStartAge = r1.getAge();
        r1.act(world);
        assertTrue(r1.getAge() > rabbitStartAge);
    }

    /**
     * K1-2e project specification test.
     * Pregnant rabbit creates baby rabbit.
     */
    @Test
    void testRabbitProcreation1()
    {
        //Arrange
        Rabbit pregnantRabbit = new Rabbit(Rabbit.AGE_OF_MATURITY+1,Animal.Sex.FEMALE,false,false);
        pregnantRabbit.setPregnant(true);
        pregnantRabbit.isPregnant();
        int startRabbits = world.getEntities().size();

        //Act
        pregnantRabbit.act(world);
        pregnantRabbit.act(world);

        //Assert
        int endRabbits = world.getEntities().size();
        assertTrue(endRabbits > startRabbits);
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
