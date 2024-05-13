package fr.mrqsdf.engine2d.jade;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.mrqsdf.engine2d.components.Component;
import fr.mrqsdf.engine2d.components.ComponentDeserializer;
import fr.mrqsdf.engine2d.components.SpriteRenderer;
import fr.mrqsdf.engine2d.utils.AssetPool;
import imgui.ImGui;

import java.util.ArrayList;
import java.util.List;

public class GameObject {
    private static int ID_COUNTER = 0;
    private int uid = -1;
    public String name;
    private List<Component> components;
    public transient Transform transform;
    private boolean doSerialisation = true;
    private boolean isDead = false;

    public GameObject(String name){
        this.name = name;
        this.components = new ArrayList<>();

        uid = ID_COUNTER++;
    }
    public <T extends Component> T getComponent(Class<T> componentClass){
        for (Component c : components){
            if (componentClass.isAssignableFrom(c.getClass())){
                try {
                    return componentClass.cast(c);
                } catch (ClassCastException e){
                    e.printStackTrace();
                    assert false : "Error : Casting Component.";
                }
            }
        }
        return null;
    }

    public <T extends Component> void removeComponent(Class<T> componentClass) {
        for (int i=0; i < components.size(); i++) {
            Component c = components.get(i);
            if (componentClass.isAssignableFrom(c.getClass())) {
                components.remove(i);
                return;
            }
        }
    }

    public void addComponent(Component c){
        c.generateId();
        this.components.add(c);
        c.gameObject = this;
    }

    public void update(float dt){
        for (int i=0; i < components.size(); i++){
            components.get(i).update(dt);
        }
    }

    public void editorUpdate(float dt){
        for (int i=0; i < components.size(); i++){
            components.get(i).editorUpdate(dt);
        }
    }

    public GameObject copy() {
        // TODO: come up with cleaner solution
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .enableComplexMapKeySerialization()
                .create();
        String objAsJson = gson.toJson(this);
        GameObject obj = gson.fromJson(objAsJson, GameObject.class);

        obj.generateUid();
        for (Component c : obj.getAllComponents()) {
            c.generateId();
        }

        SpriteRenderer sprite = obj.getComponent(SpriteRenderer.class);
        if (sprite != null && sprite.getTexture() != null) {
            sprite.setTexture(AssetPool.getTexture(sprite.getTexture().getFilepath()));
        }

        return obj;
    }

    public void start(){
        for (int i = 0; i < components.size(); i++){
            components.get(i).start();
        }
    }

    public void imgui(){
        for (Component c : components){
            if (ImGui.collapsingHeader(c.getClass().getSimpleName()))
                c.imgui();
        }
    }

    public void destroy(){
        isDead = true;
        for (Component component : components) {
            component.destroy();
        }
    }

    public boolean isDead(){
        return isDead;
    }
    public static void init(int maxId){
        ID_COUNTER = maxId;
    }


    public int getUid(){
        return uid;
    }
    public void generateUid(){
        uid = ID_COUNTER++;
    }
    public List<Component> getAllComponents(){
        return components;
    }

    public void setNoSerialize(){
        doSerialisation = false;
    }

    public boolean doSerialisation(){
        return doSerialisation;
    }

    public void setSerializable(){
        doSerialisation = true;
    }

}
