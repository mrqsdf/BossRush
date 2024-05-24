package fr.mrqsdf.engine2d.scenes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.mrqsdf.engine2d.components.Component;
import fr.mrqsdf.engine2d.components.ComponentDeserializer;
import fr.mrqsdf.engine2d.components.SpriteRenderer;
import fr.mrqsdf.engine2d.components.StateMachine;
import fr.mrqsdf.engine2d.jade.Camera;
import fr.mrqsdf.engine2d.jade.GameObject;
import fr.mrqsdf.engine2d.jade.GameObjectDeserializer;
import fr.mrqsdf.engine2d.jade.Transform;
import fr.mrqsdf.engine2d.physics2d.Physics2D;
import fr.mrqsdf.engine2d.renderer.Renderer;
import fr.mrqsdf.engine2d.utils.AssetPool;
import fr.mrqsdf.engine2d.utils.Levels;
import fr.mrqsdf.engine2d.utils.Saved;
import org.joml.Vector2f;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Scene {

    private Renderer renderer;
    private Camera camera;
    private boolean isRunning;
    private List<GameObject> gameObjects;
    private Physics2D physics2D;

    private SceneInitializer sceneInitializer;

    private int levelID;

    public Scene(SceneInitializer sceneInitializer, int levelID) {
        this.sceneInitializer = sceneInitializer;
        this.physics2D = new Physics2D();
        this.renderer = new Renderer();
        this.gameObjects = new ArrayList<>();
        this.isRunning = false;
        this.levelID = levelID;
    }

    public void init() {
        this.camera = new Camera(new Vector2f(0, 0));
        this.sceneInitializer.loadResources(this);
        this.sceneInitializer.init(this);
    }

    public void start() {
        for (int i=0; i < gameObjects.size(); i++) {
            GameObject go = gameObjects.get(i);
            go.start();
            if (go.transform.isVisible)this.renderer.add(go);
            this.physics2D.add(go);
        }
        isRunning = true;
    }

    public void addGameObjectToScene(GameObject go) {
        if (!isRunning) {
            gameObjects.add(go);
        } else {
            gameObjects.add(go);
            go.start();
            if (go.transform.isVisible)this.renderer.add(go);
            this.physics2D.add(go);
        }
    }

    public <T extends Component> GameObject getGameObjectWithComponent(Class<T> clazz) {
        for (GameObject go : gameObjects) {
            if (go.getComponent(clazz) != null) {
                return go;
            }
        }
        return null;
    }
    public <T extends Component> List<GameObject> getGameObjectsWithComponent(Class<T> clazz) {
        List<GameObject> gos = new ArrayList<>();
        for (GameObject go : gameObjects) {
            if (go.getComponent(clazz) != null) {
                gos.add(go);
            }
        }
        return gos;
    }
    public void addGameObjectToScene(int pos, GameObject go) {
        if (!isRunning) {
            gameObjects.add(pos, go);
        } else {
            gameObjects.add(pos, go);
            go.start();
            if (go.transform.isVisible)this.renderer.add(go);
            this.physics2D.add(go);
        }
    }

    public void destroy() {
        for (GameObject go : gameObjects) {
            go.destroy();
        }
    }

    public List<GameObject> getGameObjects() {
        return this.gameObjects;
    }
    public GameObject getGameObject(float x, float y){
        for (GameObject go : gameObjects){
            if (go.getComponent(Transform.class).position.x == x && go.getComponent(Transform.class).position.y == y){
                return go;
            }
        }
        return null;
    }
    public List<GameObject> getGameObjects(float x, float y){
        List<GameObject> gos = new ArrayList<>();
        for (GameObject go : gameObjects){
            if (go.getComponent(Transform.class).position.x == x && go.getComponent(Transform.class).position.y == y){
                gos.add(go);
            }
        }
        return gos;
    }
    public GameObject getGameObject(int gameObjectId) {
        Optional<GameObject> result = this.gameObjects.stream()
                .filter(gameObject -> gameObject.getUid() == gameObjectId)
                .findFirst();
        return result.orElse(null);
    }
    public void clearGameObjects() {
        for (GameObject go : gameObjects) {
            go.destroy();
        }
        gameObjects.clear();
    }
    public GameObject getLevelEditorStuff() {
        for (GameObject go : gameObjects) {
            if (go.name.equals("LevelEditor")) {
                return go;
            }
        }
        return null;
    }
    public void clearSerializableGameObjects() {
        for (GameObject go : gameObjects) {
            if (go.doSerialisation()) {
                go.destroy();
            }
        }
        gameObjects.removeIf(GameObject::doSerialisation);
    }

    public void editorUpdate(float dt) {
        this.camera.adjustProjection();

        for (int i=0; i < gameObjects.size(); i++) {
            GameObject go = gameObjects.get(i);
            go.editorUpdate(dt);
            if (!go.transform.isVisible) {
                this.renderer.remove(go);
            } else {
                this.renderer.add(go);
            }
            if (go.isDead()) {
                gameObjects.remove(i);
                this.renderer.destroyGameObject(go);
                this.physics2D.destroyGameObject(go);
                i--;
            }
        }
    }

    public void update(float dt) {
        this.camera.adjustProjection();
        this.physics2D.update(dt);

        for (int i=0; i < gameObjects.size(); i++) {
            GameObject go = gameObjects.get(i);
            go.update(dt);
            if (!go.transform.isVisible) {
                this.renderer.remove(go);
            } else {
                this.renderer.add(go);
            }
            if (go.isDead()) {
                gameObjects.remove(i);
                this.renderer.destroyGameObject(go);
                this.physics2D.destroyGameObject(go);
                i--;
            }
        }
    }

    public void render() {
        this.renderer.render();
    }

    public Camera camera() {
        return this.camera;
    }

    public void imgui() {
        this.sceneInitializer.imgui();
    }
    public void loadRessource(Scene scene){
        for (GameObject go : scene.getGameObjects()){
            if (go.getComponent(SpriteRenderer.class) != null){
                SpriteRenderer spr = go.getComponent(SpriteRenderer.class);
                if (spr.getTexture() != null){
                    spr.setTexture(AssetPool.getTexture(spr.getTexture().getFilepath()));
                }
            }
            if (go.getComponent(StateMachine.class) != null){
                StateMachine stateMachine = go.getComponent(StateMachine.class);
                stateMachine.refreshTextures();
            }
        }
    }
    public GameObject createGameObject(String name) {
        GameObject go = new GameObject(name);
        go.addComponent(new Transform());
        go.transform = go.getComponent(Transform.class);
        return go;
    }

    public void save() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();

        try {
            FileWriter writer = new FileWriter("assets/Levels/" + Levels.getLevelName(levelID) + ".level");
            List<GameObject> objsToSerialize = new ArrayList<>();
            for (GameObject obj : this.gameObjects) {
                if (obj.doSerialisation()) {
                    objsToSerialize.add(obj);
                }
            }
            String json = gson.toJson(objsToSerialize);
            String base64 = Saved.toBase64(json);
            writer.write(base64);
            writer.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();

        String inFile = "";
        File file = new File("assets/Levels/" + Levels.getLevelName(levelID) + ".level");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            inFile = new String(Files.readAllBytes(Paths.get("assets/Levels/" + Levels.getLevelName(levelID) + ".level")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!inFile.equals("")) {
            int maxGoId = -1;
            int maxCompId = -1;

            String json = Saved.fromBase64(inFile);
            GameObject[] objs = gson.fromJson(json.toString(), GameObject[].class);
            for (int i=0; i < objs.length; i++) {
                addGameObjectToScene(objs[i]);

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



    public int getLevelID() {
        return this.levelID;
    }
    public void setLevelID(int id) {
        this.levelID = id;
    }

    public Physics2D getPhysics() {
        return this.physics2D;
    }
}
