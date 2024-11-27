package gameOfLifeTest.entitiesTest;

import gameOfLife.util.ProgramInitializer;
import gameOfLife.worldObjects.entities.BerryBush;
import gameOfLife.worldObjects.entities.Grass;
import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BerryBushTest {
    Program program;
    World world;
    BerryBush berryBush;
    Location defaultLocation = new Location(0,0);
    //Bear bear;

    @BeforeEach
    void setUp(){
        program = new Program(2, 800, 1);
        world = program.getWorld();
        berryBush = new BerryBush();
        //bear = new Bear();
    }

    @AfterEach
    void tearDown(){
        program = null;
        world = null;
        berryBush = null;
        //bear = null;
    }

   /**
    * BerryBush can be placed from input file on a random location.
    */
    @Test
    void testBerryBushPlacedFromFile(){
        //using ProgramInitialiser to place BerryBush from a custom test file, expecting a world of size 2 and 1 berry.
        ProgramInitializer pi = new ProgramInitializer("test", "berryBushTest", 200, 1);
        program = pi.getPrograms().getFirst();
        world = program.getWorld();
        assertEquals(1, world.getEntities().size()); //assert the world has exactly one entity
        assertInstanceOf(BerryBush.class, world.getEntities().keySet().stream().toList().getFirst()); //assert the entity is of type BerryBush
    }

    /**
     * Tests that a BerryBush can regrow berries
     */
    @Test
    void testBerryBushRegrows(){
        berryBush = new BerryBush(0, false);
        world.setTile(defaultLocation, berryBush);
        for(int i = 0; i < BerryBush.STEPS_TO_REGROW_BERRIES; i++){
            assertFalse(berryBush.isEdible()); //bush does not have any berries currently
            assertEquals(0, berryBush.getProvidedSustenance()); //the sustenance provided is zero
            program.simulate();
        }
        program.simulate();
        assertTrue(berryBush.isEdible()); //berry bush should now be edible meaning it has berries
        assertEquals(BerryBush.PROVIDED_SUSTENANCE_WITH_BERRIES, berryBush.getProvidedSustenance()); //the berry bush has sustenance with berries
    }

    /**
     * Tests that a BerryBush can get eaten and change its state accordingly
     */
    @Test
    void testBerryBushBeEaten(){
        assertTrue(berryBush.isEdible());
        assertEquals(BerryBush.PROVIDED_SUSTENANCE_WITH_BERRIES, berryBush.getProvidedSustenance());
        berryBush.beEaten();
        assertFalse(berryBush.isEdible()); // should no longer be edible and thus have no berries
        assertEquals(0, berryBush.getProvidedSustenance()); // the sustenance provided after being eaten should be zero
        assertEquals(0, berryBush.getStepsSinceLastEaten()); // number of steps since last eaten should be zero
        assertThrows(IllegalStateException.class, () -> {berryBush.beEaten();}); // trying to eat an empty bush throws exception
    }
}
