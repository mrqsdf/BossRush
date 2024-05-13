package fr.mrqsdf.engine2d.editor;

import fr.mrqsdf.engine2d.components.SpriteRenderer;
import fr.mrqsdf.engine2d.jade.GameObject;
import fr.mrqsdf.engine2d.renderer.PickingTexture;
import fr.mrqsdf.engine2d.utils.ComponentAdder;
import imgui.ImGui;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public class PropertiesWindows {

    private List<GameObject> activeGameObjects = new ArrayList<>();
    private List<Vector4f> activeGameObjectOgColor = new ArrayList<>();
    private GameObject activeGameObject = null;
    private PickingTexture pickingTexture;

    public PropertiesWindows(PickingTexture pickingTexture){
        this.pickingTexture = pickingTexture;
    }


    public void imgui(){
        if (activeGameObjects.size() == 1 && activeGameObjects.get(0) != null){
            activeGameObject = activeGameObjects.get(0);
            ImGui.begin("Properties");
            if (ImGui.beginPopupContextWindow("ComponentAdder")){
                for (ComponentAdder componentAdder : ComponentAdder.componentAdderList){
                    if (ImGui.menuItem("add " + componentAdder.name)){
                        if (activeGameObject.getComponent(componentAdder.component.getClass()) == null)
                            activeGameObject.addComponent(componentAdder.component);
                    }
                }
                ImGui.endPopup();
            }
            activeGameObject.imgui();
            ImGui.end();
        }
    }

    public PickingTexture getPickingTexture() {
        return this.pickingTexture;
    }

    public GameObject getActiveGameObject(){
        return activeGameObjects.size() == 1 ? activeGameObjects.get(0) : null;
    }

    public List<GameObject> getActiveGameObjects(){
        return activeGameObjects;
    }

    public void clearSelected(){
        if (!activeGameObjectOgColor.isEmpty()){
            int i = 0;
            for (GameObject go : activeGameObjects){
                SpriteRenderer spriteRenderer = go.getComponent(SpriteRenderer.class);
                if (spriteRenderer != null){
                    spriteRenderer.setColor(activeGameObjectOgColor.get(i));
                }
                i++;
            }
        }
        activeGameObjects.clear();
        activeGameObjectOgColor.clear();
    }

    public void setActiveGameObject(GameObject activeGameObject){
        if (activeGameObject != null){
            clearSelected();
            this.activeGameObjects.add(activeGameObject);
        }
    }

    public void addActiveGameObject(GameObject go){
        SpriteRenderer sprite = go.getComponent(SpriteRenderer.class);
        if (sprite !=null){
            this.activeGameObjectOgColor.add(new Vector4f(sprite.getColor()));
            sprite.setColor(new Vector4f(0.8f,0.8f,0,0.8f));
        } else {
            this.activeGameObjectOgColor.add(new Vector4f());
        }
        this.activeGameObjects.add(go);
    }


}
