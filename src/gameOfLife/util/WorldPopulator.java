package gameOfLife.util;

import gameOfLife.entities.Grass;
import gameOfLife.entities.Hole;
import gameOfLife.entities.Rabbit;
import itumulator.world.Location;
import itumulator.world.World;

import java.io.*;
import java.util.*;

public class WorldPopulator {
    private final DataHandler dh;
    private final Set<String> entities;

    public WorldPopulator(World world, String week)
    {
        this.dh = new DataHandler(week);
        this.entities = new HashSet<>();
        entities.add("rabbit");
        entities.add("grass");
        entities.add("burrow");
        loadEntities(world);
    }

    private void loadEntities(World world)
    {
        Map<String, String> fileMap = new HashMap<>();
        Random rand = new Random();
        List<File> loadedFile = dh.getAllFiles();
        try
        {
            for(File f : loadedFile)
            {
                BufferedReader br = new BufferedReader(new FileReader(f));
                Scanner scan  = new Scanner(br);
                //Skips first line, as this has been loaded elsewhere (world size).
                scan.nextLine();

                //Inserting each line of file into map ([0] contains entity type, [1] contains population size).
                fileMap.put(scan.nextLine().split(" ")[0], scan.nextLine().split(" ")[1]);

                for(String entity : entities)
                {
                    for(String key : fileMap.keySet())
                    {
                        if(entity.equals(key))
                        {
                            int amount = 0;
                            if(fileMap.get(key).contains("-"))
                            {
                                //Creates a random amount of entities between the first and second digit declared in the file.
                                amount = rand.nextInt(Integer.parseInt(fileMap.get(key).split("-")[0]),Integer.parseInt(fileMap.get(key).split("-")[1]));
                            }
                            else {
                                amount = Integer.parseInt(fileMap.get(key));
                            }
                            //Creates a random location to spawn new entity.
                            createEntity(world, entity, amount);
                        }
                    }
                }
            }
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    private void createEntity(World world, String type, int amount)
    {
        Random rand = new Random();
        int population = world.getEntities().size()+amount;

        switch(type)
        {
            case "grass":
                while(population >= world.getEntities().size())
                {
                    Location location = new Location(rand.nextInt(world.getSize()),rand.nextInt(world.getSize()));
                    world.setTile(location, new Grass(1));
                }
                break;
            case "burrow":
                while(population >= world.getEntities().size())
                {
                    Location location = new Location(rand.nextInt(world.getSize()),rand.nextInt(world.getSize()));
                    world.setTile(location, new Hole(2));
                }
                break;
            case "rabbit":
                while(population >= world.getEntities().size())
                {
                    Location location = new Location(rand.nextInt(world.getSize()),rand.nextInt(world.getSize()));
                    world.setTile(location, new Rabbit());
                }
                break;
        }
    }
}
