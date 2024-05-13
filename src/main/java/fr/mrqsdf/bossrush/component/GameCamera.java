package fr.mrqsdf.bossrush.component;

import fr.mrqsdf.bossrush.animation.mobs.SlimeAnimation;
import fr.mrqsdf.bossrush.animation.player.PlayerAnimationTrigger;
import fr.mrqsdf.bossrush.res.DisplayState;
import fr.mrqsdf.bossrush.res.GameState;
import fr.mrqsdf.bossrush.res.MobAction;
import fr.mrqsdf.bossrush.scene.GameSceneInitializer;
import fr.mrqsdf.bossrush.util.Algorithme;
import fr.mrqsdf.bossrush.util.Utils;
import fr.mrqsdf.engine2d.components.Component;
import fr.mrqsdf.engine2d.components.HudComponent;
import fr.mrqsdf.engine2d.components.StateMachine;
import fr.mrqsdf.engine2d.editor.AssetsWindow;
import fr.mrqsdf.engine2d.jade.*;

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
                GameObject mobHealHud = Window.getScene().createGameObject("MobHealHud");
                mobHealHud.setNoSerialize();
                HudComponent mobHealHudComponent = new HudComponent();
                GameSceneInitializer.drawDisplay("MobHealHudFull",cameraGameObject.transform.position.x + 3.5f,2.175f,-1f,0.175f, mobHealHudComponent, Window.getScene(), DisplayState.MOB_HEAL_HUD_FULL,"assets/spritesheets/hud/HealBar.spsheet", 5);
                GameSceneInitializer.drawDisplay("MobHealHudEmpty",cameraGameObject.transform.position.x + 3.5f,2.175f,-1f,0.175f, mobHealHudComponent, Window.getScene(), DisplayState.MOB_HEAL_HUD_EMPTY,"assets/spritesheets/hud/HealBarEmpty.spsheet",4);

                mobHealHud.addComponent(mobHealHudComponent);
                Window.getScene().addGameObjectToScene(mobHealHud);

                GameObject slime = SlimeAnimation.playerGameObject(
                        AssetsWindow.getSpriteSheet("assets/spritesheets/entity/slime/SlimeSpriteSheet.spsheet"),
                        1f,1,cameraGameObject.transform.position.x + 3.5f,0.73f);
                slime.setNoSerialize();
                Window.getScene().addGameObjectToScene(slime);

                setActualEnemy(slime);
            }
        }
        else if (GameState.gameState == GameState.WAIT){
            if (getActualEnemy() != null && !getActualEnemy().getComponent(EntityComponent.class).isAlive()){
                getActualEnemy().destroy();
                for (GameObject go : Window.getScene().getGameObjectsWithComponent(DisplayComponent.class)){
                    if (go.getComponent(DisplayComponent.class).displayState == DisplayState.MOB_HEAL_HUD_FULL){
                        go.destroy();
                    }
                    if (go.getComponent(DisplayComponent.class).displayState == DisplayState.MOB_HEAL_HUD_EMPTY){
                        go.destroy();
                    }
                    if (go.getComponent(DisplayComponent.class).displayState == DisplayState.MOB_MANA_HUD_FULL){
                        go.destroy();
                    }
                    if (go.getComponent(DisplayComponent.class).displayState == DisplayState.MOB_MANA_HUD_EMPTY){
                        go.destroy();
                    }
                }
                setActualEnemy(null);
                GameState.gameState = GameState.MOVE;
                cameraGameObject.getComponent(StateMachine.class).trigger(PlayerAnimationTrigger.RUN);
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
        else if (GameState.gameState == GameState.MOB_ACTION) {
            MobAction mobAction = Algorithme.mobAlgorithm(getActualEnemy(), cameraGameObject);
            if (mobAction == MobAction.ATTACK){
                Utils.attack(getActualEnemy(), cameraGameObject, false);
            }
            else if (mobAction == MobAction.CRITICAL){
                Utils.attack(getActualEnemy(), cameraGameObject, true);
            } else if (mobAction == MobAction.HEAL) {
                getActualEnemy().getComponent(MobsComponent.class).receiveHeal(getActualEnemy().getComponent(MobsComponent.class).healRegen);
            }
            GameState.gameState = GameState.WAIT;
        }
    }

    public void setActualEnemy(GameObject actualEnemy) {
        this.actualEnemy = actualEnemy;
    }

    public GameObject getActualEnemy() {
        return actualEnemy;
    }

}
