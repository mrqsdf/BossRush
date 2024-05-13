package fr.mrqsdf.bossrush.component;

import fr.mrqsdf.bossrush.animation.player.PlayerAnimationTrigger;
import fr.mrqsdf.bossrush.res.GameState;
import fr.mrqsdf.engine2d.components.Component;
import fr.mrqsdf.engine2d.components.StateMachine;
import fr.mrqsdf.engine2d.jade.GameObject;
import fr.mrqsdf.engine2d.jade.KeyListener;

import static org.lwjgl.glfw.GLFW.*;

public class KeyControls extends Component {

    @Override
    public void update(float dt){
        if (KeyListener.keyBeginPress(GLFW_KEY_D)){
            GameState.gameState = GameState.MOVE;
        }

        if (KeyListener.keyBeginPress(GLFW_KEY_A)){
            GameCamera gameCamera = gameObject.getComponent(GameCamera.class);
            StateMachine stateMachine = gameCamera.cameraGameObject.getComponent(StateMachine.class);
            stateMachine.trigger(PlayerAnimationTrigger.IDLE);
        }
        if (KeyListener.keyBeginPress(GLFW_KEY_B)){
            GameCamera gameCamera = gameObject.getComponent(GameCamera.class);
            StateMachine stateMachine = gameCamera.cameraGameObject.getComponent(StateMachine.class);
            stateMachine.trigger(PlayerAnimationTrigger.ATTACK);
        }
        if (KeyListener.keyBeginPress(GLFW_KEY_N)){
            GameCamera gameCamera = gameObject.getComponent(GameCamera.class);
//            MobsComponent playerComponent = gameCamera.getActualEnemy().getComponent(MobsComponent.class);
            PlayerComponent playerComponent = gameCamera.cameraGameObject.getComponent(PlayerComponent.class);
            playerComponent.receiveDamage(10);
        }
        if (KeyListener.keyBeginPress(GLFW_KEY_M)){
            GameCamera gameCamera = gameObject.getComponent(GameCamera.class);
//            MobsComponent playerComponent = gameCamera.getActualEnemy().getComponent(MobsComponent.class);
            PlayerComponent playerComponent = gameCamera.cameraGameObject.getComponent(PlayerComponent.class);
            playerComponent.setHeal(Math.min(playerComponent.getMaxHeal(),playerComponent.getHeal()+1));
        }

    }

}
