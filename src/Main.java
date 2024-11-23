import java.awt.Color;
import java.util.List;

import gameOfLife.worldObjects.entities.Rabbit;
import gameOfLife.worldObjects.entities.Grass;
import gameOfLife.util.ProgramInitializer;
import gameOfLife.worldObjects.Burrow;
import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;

public class Main {

    public static void main(String[] args) {
        int resolution = 800;
        int programDelay = 100;
        ProgramInitializer pi = new ProgramInitializer("week-1", "t1-3a", resolution, programDelay);
        List<Program> programs = pi.getPrograms();
        for(Program p : programs) {
            p.setDisplayInformation(Grass.class, new DisplayInformation(Color.green, "grass"));
            p.setDisplayInformation(Burrow.class, new DisplayInformation(Color.green, "hole"));
            p.setDisplayInformation(Rabbit.class, new DisplayInformation(Color.green, "rabbit-large"));

            p.show();
            for(int i = 0; i < 50; i++)
            {
                p.simulate();
            }

        }
//        Program program = pi.getPrograms().getFirst();
//        program.setDisplayInformation(Grass.class, new DisplayInformation(Color.green, "grass"));
//
//        program.show();
    }
}