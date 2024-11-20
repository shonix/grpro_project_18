import java.awt.Color;

import gameOfLife.entities.Grass;
import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;

public class Main {

    public static void main(String[] args) {
        int size = 5;
        Program p = new Program(size, 800, 75);

        World w = p.getWorld();
        w.setTile(new Location(1,1), new Grass(1));
        w.setTile(new Location(2,4), new Grass(1));
        w.setTile(new Location(3,2), new Grass(1));

        p.setDisplayInformation(Grass.class, new DisplayInformation(Color.green, "grass"));

        // p.setDisplayInformation(<MyClass>.class, new DisplayInformation(<Color>, "<ImageName>"));

        p.show();
    }
}