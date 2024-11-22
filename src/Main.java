import java.awt.Color;

import gameOfLife.worldObjects.entities.Rabbit;
import gameOfLife.worldObjects.entities.Grass;
import gameOfLife.util.ProgramInitializer;
import gameOfLife.worldObjects.Burrow;
import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;

public class Main {

    public static void main(String[] args) {
        int resolution = 800;
        int programDelay = 1000;
        ProgramInitializer pi = new ProgramInitializer("week-1", "t1-2cde", resolution, programDelay);
        Program program = pi.getPrograms().getFirst();
        program.setDisplayInformation(Grass.class, new DisplayInformation(Color.green, "grass"));

        program.show();
    }
}