package fr.mrqsdf.engine2d.jade;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.mrqsdf.engine2d.components.*;
import org.joml.Vector4f;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Prefabs {

    public static List<Prefab> prefabs = new ArrayList<>();

    public static void addPrefab(GameObject gameObject){
        gameObject.getComponent(SpriteRenderer.class).setColor(new Vector4f(1,1,1,1));
        prefabs.add(new Prefab(gameObject));
    }

    public static GameObject getPrefab(int id){
        for (Prefab prefab : prefabs){
            if (prefab.id == id){
                return prefab.gameObject.copy();
            }
        }
        return null;
    }

    public static GameObject generateSpriteObject(Sprite sprite, float sizeX, float sizeY){
        GameObject block = Window.getScene().createGameObject("Sprite_Object_Gen");
        block.transform.scale.x = sizeX;
        block.transform.scale.y = sizeY;
        SpriteRenderer spriteRenderer = new SpriteRenderer();
        spriteRenderer.setSprite(sprite);
        block.addComponent(spriteRenderer);

        return block;
    }

    public static GameObject generateSpriteObject(Sprite sprite, float sizeX, float sizeY, float posX, float posY, float rotation){
        GameObject block = Window.getScene().createGameObject("Sprite_Object_Gen");
        block.transform.scale.x = sizeX;
        block.transform.scale.y = sizeY;
        block.transform.position.x = posX;
        block.transform.position.y = posY;
        block.transform.rotation = rotation;
        SpriteRenderer spriteRenderer = new SpriteRenderer();
        spriteRenderer.setSprite(sprite);
        block.addComponent(spriteRenderer);
        return block;
    }
    public static GameObject generateSpriteObject(Sprite sprite, float sizeX, float sizeY, float posX, float posY, float rotation, int zIndex){
        GameObject block = Window.getScene().createGameObject("Sprite_Object_Gen");
        block.transform.scale.x = sizeX;
        block.transform.scale.y = sizeY;
        block.transform.position.x = posX;
        block.transform.position.y = posY;
        block.transform.rotation = rotation;
        block.transform.zIndex = zIndex;
        SpriteRenderer spriteRenderer = new SpriteRenderer();
        spriteRenderer.setSprite(sprite);
        block.addComponent(spriteRenderer);
        return block;
    }
    public static GameObject generatePlayer(SpriteSheet spriteSheet, String label, int defaultSprite, boolean loop){
        GameObject entitySprite = generateSpriteObject(spriteSheet.getSprite(defaultSprite), 0.5f, 0.5f);
        AnimationState run = new AnimationState();
        run.title = "run";
        run.animationTypeName = AnimationPrefabs.RUN.name;
        float defaultFrameTime = 0.23f;
        run.addFrame(spriteSheet.getSprite(defaultSprite), defaultFrameTime);
        run.addFrame(spriteSheet.getSprite(2), defaultFrameTime);
        run.addFrame(spriteSheet.getSprite(3), defaultFrameTime);
        run.addFrame(spriteSheet.getSprite(2), defaultFrameTime);
        run.setLoop(loop);

        StateMachine stateMachine = new StateMachine();
        stateMachine.addState(run);
        stateMachine.setDefaultState(AnimationPrefabs.RUN);
        stateMachine.refreshTextures();
        entitySprite.addComponent(stateMachine);

        return entitySprite;
    }
    public static GameObject generateAnimation(SpriteSheet spriteSheet, String label, int defaultSprite, float defaultFrameTime, boolean loop, int endSprite){
        return generateAnimation(spriteSheet, label, defaultSprite, defaultFrameTime, loop, endSprite, defaultSprite);
    }
    public static GameObject generateAnimation(SpriteSheet spriteSheet, String label, int defaultSprite,float defaultFrameTime, boolean loop, int endSprite, int startSprite){
        GameObject entitySprite = generateSpriteObject(spriteSheet.getSprite(defaultSprite), 0.25f, 0.25f);
        AnimationState run = new AnimationState();
        run.title = "run";
        run.animationTypeName = AnimationPrefabs.RUN.name;
        int interval = endSprite - startSprite;
        for (int i = startSprite; i< endSprite; i++){
            run.addFrame(spriteSheet.getSprite(i), defaultFrameTime);
        }
        run.setLoop(loop);
        StateMachine stateMachine = new StateMachine();
        stateMachine.addState(run);
        stateMachine.setDefaultState(AnimationPrefabs.RUN);
        stateMachine.refreshTextures();
        entitySprite.addComponent(stateMachine);

        return entitySprite;
    }

    public static class Prefab{
        public int id;
        public GameObject gameObject;
        public static int idCounter = 0;
        public Prefab(GameObject go){
            this.gameObject = go;
            this.id = idCounter;
            idCounter++;
        }

    }


    public static void save() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();

        try {
            FileWriter writer = new FileWriter("Prefabs.txt");
            List<GameObject> objsToSerialize = new ArrayList<>();
            for (Prefab prefabs : prefabs) {
                GameObject obj = prefabs.gameObject;
                if (obj.doSerialisation()) {
                    objsToSerialize.add(obj);
                }
            }
            writer.write(gson.toJson(objsToSerialize));
            writer.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void load() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();

        String inFile = "";
        File file = new File("Prefabs.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            inFile = new String(Files.readAllBytes(Paths.get("Prefabs.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!inFile.equals("")) {
            int maxGoId = -1;
            int maxCompId = -1;
            GameObject[] objs = gson.fromJson(inFile, GameObject[].class);
            for (int i=0; i < objs.length; i++) {
                addPrefab(objs[i]);

                for (Component c : objs[i].getAllComponents()) {
                    if (c.getUid() > maxCompId) {
                        maxCompId = c.getUid();
                    }
                }
                if (objs[i].getUid() > maxGoId) {
                    maxGoId = objs[i].getUid();
                }
            }

            maxGoId++;
            maxCompId++;
            GameObject.init(maxGoId);
            Component.init(maxCompId);
        }
    }

    private enum AnimationPrefabs implements AnimationType {
        RUN("run"),
        IDLE("idle");

        public final String name;

        AnimationPrefabs(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

}
