package fr.mrqsdf.bossrush.component;

import fr.mrqsdf.bossrush.animation.player.PlayerAnimationTrigger;
import fr.mrqsdf.bossrush.res.GameState;
import fr.mrqsdf.bossrush.res.ItemType;
import fr.mrqsdf.bossrush.res.PotionType;
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

    @Override
    public void update(float dt){
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
                DisplayComponent displayComponent = pickedObj.getComponent(DisplayComponent.class);
                if (displayComponent != null){
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
                                System.out.println("Player use item");
                                ItemComponent itemComponent = pickedObj.getComponent(ItemComponent.class);
                                InventoryComponent inventoryComponent = inventory.getComponent(InventoryComponent.class);
                                itemComponent.use(gameCamera);
                                inventoryComponent.removeItem(pickedObj);
                                pickedObj.destroy();
                            }
                        }
                    }
                }
            }
            debounce = debounceTime;
        }
    }

}
