package gameOfLife.util;

import gameOfLife.worldObjects.entities.Grass;
import gameOfLife.worldObjects.Burrow;
import gameOfLife.worldObjects.entities.Rabbit;
import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;

import java.io.*;
import java.util.*;

public class ProgramInitializer {
    private final DataHandler dh;
    private final Set<String> entities;
    private final int resolution;
    private final int programDelay;
    private List<Program> programs;

    /**
     * Initializes the program, as well as populates the world in said program, based on input file supplied.
     * @param folderName Name of folder for input files to be imported.
     * @param fileName Name of file, in folder, to be used for world generation.
     * @param resolution Resolution of program screen.
     * @param programDelay Delay of simulation steps (act).
     */
    public ProgramInitializer(String folderName, String fileName, int resolution, int programDelay)
    {
        this.dh = new DataHandler(folderName);
        this.entities = new HashSet<>();
        this.programs = new ArrayList<>();
        this.resolution = resolution;
        this.programDelay = programDelay;

        initializeEntities();
        File file = dh.getFile(fileName);
        loadEntities(file);
    }

    /**
     * Initializes and returns programs for all input files in cached folder.
     * @return List of programs with populated worlds.
     */
    public List<Program> initializeAllWPrograms()
    {
        initializeEntities();
        loadEntities(null);
        return programs;
    }

    /**
     * Getter for returning all Programs created while initializing programs.
     * @return List of all Programs created in initialization.
     */
    public List<Program> getPrograms() {
        return programs;
    }

    /**
     * Creates a new Program with a world the size of given input, read from input file.
     * @param size Size of world, as dictated by input file.
     * @return A new Program with world created inside.
     */
    private Program createProgram(int size)
    {
        return new Program(size,this.resolution,this.programDelay);
    }

    /**
     * Handles reading through the given input file, and loading in values needed for creating entities for the world.
     * @param file File used for loading in all entities.
     */
    private void loadEntities(File file)
    {
        List<File> loadedFiles;
        Map<String, String> fileMap = new HashMap<>(); // maps each type of entity contained in the file to how many of each to be created
        Program program;

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
                int worldSize = Integer.parseInt(scan.nextLine());
                    program = createProgram(worldSize);
                    programs.add(program);

                //Inserting each line of file into map ([0] contains entity type, [1] contains population size).

                while(scan.hasNextLine()) {
                    String[] fileContent = scan.nextLine().split(" ");
                    fileMap.put(fileContent[0], fileContent[1]);
                }

                for(String entity : this.entities) //For every creatable object.
                {
                    for(String key : fileMap.keySet()) //For every creatable object in the loaded file
                    {
                        if(entity.equals(key)) //If the object in the file == creatable object in entities map.
                        {
                            createEntity(program.getWorld(), entity, fileMap, key);
                        }
                    }
                }
            }
        } catch (FileNotFoundException e)
        {
            System.out.println(e.getMessage());
        }
    }


    /** //Remove side effects - Don't call populateWorld.
     * Creates a new entity for the given world, using the information provided by the files in the fileMap.
     * @param world World object which holds all information of the simulation.
     * @param entity The name of the entity being created.
     * @param fileMap Map of files being used to create entities to populate the world.
     * @param key The key of entity being created.
     */
    private void createEntity(World world, String entity, Map<String, String> fileMap, String key)
    {
        Random rand = new Random();
        int amount;
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
                    if(world.isTileEmpty(location)) {
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
