package gameOfLife.util;

import gameOfLife.entities.Grass;
import gameOfLife.entities.Burrow;
import gameOfLife.entities.Rabbit;
import itumulator.world.Location;
import itumulator.world.World;

import java.io.*;
import java.util.*;

public class WorldInitializor {
    private final DataHandler dh;
    private final Set<String> entities;
    private List<World> worlds;

    /**
     * TODO
     * @param week
     * @param fileName
     */
    public WorldInitializor(String week,String fileName)
    {
        this.dh = new DataHandler(week);
        this.entities = new HashSet<>();
        this.worlds = new ArrayList<>();

        initializeEntities();
        File file = dh.getFile(fileName);
        loadEntities( file);
    }

    /**
     * TODO
     * @return
     */
    public List<World> initializeAllWorlds()
    {
        initializeEntities();
        loadEntities(null);
        return worlds;
    }

    /**
     * Getter for returning all worlds created while initializing worlds.
     * @return List of all worlds created in initialization.
     */
    public List<World> getWorlds() {
        return worlds;
    }

    /**
     * TODO
     * @param size
     * @return
     */
    private World createWorld(int size)
    {
        return new World(size);
    }

    /**
     * TODO
     * @param file
     */
    private void loadEntities(File file)
    {
        List<File> loadedFiles = null;
        Map<String, String> fileMap = new HashMap<>();
        World world = null;

        if(file != null)
        {
            loadedFiles = new ArrayList<>();
            loadedFiles.add(file);
        }else{
            loadedFiles = dh.getAllFiles();
        }
        try
        {
            for(File f : loadedFiles)
            {
                BufferedReader br = new BufferedReader(new FileReader(f));
                Scanner scan  = new Scanner(br);

                    world = createWorld(Integer.parseInt(scan.nextLine()));
                    worlds.add(world);

                //Inserting each line of file into map ([0] contains entity type, [1] contains population size).
                String[]fileContent = scan.nextLine().split(" ");
                fileMap.put(fileContent[0], fileContent[1]);

                for(String entity : this.entities) //For every creatable object.
                {
                    for(String key : fileMap.keySet()) //For every creatable object in the loaded file
                    {
                        if(entity.equals(key)) //If the object in the file == creatable object in entities map.
                        {
                            createEntity(world, entity, fileMap, key);
                        }
                    }
                }
            }
        } catch (FileNotFoundException e)
        {
            System.out.println(e.getMessage());
        }
    }


    /**
     * Creates a new entity for the given world, using the information provided by the files in the fileMap.
     * @param world World object which holds all information of the simulation.
     * @param entity The name of the entity being created.
     * @param fileMap Map of files being used to create entities to populate the world.
     * @param key The key of entity being created.
     */
    private void createEntity(World world, String entity, Map<String, String> fileMap, String key)
    {
        Random rand = new Random();
        int amount = 0;
        if(fileMap.get(key).contains("-"))
        {
            //Creates a random amount of entities between the first and second digit declared in the file.
            int minPopulation = Integer.parseInt(fileMap.get(key).split("-")[0]);
            int maxPopulation = Integer.parseInt(fileMap.get(key).split("-")[1]);
            amount = rand.nextInt(minPopulation, maxPopulation);
        }
        else
        {
            amount = Integer.parseInt(fileMap.get(key));
        }
        //Creates a random location to spawn new entity.
        populateWorld(world, entity, amount);
    }

    /**
     * Populates created world with entities based on file input.
     * @param world World object.
     * @param type Which type of entity to create.
     * @param amount How many entities to distribute in the world
     */
    private void populateWorld(World world, String type, int amount)
    {
        Random rand = new Random();
        int population = world.getEntities().size()+amount-1;

        switch(type)
        {
            case "grass":
                while(population >= world.getEntities().size())
                {
                    Location location = new Location(rand.nextInt(world.getSize()),rand.nextInt(world.getSize()));
                    if(!world.containsNonBlocking(location))
                    {
                        world.setTile(location, new Grass(1));
                    }
                }
                break;
            case "burrow":
                while(population >= world.getEntities().size())
                {
                    Location location = new Location(rand.nextInt(world.getSize()),rand.nextInt(world.getSize()));
                    if(!world.containsNonBlocking(location)) {
                        world.setTile(location, new Burrow(2));
                    }
                }
                break;
            case "rabbit":
                while(population >= world.getEntities().size())
                {
                    Location location = new Location(rand.nextInt(world.getSize()),rand.nextInt(world.getSize()));
                    if(!world.isTileEmpty(location)) {
                        world.setTile(location, new Rabbit());
                    }
                }
                break;
        }
    }

    /**
     * Initializes all possible entities for creation.
     */
    private void initializeEntities()
    {
        this.entities.add("rabbit");
        this.entities.add("grass");
        this.entities.add("burrow");
    }
}
