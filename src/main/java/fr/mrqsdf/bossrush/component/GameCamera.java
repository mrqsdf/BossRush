package fr.mrqsdf.bossrush.component;

import fr.mrqsdf.bossrush.animation.player.PlayerAnimationTrigger;
import fr.mrqsdf.bossrush.res.GameState;
import fr.mrqsdf.engine2d.components.Component;
import fr.mrqsdf.engine2d.components.HudComponent;
import fr.mrqsdf.engine2d.components.StateMachine;
import fr.mrqsdf.engine2d.editor.AssetsWindow;
import fr.mrqsdf.engine2d.jade.*;
import fr.mrqsdf.engine2d.utils.EngineSettings;

import java.util.ArrayList;
import java.util.List;

public class GameCamera extends Component {

    public transient GameObject cameraGameObject;
    private transient Camera gameCamera;
    private transient float highestX = Float.MIN_VALUE;
    private transient float highestY = 0.25f;
    private transient float zoom = 0.75f;
    private transient List<GameObject> hudGameObjects = new ArrayList<>();
    private final transient float moveLeftMax = 5;
    private transient float moveLeft = moveLeftMax;
    public boolean gameover = false;

    private transient GameObject actualEnemy;



    public GameCamera(Camera gameCamera) {
        this.gameCamera = gameCamera;
    }

    @Override
    public void start(){
        this.cameraGameObject = Window.getScene().getGameObjectWithComponent(PlayerComponent.class);
        hudGameObjects = Window.getScene().getGameObjects().stream().filter(go -> go.getComponent(HudComponent.class) != null && !hudGameObjects.contains(go)).toList(); //todo opti ici
        gameCamera.position.y = highestY;
        gameCamera.position.x = 1.5f;
        gameCamera.setZoom(zoom);
    }

    @Override
    public void update(float dt){
        if (GameState.gameState == GameState.MOVE){
            gameCamera.position.x += 0.2f;
            if (cameraGameObject != null){
                cameraGameObject.transform.position.x += 0.2f;
            }
            for (GameObject go : hudGameObjects){
                go.getComponent(HudComponent.class).moveHud(0.2f,0);
            }
            moveLeft -=0.2f;
            if (moveLeft <= 0) {
                GameState.gameState = GameState.WAIT;
                moveLeft = moveLeftMax;
                cameraGameObject.getComponent(StateMachine.class).trigger(PlayerAnimationTrigger.IDLE);
            }
        }
        else {
            if (!getActualEnemy().getComponent(EntityComponent.class).isAlive()){
                setActualEnemy(null);
                GameState.gameState = GameState.MOVE;
            } else if (!cameraGameObject.getComponent(EntityComponent.class).isAlive() && !gameover) {
                System.out.println("Game Over");
                gameover = true;
                Transform transform = cameraGameObject.transform.copy();
                transform.position.x += 1.75f;
                transform.position.y += 0.75f;
                GameObject gameOver = Prefabs.generateSpriteObject(AssetsWindow.getSpriteSheet("assets/spritesheets/hud/GameOver.spsheet").getSprite(0), 2,0.5f,transform.position.x,transform.position.y,0,10);
                gameOver.setNoSerialize();
                Window.getScene().addGameObjectToScene(gameOver);
            }
        }
    }

    public void setActualEnemy(GameObject actualEnemy) {
        this.actualEnemy = actualEnemy;
    }

    public GameObject getActualEnemy() {
        return actualEnemy;
    }

}
