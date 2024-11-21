import java.awt.Color;

import gameOfLife.entities.Rabbit;
import gameOfLife.entities.Grass;
import gameOfLife.util.WorldInitializor;
import gameOfLife.entities.Burrow;
import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.world.World;

public class Main {

    public static void main(String[] args) {
        WorldInitializor pop = new WorldInitializor("week-1", "t1-1a");
        Program program = pop.getWorlds().getFirst();
        program.setDisplayInformation(Grass.class, new DisplayInformation(Color.green, "grass"));
        program.setDisplayInformation(Rabbit.class, new DisplayInformation(Color.red, "rabbit-small"));
        program.setDisplayInformation(Burrow.class, new DisplayInformation(Color.red, "hole"));

        program.show();
    }
}