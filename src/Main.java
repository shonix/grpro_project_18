import java.awt.Color;

import gameOfLife.entities.Grass;
import gameOfLife.entities.Rabbit;
import gameOfLife.util.WorldPopulator;
import gameOfLife.entities.Grass;
import gameOfLife.entities.Hole;
import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;

public class Main {

    public static void main(String[] args) {
        int size = 5;
        Program p = new Program(size, 800, 75);
        World w = p.getWorld();

        WorldPopulator pop = new WorldPopulator(w, "week-1");
        p.setDisplayInformation(Grass.class, new DisplayInformation(Color.green, "gwass"));
        p.setDisplayInformation(Rabbit.class, new DisplayInformation(Color.red, "rabbit-small"));
        p.setDisplayInformation(Hole.class, new DisplayInformation(Color.red, "hole"));

        p.show();
    }
}