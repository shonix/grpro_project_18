package gameOfLife.util;

import java.io.File;
import java.util.*;

/**
 * A class to allow caching of all files in data folder. Strips any subdirectory and only maintains name (thus they must be unique even in subdirectories).
 */
public class DataHandler
{
    private Map<String, File> cache;

    /**
     * Constructs a cache from the chosen folder, containing files for later use.
     * @param dataFolder Folder inside project where input files are located.
     */
    public DataHandler(String dataFolder)
    {
        this.cache = new HashMap<>();
        fileCacher(dataFolder);
    }

    /**
     * Returns File with name matching a filename in cache.
     * @param cacheString Name of file which exists in the cache.
     * @return File with matched name from cache.
     */
    public File getFile(String cacheString)
    {
        File result = cache.get(cacheString);
        if (result == null){
            System.out.println(cacheString + " is null");
        }
        return result;
    }

    /**
     * Returns a list of all cached Files.
     * @return Returns a list of all files loaded into the cache.
     */
    public List<File> getAllFiles()
    {
        List<File> result = new ArrayList<>();
        for(String s : cache.keySet())
        {
            result.add(getFile(s));
        }
        return result;
    }

    /**
     * Caches all files from folder corresponding to the weeknumber.
     * @param folderName name of folder wishing to cache.
     */
    private void fileCacher(String folderName)
    {
        cache = new HashMap<>();

        loadFolder(".\\data\\"+folderName+"\\");
        loadFolder("..\\data"+folderName+"\\");
    }

    /**
     * Loads in all the files in the folder chosen to cache.
     * @param folderPath Path to the folder to load into cache.
     */
    private void loadFolder(String folderPath){
        try {
            File folder = new File(folderPath);

            if (folder.listFiles() == null) {
                return;
            }
            for (File file : folder.listFiles()) {
                if (file.getName().endsWith(".txt")){
                    String fileName = file.getName().substring(0, file.getName().lastIndexOf('.'));
                    if (cache.containsKey(fileName)){
                        throw new IllegalArgumentException("File names in Data folder must be unique - " + fileName);
                    } else {
                        cache.put(fileName, file);
                    }
                }
            }
        } catch (IllegalArgumentException | NullPointerException e){
            System.out.println("Cache loader error: " + e.getMessage());
        }
    }
}
