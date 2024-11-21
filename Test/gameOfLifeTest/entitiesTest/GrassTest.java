package gameOfLifeTest.entitiesTest;

import gameOfLife.entities.Grass;
import itumulator.world.World;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GrassTest {
    Grass grass;
    int typicalFood = Grass.MAX_PROVIDED_SUSTENANCE - Grass.MIN_PROVIDED_SUSTENANCE;

    @BeforeEach
    public void setUp() {
        grass = new Grass(1);
    }

    @AfterEach
    public void tearDown() {
        grass = null;
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
}
