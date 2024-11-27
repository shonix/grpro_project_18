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
        int programDelay = 10;
        ProgramInitializer pi = new ProgramInitializer("week-1", "t1-2cde", resolution, programDelay);
        List<Program> programs = pi.getPrograms();
        for(Program p : programs) {
            p.setDisplayInformation(Grass.class, new DisplayInformation(Color.green, "grass"));
            p.setDisplayInformation(Burrow.class, new DisplayInformation(Color.DARK_GRAY, "hole-small"));
            p.show();
        }
    }
}