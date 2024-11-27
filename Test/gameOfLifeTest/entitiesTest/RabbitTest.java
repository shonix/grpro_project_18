package gameOfLifeTest.entitiesTest;

import gameOfLife.util.DataHandler;
import gameOfLife.util.ProgramInitializer;
import gameOfLife.util.WorldHandler;
import gameOfLife.worldObjects.Burrow;
import gameOfLife.worldObjects.entities.Animal;
import gameOfLife.worldObjects.entities.Carcass;
import gameOfLife.worldObjects.entities.Grass;
import gameOfLife.worldObjects.entities.Rabbit;
import gameOfLife.worldObjects.entities.enums.EntityTypeID;
import gameOfLife.worldObjects.entities.enums.Sex;
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
        r1.setHome(burrow);
        r1.setAwake(false);
        r1.setHiding(true);
        burrow.addOwners(r1);
        burrow.addInhabitants(r1);
        world.setTile(new Location(2,2), burrow);
        world.add(r1);
        assertEquals(new Location(2, 2), world.getLocation(burrow));
        assertTrue(world.contains(r1));
        assertTrue(burrow.getInhabitants().contains(r1));

        r1.exitHome(world);
        assertFalse(burrow.getInhabitants().contains(r1));
        assertTrue(WorldHandler.getAllTiles(world, world.getLocation(burrow), 1).contains(world.getLocation(r1)));
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
        burrow.addOwners(Set.of(r4, r5));
        burrow.addInhabitants(Set.of(r4, r5));
        r4.setHiding(true);
        r5.setHiding(true);

        assertEquals(2, burrow.getInhabitants().size()); // assert burrow has exactly 2 rabbits
        burrow.destroy(world);
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
        int expectedRabbits = 7;
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
            program.simulate();
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
        r1.setCurrentEnergy(Rabbit.DAILY_ENERGY_REDUCTION); //set the energy of the rabbit to just what 1 day would remove from energy
        r1.age(world);
        assertFalse(world.contains(r1), "Rabbit has starved to death");
    }

    /**
     * Tests that a rabbit that dies while in a burrow, does not throw an exception and does not leave a carcass,
     * and that a rabbit that dies outside of a burrow also doesn't throw an exception, but does leave a carcass.
     */
    @Test
    void testRabbitCarcassCreation(){
        world.setTile(new Location(0,0), burrow);
        world.add(r1);
        burrow.addOwners(r1);
        burrow.addInhabitants(r1);
        world.setTile(new Location(0,1), r2);

        //assert that a burrow and two rabbits exists
        assertEquals(3, world.getEntities().size());
        assertTrue(world.getEntities().containsKey(r1));
        assertTrue(world.getEntities().containsKey(r2));
        assertTrue(world.getEntities().containsKey(burrow));

        //assert deaths does not throw exceptions in World
        assertDoesNotThrow(() -> {r1.die(world);});
        assertDoesNotThrow(() -> {r2.die(world);});

        //assert that a burrow and a rabbit carcass exists
        assertEquals(2, world.getEntities().size());
        assertEquals(1, WorldHandler.getEntitiesByType(world, Burrow.class).size());
        assertEquals(1, WorldHandler.getEntitiesByType(world, Carcass.class).size());
        assertEquals(EntityTypeID.CARCASS_RABBIT, WorldHandler.getEntitiesByType(world, Carcass.class).stream().toList().getFirst().getEntityTypeID());
    }

    /**
     *
     */
    @Test
    void testRabbitDieFromOldAge()
    {
        world.setTile(new Location(0,0), r1);
        world.setTile(new Location(0,0), new Grass(Grass.MAX_PROVIDED_SUSTENANCE));
        world.setCurrentLocation(new Location(0,0));
        r1.setAge(Rabbit.MAX_AGE);
        assertTrue(world.contains(r1));
        r1.age(world);
        assertFalse(world.contains(r1));
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
        world.setTile(new Location(0,0), r1);
        world.setCurrentLocation(new Location(0,0));
        r1.act(world);
        assertTrue(r1.getAge() > rabbitStartAge);
    }

    /**
     * K1-2e project specification test.
     * Pregnant rabbit creates baby rabbit.
     */
    @Test
    void testRabbitProcreation()
    {
        //Arrange
        Rabbit pregnantRabbit = new Rabbit(Rabbit.AGE_OF_MATURITY+1, Sex.FEMALE,false,false);
        pregnantRabbit.setPregnant(true);
        world.setTile(new Location(0,0), pregnantRabbit);
        int startRabbits = world.getEntities().size();

        //Act
        pregnantRabbit.act(world);

        //Assert
        int endRabbits = world.getEntities().size();
        assertTrue(endRabbits > startRabbits);
    }

    /**
     * K1-2f project specification test.
     */
    @Test
    void testRabbitDigBurrow1() {
        //Arrange
        program.getWorld().setTile(new Location(0,0), r1);

        //Act
        for (int i = 0; i < World.getDayDuration() +1; i++)
        {
            program.simulate();
        }

        //Assert
        assertTrue(program.getWorld().contains(r1.getHome()));
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
    void testRabbitHasOneBurrow(){
        {
            //Arrange
            program.getWorld().setTile(new Location(0,0), r1);

            //Act
            for (int i = 0; i < World.getDayDuration() +1; i++)
            {
                program.simulate();
            }

            //Assert
            assertNotNull(r1.getHome());
        }
    }


    /**
     * K1-2g project specification test.
     */
    @Test
    void testRabbitSeekBurrow1(){
        {
            //Arrange
            program.getWorld().setTile(new Location(0,0), r1);

            //Act
            for (int i = 0; i < World.getDayDuration() +1; i++)
            {
                program.simulate();
                if(world.getCurrentTime()>12)
                {
                    break;
                }
            }

            //Assert
            assertTrue(r1.getHome().getInhabitants().contains(r1));
        }
    }
}
