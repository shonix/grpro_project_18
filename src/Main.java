import java.awt.Color;

import gameOfLife.worldObjects.entities.Rabbit;
import gameOfLife.worldObjects.entities.Grass;
import gameOfLife.util.ProgramInitializer;
import gameOfLife.worldObjects.Burrow;
import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;

public class Main {

    public static void main(String[] args) {
        int resolution = 800;
        int programDelay = 1000;
        ProgramInitializer pi = new ProgramInitializer("week-1", "t1-2cde", resolution, programDelay);
        Program program = pi.getPrograms().getFirst();
        program.setDisplayInformation(Grass.class, new DisplayInformation(Color.green, "grass"));
        program.setDisplayInformation(Rabbit.class, new DisplayInformation(Color.red, "rabbit-small"));
        program.setDisplayInformation(Burrow.class, new DisplayInformation(Color.red, "hole"));

        program.show();
    }
}