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
        int size = 5;
        Program p = new Program(size, 800, 75);
        World w = p.getWorld();

        WorldInitializor pop = new WorldInitializor("week-1", "t1-1a");
        World world = pop.getWorlds().getFirst();
        p.setDisplayInformation(Grass.class, new DisplayInformation(Color.green, "grass"));
        p.setDisplayInformation(Rabbit.class, new DisplayInformation(Color.red, "rabbit-small"));
        p.setDisplayInformation(Burrow.class, new DisplayInformation(Color.red, "hole"));

        p.show();
    }
}