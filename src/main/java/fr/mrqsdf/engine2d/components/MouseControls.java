package fr.mrqsdf.engine2d.components;

import fr.mrqsdf.engine2d.jade.GameObject;
import fr.mrqsdf.engine2d.jade.KeyListener;
import fr.mrqsdf.engine2d.jade.MouseListener;
import fr.mrqsdf.engine2d.jade.Window;
import fr.mrqsdf.engine2d.renderer.DebugDraw;
import fr.mrqsdf.engine2d.renderer.PickingTexture;
import fr.mrqsdf.engine2d.scenes.Scene;
import fr.mrqsdf.engine2d.utils.EngineSettings;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector4f;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.lwjgl.glfw.GLFW.*;

public class MouseControls extends Component {

    public GameObject holdingObject = null;
    public int holdingZIndex = 0;
    private float debounceTime = 0.2f;
    private float debounce = debounceTime;
    private float rotateTime = 0.15f;
    private float rotate = rotateTime;
    private boolean boxSelectSet = false;
    private Vector2f boxSelectStart = new Vector2f();
    private Vector2f boxSelectEnd = new Vector2f();

    public void pickUpObject(GameObject go){
        if (holdingObject != null){
            holdingObject.destroy();
            holdingZIndex = 0;
        }
        holdingObject = go;
        holdingObject.getComponent(SpriteRenderer.class).setColor(new Vector4f(0.8f,0.8f,0.8f,0.5f));
        if (holdingObject.getComponent(NonPickable.class) == null)holdingObject.addComponent(new NonPickable());
        holdingObject.setNoSerialize();
        holdingZIndex = holdingObject.transform.zIndex;
        holdingObject.transform.zIndex = Integer.MAX_VALUE;
        Window.getScene().addGameObjectToScene(go);
    }

    public void place(){
        GameObject newObject = holdingObject.copy();
        if (newObject.getComponent(StateMachine.class) != null) {
            newObject.getComponent(StateMachine.class).refreshTextures();
        }
        newObject.getComponent(SpriteRenderer.class).setColor(new Vector4f(1, 1, 1, 1));
        newObject.removeComponent(NonPickable.class);
        newObject.setSerializable();
        newObject.transform.zIndex = holdingZIndex;
        Window.getScene().addGameObjectToScene(newObject);
    }

    @Override
    public void editorUpdate(float dt) {
        debounce -= dt;
        rotate -= dt;
        PickingTexture pickingTexture = Window.getImGuiLayer().getPropertiesWindows().getPickingTexture();
        Scene currentScene = Window.getScene();
        if (holdingObject != null) {
            float x = MouseListener.getWorldX();
            float y = MouseListener.getWorldY();
            holdingObject.transform.position.x = ((int)Math.floor(x / EngineSettings.GRID_WIDTH) * EngineSettings.GRID_WIDTH) + EngineSettings.GRID_WIDTH / 2.0f;
            holdingObject.transform.position.y = ((int)Math.floor(y / EngineSettings.GRID_HEIGHT) * EngineSettings.GRID_HEIGHT) + EngineSettings.GRID_HEIGHT / 2.0f;
            if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
                boolean canPlace = blockInSquare(holdingObject.transform.position.x,
                        holdingObject.transform.position.y);
                if (MouseListener.isDragging() &&
                        canPlace) {
                    place();
                    debounce = debounceTime;
                } else if (!MouseListener.isDragging() && debounce < 0) {
                    place();
                    debounce = debounceTime;
                }
            }
            if (rotate <= 0.0f){
                if (KeyListener.isKeyPressed(GLFW_KEY_LEFT_SHIFT)|| KeyListener.isKeyPressed(GLFW_KEY_RIGHT_SHIFT)) {
                    if (KeyListener.keyBeginPress(GLFW_KEY_R)){
                        holdingObject.transform.rotation +=90;
                        rotate = rotateTime;
                    }
                }
                else if (KeyListener.keyBeginPress(GLFW_KEY_R)){
                    holdingObject.transform.rotation -= 90;
                    rotate = rotateTime;
                }
            }
            if (KeyListener.isKeyPressed(GLFW_KEY_ESCAPE)){
                holdingObject.destroy();
                holdingObject = null;
            }

        } else if (!MouseListener.isDragging() && MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT) && debounce < 0) {
            int x = (int)MouseListener.getScreenX();
            int y = (int)MouseListener.getScreenY();
            int gameObjectId = pickingTexture.readPixel(x, y);
            GameObject pickedObj = currentScene.getGameObject(gameObjectId);
            if (pickedObj != null && pickedObj.getComponent(NonPickable.class) == null) {
                Window.getImGuiLayer().getPropertiesWindows().setActiveGameObject(pickedObj);
            } else if (pickedObj == null && !MouseListener.isDragging()) {
                Window.getImGuiLayer().getPropertiesWindows().clearSelected();
            }
            this.debounce = 0.2f;
        } else if (MouseListener.isDragging() && MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
            if (!boxSelectSet) {
                Window.getImGuiLayer().getPropertiesWindows().clearSelected();
                boxSelectStart = MouseListener.getScreen();
                boxSelectSet = true;
            }
            boxSelectEnd = MouseListener.getScreen();
            Vector2f boxSelectStartWorld = MouseListener.screenToWorld(boxSelectStart);
            Vector2f boxSelectEndWorld = MouseListener.screenToWorld(boxSelectEnd);
            Vector2f halfSize =
                    (new Vector2f(boxSelectEndWorld).sub(boxSelectStartWorld)).mul(0.5f);
            DebugDraw.addBox2D(
                    (new Vector2f(boxSelectStartWorld)).add(halfSize),
                    new Vector2f(halfSize).mul(2.0f),
                    0.0f);
        } else if (boxSelectSet) {
            boxSelectSet = false;
            int screenStartX = (int)boxSelectStart.x;
            int screenStartY = (int)boxSelectStart.y;
            int screenEndX = (int)boxSelectEnd.x;
            int screenEndY = (int)boxSelectEnd.y;
            boxSelectStart.zero();
            boxSelectEnd.zero();

            if (screenEndX < screenStartX) {
                int tmp = screenStartX;
                screenStartX = screenEndX;
                screenEndX = tmp;
            }
            if (screenEndY < screenStartY) {
                int tmp = screenStartY;
                screenStartY = screenEndY;
                screenEndY = tmp;
            }

            float[] gameObjectIds = pickingTexture.readPixels(
                    new Vector2i(screenStartX, screenStartY),
                    new Vector2i(screenEndX, screenEndY)
            );
            Set<Integer> uniqueGameObjectIds = new HashSet<>();
            for (float objId : gameObjectIds) {
                uniqueGameObjectIds.add((int)objId);
            }

            for (Integer gameObjectId : uniqueGameObjectIds) {
                GameObject pickedObj = Window.getScene().getGameObject(gameObjectId);
                if (pickedObj != null && pickedObj.getComponent(NonPickable.class) == null) {
                    Window.getImGuiLayer().getPropertiesWindows().addActiveGameObject(pickedObj);
                }
            }
        }
    }

    private boolean blockInSquare(float x, float y) {
        List<GameObject> gos = Window.getScene().getGameObjects(x, y);
        if (gos.isEmpty()) return true;
        for (GameObject go : gos){
            if (go.transform.zIndex == Integer.MAX_VALUE) continue;
            return false;
        }
        return true;
    }

}
