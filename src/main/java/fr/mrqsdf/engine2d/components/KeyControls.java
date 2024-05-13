package fr.mrqsdf.engine2d.components;

import fr.mrqsdf.engine2d.editor.PropertiesWindows;
import fr.mrqsdf.engine2d.jade.GameObject;
import fr.mrqsdf.engine2d.jade.KeyListener;
import fr.mrqsdf.engine2d.jade.Window;
import fr.mrqsdf.engine2d.utils.EngineSettings;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class KeyControls extends Component{

    @Override
    public void editorUpdate(float dt){
        PropertiesWindows propertiesWindow = Window.getImGuiLayer().getPropertiesWindows();
        GameObject activeGameObject = propertiesWindow.getActiveGameObject();
        List<GameObject> activeGameObjects = propertiesWindow.getActiveGameObjects();
        if (KeyListener.isKeyPressed(GLFW_KEY_LEFT_CONTROL)){
            if (KeyListener.keyBeginPress(GLFW_KEY_D) && activeGameObject != null){
                GameObject newObj = activeGameObject.copy();
                Window.getScene().addGameObjectToScene(newObj);
                newObj.transform.position.add(EngineSettings.GRID_WIDTH, 0.0f);
                propertiesWindow.setActiveGameObject(newObj);
            }
            else if (KeyListener.keyBeginPress(GLFW_KEY_D) && activeGameObjects.size() > 1){
                List<GameObject> gameObjects = new ArrayList<>(activeGameObjects);
                propertiesWindow.clearSelected();
                for (GameObject go : gameObjects){
                    GameObject copy = go.copy();
                    Window.getScene().addGameObjectToScene(copy);
                    propertiesWindow.addActiveGameObject(copy);
                }
            } else if (KeyListener.keyBeginPress(GLFW_KEY_C) && activeGameObject != null){
                GameObject newObj = activeGameObject.copy();
                MouseControls mouseControls = Window.getScene().getLevelEditorStuff().getComponent(MouseControls.class);
                mouseControls.pickUpObject(newObj);
            }
        }
        else if (KeyListener.keyBeginPress(GLFW_KEY_DELETE)) {
            for (GameObject go : activeGameObjects) {
                go.destroy();
            }
            propertiesWindow.clearSelected();
        }
    }

}
