package gameOfLife.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataHandler
{
    private Map<String, File> cache;
    public DataHandler(String week)
    {
        fileCacher(week);
    }

    /**
     * TODO
     * @param cacheString
     * @return
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
     * TODO
     * @return
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
     * TODO
     */
    private void fileCacher(String weekNumber)
    {
        cache = new HashMap<String, File>();

        loadFolder(".\\data\\"+weekNumber+"\\");
        loadFolder("..\\data"+weekNumber+"\\");
    }

    /**
     * TODO
     * @param folderPath
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
        } catch (IllegalArgumentException e){
            System.out.println("Cache loader error: " + e.getMessage());
        }
    }
}
