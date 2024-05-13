package fr.mrqsdf.bossrush.animation.mobs.slime;

import fr.mrqsdf.bossrush.animation.player.PlayerAnimationType;
import fr.mrqsdf.bossrush.component.MobsComponent;
import fr.mrqsdf.bossrush.res.EntityType;
import fr.mrqsdf.engine2d.components.AnimationState;
import fr.mrqsdf.engine2d.components.SpriteSheet;
import fr.mrqsdf.engine2d.components.StateMachine;
import fr.mrqsdf.engine2d.components.StateTrigger;
import fr.mrqsdf.engine2d.jade.GameObject;
import fr.mrqsdf.engine2d.jade.Prefabs;

import java.util.ArrayList;
import java.util.List;

public class SlimeAnimation {

    private static boolean initialized = false;
    public static List<AnimationState> animationStates = new ArrayList<>();


    public static void setAnimation(SpriteSheet spriteSheet) {
        AnimationState idle = new AnimationState();
        idle.title = SlimeAnimationType.IDLE.getName();
        idle.animationTypeName = SlimeAnimationType.IDLE.getName();
        idle.setLoop(true);
        for (int i = 0; i < 4; i++) {
            idle.addFrame(spriteSheet.getSprite(i), 0.2f);
        }
        animationStates.add(idle);
        AnimationState attack = new AnimationState();
        attack.title = SlimeAnimationType.ATTACK.getName();
        attack.animationTypeName = SlimeAnimationType.ATTACK.getName();
        attack.setLoop(false);
        for (int i = 16; i < 20; i++) {
            attack.addFrame(spriteSheet.getSprite(i), 0.2f);
        }
        animationStates.add(attack);
        AnimationState takingDamage = new AnimationState();
        takingDamage.title = SlimeAnimationType.TAKING_DAMAGE.getName();
        takingDamage.animationTypeName = SlimeAnimationType.TAKING_DAMAGE.getName();
        takingDamage.setLoop(false);
        for (int i = 20; i < 24; i++) {
            takingDamage.addFrame(spriteSheet.getSprite(i), 0.2f);
        }
        animationStates.add(takingDamage);
        AnimationState death = new AnimationState();
        death.title = SlimeAnimationType.DEATH.getName();
        death.animationTypeName = SlimeAnimationType.DEATH.getName();
        death.setLoop(false);
        for (int i = 24; i < 27; i++) {
            death.addFrame(spriteSheet.getSprite(i), 0.15f);
        }
        animationStates.add(death);
    }

    public static GameObject playerGameObject(SpriteSheet spriteSheet, float sizeX, float sizeY, float pos1, float pos2) {
        GameObject go = Prefabs.generateSpriteObject(spriteSheet.getSprite(0), -sizeX, sizeY, pos1, pos2, 0, 5);
        if (!initialized) {
            setAnimation(spriteSheet);
            initialized = true;
        }
        StateMachine stateMachine = new StateMachine();
        stateMachine.setStates(animationStates);
        stateMachine.setDefaultState(SlimeAnimationType.IDLE);

        stateMachine.addStateTrigger(new StateTrigger(SlimeAnimationType.IDLE, SlimeAnimationTrigger.ATTACK, 1), SlimeAnimationType.ATTACK);
        stateMachine.addStateTrigger(new StateTrigger(SlimeAnimationType.IDLE, SlimeAnimationTrigger.TAKING_DAMAGE, 1), SlimeAnimationType.TAKING_DAMAGE);
        stateMachine.addStateTrigger(new StateTrigger(SlimeAnimationType.IDLE, SlimeAnimationTrigger.DEATH, 1), SlimeAnimationType.DEATH);

        stateMachine.refreshTextures();
        go.addComponent(stateMachine);
        go.addComponent(new MobsComponent(EntityType.SLIME, 1));
        return go;
    }

}
