package fr.mrqsdf.engine2d.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import fr.mrqsdf.engine2d.jade.GameObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Prefab {
    public static List<Prefab> prefabs = new ArrayList<>();
    public int id;
    public String name;
    public GameObject gameObject;

    public Prefab(String name, GameObject newObject){
        this.id = getMaxId() + 1;
        this.name = name;
        this.gameObject = newObject;
        prefabs.add(this);
    }
    public Prefab(String name, int id, GameObject newObject){
        for (Prefab l : prefabs){
            if (l.id == id) throw new IllegalArgumentException("Level with id " + id + " already exists");
        }
        this.id = id;
        this.name = name;
        this.gameObject = newObject;
        prefabs.add(this);
    }


    public static String getPrefabName(int id){
        String name = null;
        for (Prefab l : prefabs){
            if (l.id == id) name = l.name;
        }
        return name;
    }

    public static Prefab getPrefab(int id){
        Prefab level = null;
        for (Prefab l : prefabs){
            if (l.id == id) level = l;
        }
        return level;
    }

    private static int getMaxId(){
        int max = 0;
        for (Prefab l : prefabs){
            if (l.id > max) max = l.id;
        }
        return max;
    }
    public static int getMinId() {
        int min = getMaxId();
        for (Prefab l : prefabs) {
            if (l.id < min) min = l.id;
        }
        return min;
    }

    public static void savePrefabs(){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(prefabs);

        try {
            FileWriter writer = new FileWriter("Prefab/prefabID.txt");
            writer.write(json);
            writer.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    public static void loadPrefabs() {
        File file = new File("Prefab/prefabID.txt");
        File folder = new File("Prefab");
        if (!folder.exists()) folder.mkdir();
        if (!file.exists()) return;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            String json = new String(Files.readAllBytes(Paths.get("Prefab/prefabID.txt")));
            prefabs = gson.fromJson(json, new TypeToken<List<Prefab>>(){}.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
