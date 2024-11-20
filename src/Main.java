import java.awt.Color;

import gameOfLife.entities.Grass;
import gameOfLife.util.WorldPopulator;
import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;

import itumulator.world.World;

public class Main {

    public static void main(String[] args) {
        int size = 5;
        Program p = new Program(size, 800, 75);

        World w = p.getWorld();
        WorldPopulator pop = new WorldPopulator(w, "week-1");
        // w.setTile(new Location(0, 0), new <MyClass>());
        p.setDisplayInformation(Grass.class, new DisplayInformation(Color.green, "gwass"));

        p.show();
    }
}