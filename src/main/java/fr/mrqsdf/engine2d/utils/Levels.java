package fr.mrqsdf.engine2d.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import fr.mrqsdf.engine2d.jade.Window;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Levels {

    public static List<Levels> levels = new ArrayList<>();

    public String name;
    public int id;
    public transient boolean isLoaded = false;
    public transient boolean isDeleted = false;


    public Levels(String name) {
        this.name = name;
        this.id = getMaxId() + 1;
        levels.add(this);
    }

    public Levels(String name, int id) {
        for (Levels l : levels){
            if (l.id == id) throw new IllegalArgumentException("Level with id " + id + " already exists");
        }
        this.name = name;
        this.id = id;
        levels.add(this);
    }


    public static String getLevelName(int id){
        String name = null;
        for (Levels l : levels){
            if (l.id == id) name = l.name;
        }
        return name;
    }
    public static Levels getLevel(String name){
        Levels level = null;
        for (Levels l : levels){
            if (Objects.equals(l.name, name)) level = l;
        }
        return level;
    }
    public static Levels getLevel(int id){
        Levels level = null;
        for (Levels l : levels){
            if (l.id == id) level = l;
        }
        return level;
    }

    private static int getMaxId(){
        int max = 0;
        for (Levels l : levels){
            if (l.id > max) max = l.id;
        }
        return max;
    }
    public static int getMinId() {
        int min = getMaxId();
        for (Levels l : levels) {
            if (l.id < min) min = l.id;
        }
        return min;
    }



    public static void saveLevels(){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(levels);
        String base64 = Saved.toBase64(json);

        try {
            FileWriter writer = new FileWriter("assets/Levels/levelID.level");
            writer.write(base64);
            writer.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveAllLevels(){
        //save tout les level comme le save levels, et suprimé tout les fichier qui ne sons pas dans le levels
        saveLevels();
        File folder = new File("assets/Levels");
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                String name = file.getName();
                if (!name.equals("levelID.level")){
                    boolean found = false;
                    for (Levels l : levels){
                        if (name.equals(l.name + ".level")){
                            found = true;
                        }
                    }
                    if (!found){
                        file.delete();
                    }
                }
            }
        }
    }

    public static void loadLevels() {
        File file = new File("assets/Levels/levelID.level");
        File folder = new File("assets/Levels");
        if (!folder.exists()) folder.mkdir();
        if (!file.exists()) return;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            String json = new String(Files.readAllBytes(Paths.get("assets/Levels/levelID.level")));
            json = Saved.fromBase64(json);
            levels = gson.fromJson(json, new TypeToken<List<Levels>>(){}.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteLevel(){
        File file = new File("assets/Levels/" + getLevelName(Window.getScene().getLevelID()) + ".level");
        file.delete();
    }

}
