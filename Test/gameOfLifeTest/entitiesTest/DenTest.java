package gameOfLifeTest.entitiesTest;

import itumulator.executable.Program;
import itumulator.world.World;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class DenTest {
    Program program;
    World world;
    //Wolf wolf1, wolf2;
    //Den den;

    @BeforeEach
    void setUp(){
        program = new Program(20, 800, 1);
        world = program.getWorld();
        //Den = new Lair();
        //wolf1 = new Wolf();
        //wolf2 = new Wolf();
    }

    @AfterEach
    void tearDown(){
        program = null;
        world = null;
        //wolf1 = wolf2 = null;
        //Den = null;
    }

    //TODO insert tests


}
