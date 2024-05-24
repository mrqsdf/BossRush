package fr.mrqsdf.bossrush.component;

import fr.mrqsdf.bossrush.animation.player.PlayerAnimationTrigger;
import fr.mrqsdf.bossrush.res.*;
import fr.mrqsdf.bossrush.scene.GamePlay;
import fr.mrqsdf.bossrush.scene.GameSceneInitializer;
import fr.mrqsdf.bossrush.util.Utils;
import fr.mrqsdf.engine2d.components.Component;
import fr.mrqsdf.engine2d.components.StateMachine;
import fr.mrqsdf.engine2d.jade.GameObject;
import fr.mrqsdf.engine2d.jade.MouseListener;
import fr.mrqsdf.engine2d.jade.Window;
import fr.mrqsdf.engine2d.renderer.PickingTexture;
import fr.mrqsdf.engine2d.scenes.Scene;

import java.util.Random;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class MouseControls extends Component {

    private float debounceTime = 0.2f;
    private float debounce = debounceTime;

    private float switchGameStatesTime = 0.8f;
    private float switchGameStates = switchGameStatesTime;

    private DisplayItem displayItem = null;

    @Override
    public void update(float dt){
        if (displayItem == null) displayItem = GamePlay.displayItem;
        debounce -= dt;
        if (GameState.gameState != GameState.MOB_ACTION) switchGameStates -= dt;
        if (switchGameStates < 0 ){
            if (GameState.gameState == GameState.PLAYER_ACTION){
                GameState.gameState = GameState.MOB_ACTION;
                switchGameStates = switchGameStatesTime;
            }
            if (GameState.gameState != GameState.WAIT && GameState.MOVE != GameState.gameState && GameState.gameState != GameState.MOB_ACTION){
                GameState.gameState = GameState.WAIT;
            }
        }
        PickingTexture pickingTexture = Window.get().pickingTexture;
        Scene currentScene = Window.getScene();
        if (!MouseListener.isDragging() && MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT) && debounce < 0){
            int x = (int)MouseListener.getScreenX();
            int y = (int)MouseListener.getScreenY();
            System.out.println(x + " " + y);
            int gameObjectId = pickingTexture.readPixel(x, y);
            System.out.println(gameObjectId);
            GameObject pickedObj = currentScene.getGameObject(gameObjectId);
            GameObject inventory = currentScene.getGameObjectWithComponent(InventoryComponent.class);
            System.out.println(pickedObj);
            if (pickedObj != null){
                System.out.println(pickedObj.name);
                DisplayComponent displayComponent = pickedObj.getComponent(DisplayComponent.class);
                if (displayComponent == null || displayComponent.displayState != DisplayState.USABLE){
                    displayItem.hide();
                }
                if (displayComponent != null){
                    System.out.println(displayComponent.displayState);
                    GameCamera gameCamera = gameObject.getComponent(GameCamera.class);
                    StateMachine stateMachine = gameCamera.cameraGameObject.getComponent(StateMachine.class);
                    if (!gameCamera.gameover && GameState.gameState == GameState.WAIT){
                        switch (displayComponent.displayState){
                            case ATTACK -> {
                                System.out.println("Player Attack");
                                GameState.gameState = GameState.PLAYER_ACTION;
                                stateMachine.trigger(PlayerAnimationTrigger.ATTACK);
                                double rd = new Random().nextDouble();
                                boolean isCritical = rd < gameCamera.cameraGameObject.getComponent(EntityComponent.class).criticalChance;
                                Utils.attack(gameCamera.cameraGameObject, gameCamera.getActualEnemy(),isCritical);
                                switchGameStates = switchGameStatesTime;
                                if (!gameCamera.getActualEnemy().getComponent(EntityComponent.class).isAlive()) GameState.gameState = GameState.WAIT;
                            }
                            case DEFEND -> {
                                System.out.println("Player Defend");
                                GameState.gameState = GameState.PLAYER_ACTION;
                                stateMachine.trigger(PlayerAnimationTrigger.DEFEND);
                                switchGameStates = switchGameStatesTime;
                            }
                            case INVENTORY -> {
                                System.out.println("Player open inventory");
                            }
                            case ITEM -> {
                                displayItem.show(pickedObj);
                                System.out.println("Player select item");
                            }
                            case USABLE -> {
                                System.out.println("Player usable");
                                if (displayItem.item != null) displayItem.use(gameCamera, inventory);
                            }

                        }
                    } else if (gameCamera.gameover) {
                        switch (displayComponent.displayState){
                            case GAME_OVER -> {
                                System.out.println("Player reset");
                                Window.changeScene(new GameSceneInitializer(), 0);
                            }
                        }
                    }
                }
            }
            debounce = debounceTime;
        }
    }

}
