package gameOfLifeTest.entitiesTest;

import itumulator.world.World;
import itumulator.executable.Program;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WolfTest {
    Program program;
    World world;
    //Wolf wolf1, wolf2;
    //Lair lair;

    @BeforeEach
    void setUp(){
        program = new Program(20, 800, 1);
        world = program.getWorld();
        //wolf1 = new Wolf();
        //wolf2  =new Wolf();
    }

    @AfterEach
    void tearDown(){
        program = null;
        world = null;
        //wolf1 = wolf2 = null;
    }

    /**
     * Tests Wolf ability to be placed on map from input file
     */
    @Test
    void k2_1a_wolfTestPlaceOnMapFromFile(){

    }


    /**
     * Tests Wolf ability to join a pack when placed on map from input file
     */
    @Test
    void k2_2a_wolfTestPlacedFromFile2(){

    }

    /**
     * Tests Wolf ability to die from being attacked
     */
    @Test
    void k2_1b_wolfTestDie1(){

    }

    /**
     * Tests Wolf ability to die of starvation
     */
    @Test
    void k2_1b_wolfTestDie2(){

    }


    /**
     * Tests Wolf ability to hunt and gain sustenance from animals
     */
    @Test
    void k2_1c_wolfTHunt(){

    }


    /**
     * Tests Wolf ability to seek towards its pack
     */
    @Test
    void k2_2a_wolfTestSeekPack(){

    }

    /**
     * Tests that a pack of wolves are assoicated with a Lair
     */
    @Test
    void k2_3a_wolfTestAssociatedWithLair(){

    }

    /**
     * Tests that wolves procreate in a Lair
     */
    @Test
    void k2_3a_wolfTestProcreateInLair(){

    }

    /**
     * Tests that a wolf pack can decide to build exactly one Lair and be associated with it
     */
    @Test
    void k2_3a_wolfTestBuildLair(){

    }

    /**
     * Tests a wolf's ability to attack a wolf from another pack.
     */
    @Test
    void k2_3a_wolfTestFightOtherPack(){

    }

    /**
     * Tests a wolf's ability to attack a bear.
     */
    @Test
    void k2_3a_wolfTestFightBear1(){

    }

    /**
     * Tests a wolf's ability to defend against a bear.
     */
    @Test
    void k2_3a_wolfTestFightBear2(){

    }

}
