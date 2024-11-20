package gameOfLifeTest.entitiesTest;

import gameOfLife.entities.Grass;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GrassTest {
    Grass grass;
    int typicalFood = Grass.MAX_FOOD - Grass.MIN_FOOD;

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
            new Grass(Grass.MIN_FOOD-1);
        });
        //test max+1
        assertThrows(IllegalArgumentException.class, () -> {
            new Grass(Grass.MAX_FOOD+1);
        });
        //test max
        assertDoesNotThrow(() -> { new Grass(Grass.MAX_FOOD); });
        //test min
        assertDoesNotThrow(() -> { new Grass(Grass.MIN_FOOD); });
        //test typical value
        assertDoesNotThrow(() -> { new Grass(typicalFood); });
    }

    @Test
    public void testAge() {
        assertEquals(0, grass.getAge());
        assertEquals(1, grass.getFood());

        for(int i = 1; i < Grass.MAX_FOOD; i++){
            grass.age();
            assertEquals(i+1, grass.getFood());
            assertEquals(i, grass.getAge());
        }
        grass.age();
        assertEquals(Grass.MAX_FOOD, grass.getFood());
        grass.age();
        assertEquals(Grass.MAX_FOOD, grass.getFood());
    }
}
