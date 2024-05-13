package fr.mrqsdf.bossrush.component;

import fr.mrqsdf.bossrush.animation.player.PlayerAnimationTrigger;
import fr.mrqsdf.bossrush.res.DisplayState;
import fr.mrqsdf.bossrush.res.GameState;
import fr.mrqsdf.bossrush.util.Utils;
import fr.mrqsdf.engine2d.components.Component;
import fr.mrqsdf.engine2d.components.StateMachine;
import fr.mrqsdf.engine2d.jade.GameObject;
import fr.mrqsdf.engine2d.jade.MouseListener;
import fr.mrqsdf.engine2d.jade.Window;
import fr.mrqsdf.engine2d.renderer.PickingTexture;
import fr.mrqsdf.engine2d.scenes.Scene;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class MouseControls extends Component {

    private float debounceTime = 0.2f;
    private float debounce = debounceTime;

    @Override
    public void update(float dt){
        debounce -= dt;
        PickingTexture pickingTexture = Window.getImGuiLayer().getPropertiesWindows().getPickingTexture();
        Scene currentScene = Window.getScene();
        if (!MouseListener.isDragging() && MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT) && debounce < 0){
            int x = (int)MouseListener.getScreenX();
            int y = (int)MouseListener.getScreenY();
            int gameObjectId = pickingTexture.readPixel(x, y);
            GameObject pickedObj = currentScene.getGameObject(gameObjectId);
            if (pickedObj != null){
                DisplayComponent displayComponent = pickedObj.getComponent(DisplayComponent.class);
                if (displayComponent != null){
                    GameCamera gameCamera = gameObject.getComponent(GameCamera.class);
                    StateMachine stateMachine = gameCamera.cameraGameObject.getComponent(StateMachine.class);
                    switch (displayComponent.displayState){
                        case ATTACK -> {
                            System.out.println("Player Attack");
                            GameState.gameState = GameState.PLAYER_ACTION;
                            stateMachine.trigger(PlayerAnimationTrigger.ATTACK);
                            Utils.attack(gameCamera.cameraGameObject, gameCamera.getActualEnemy());
                        }
                        case DEFEND -> {
                            System.out.println("Player Defend");
                            GameState.gameState = GameState.PLAYER_ACTION;
                            stateMachine.trigger(PlayerAnimationTrigger.DEFEND);
                        }
                        case INVENTORY -> {
                            System.out.println("Player open inventory");
                        }
                        case MOVE -> {
                            System.out.println("Player Move");
                            GameState.gameState = GameState.MOVE;
                            stateMachine.trigger(PlayerAnimationTrigger.RUN);
                        }
                    }
                }
            }
            debounce = debounceTime;
        }
    }

}
