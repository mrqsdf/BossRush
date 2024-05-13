package fr.mrqsdf.bossrush.animation.player;

import fr.mrqsdf.bossrush.component.PlayerComponent;
import fr.mrqsdf.engine2d.components.AnimationState;
import fr.mrqsdf.engine2d.components.SpriteSheet;
import fr.mrqsdf.engine2d.components.StateMachine;
import fr.mrqsdf.engine2d.components.StateTrigger;
import fr.mrqsdf.engine2d.jade.GameObject;
import fr.mrqsdf.engine2d.jade.Prefabs;

import java.util.ArrayList;
import java.util.List;

public class PlayerAnimation {

    public static boolean isLoad = false;
    public static AnimationState idle = new AnimationState();
    public static AnimationState walk = new AnimationState();
    public static AnimationState run = new AnimationState();
    public static AnimationState attack = new AnimationState();
    public static AnimationState comboAttack = new AnimationState();
    public static AnimationState takingDamage = new AnimationState();
    public static AnimationState spellCast = new AnimationState();
    public static AnimationState death = new AnimationState();
    public static AnimationState defend = new AnimationState();
    public static List<AnimationState> playerAnimation = new ArrayList<>();

    public static void setAnimation(SpriteSheet spriteSheet1, SpriteSheet spriteSheet2) {
        idle.title = PlayerAnimationType.IDLE.name;
        for (int i = 0; i < 6; i++) {
            idle.addFrame(spriteSheet1.getSprite(i), 0.1f);
        }
        idle.animationTypeName = PlayerAnimationType.IDLE.getName();
        idle.setLoop(true);
        walk.title = PlayerAnimationType.WALK.name;
        for (int i = 0; i < 10; i++) {
            walk.addFrame(spriteSheet2.getSprite(i), 0.1f);
        }
        walk.animationTypeName = PlayerAnimationType.WALK.getName();
        walk.setLoop(true);
        run.title = PlayerAnimationType.RUN.name;
        for (int i = 16; i < 16+8; i++) {
            run.addFrame(spriteSheet1.getSprite(i), 0.1f);
        }
        run.animationTypeName = PlayerAnimationType.RUN.getName();
        run.setLoop(true);
        attack.title = PlayerAnimationType.ATTACK.name;
        for (int i = 8; i < 8+6; i++) {
            attack.addFrame(spriteSheet1.getSprite(i), 0.1f);
        }
        attack.animationTypeName = PlayerAnimationType.ATTACK.getName();
        attack.setLoop(false);
        comboAttack.title = PlayerAnimationType.COMBO_ATTACK.name;
        for (int i = 8; i < 8+8; i++) {
            comboAttack.addFrame(spriteSheet1.getSprite(i), 0.1f);
        }
        comboAttack.animationTypeName = PlayerAnimationType.COMBO_ATTACK.getName();
        comboAttack.setLoop(false);
        takingDamage.title = PlayerAnimationType.TAKING_DAMAGE.name;
        for (int i = 40; i < 44; i++) {
            takingDamage.addFrame(spriteSheet1.getSprite(i), 0.1f);
        }
        takingDamage.animationTypeName = PlayerAnimationType.TAKING_DAMAGE.getName();
        takingDamage.setLoop(false);
        death.title = PlayerAnimationType.DEATH.name;
        for (int i = 48; i < 48+11; i++) {
            death.addFrame(spriteSheet1.getSprite(i), 0.1f);
        }
        death.animationTypeName = PlayerAnimationType.DEATH.getName();
        death.setLoop(false);
        spellCast.title = PlayerAnimationType.SPELL_CAST.name;
        for (int i = 53; i < 53+8; i++) {
            spellCast.addFrame(spriteSheet1.getSprite(i), 0.1f);
        }
        spellCast.animationTypeName = PlayerAnimationType.SPELL_CAST.getName();
        spellCast.setLoop(false);
        defend.title = PlayerAnimationType.DEFEND.name;
        for (int i = 81; i < 83; i++) {
            defend.addFrame(spriteSheet1.getSprite(i), 0.1f);
        }
        defend.animationTypeName = PlayerAnimationType.DEFEND.getName();
        defend.setLoop(false);
        addPlayerAnimation();

    }

    public static GameObject playerGameObject(SpriteSheet spriteSheet1, SpriteSheet spriteSheet2, float sizeX, float sizeY, float pos1, float pos2){
        GameObject go = Prefabs.generateSpriteObject(spriteSheet1.getSprite(0), sizeX, sizeY,pos1,pos2,0,5);
        if (!isLoad) {
            setAnimation(spriteSheet1, spriteSheet2);
            isLoad = true;
        }
        StateMachine stateMachine = new StateMachine();
        stateMachine.setStates(playerAnimation);
        stateMachine.setDefaultState(PlayerAnimationType.IDLE);

        stateMachine.addStateTrigger(new StateTrigger(PlayerAnimationType.RUN, PlayerAnimationTrigger.IDLE,0), PlayerAnimationType.IDLE);
        stateMachine.addStateTrigger(new StateTrigger(PlayerAnimationType.RUN, PlayerAnimationTrigger.WALK,1), PlayerAnimationType.WALK);
        stateMachine.addStateTrigger(new StateTrigger(PlayerAnimationType.IDLE, PlayerAnimationTrigger.RUN,2), PlayerAnimationType.RUN);
        stateMachine.addStateTrigger(new StateTrigger(PlayerAnimationType.WALK, PlayerAnimationTrigger.RUN,3), PlayerAnimationType.RUN);
        stateMachine.addStateTrigger(new StateTrigger(PlayerAnimationType.IDLE, PlayerAnimationTrigger.WALK,4), PlayerAnimationType.WALK);
        stateMachine.addStateTrigger(new StateTrigger(PlayerAnimationType.WALK, PlayerAnimationTrigger.IDLE,5), PlayerAnimationType.IDLE);
        stateMachine.addStateTrigger(new StateTrigger(PlayerAnimationType.IDLE, PlayerAnimationTrigger.ATTACK,6), PlayerAnimationType.ATTACK);
        stateMachine.addStateTrigger(new StateTrigger(PlayerAnimationType.ATTACK, PlayerAnimationTrigger.IDLE,7), PlayerAnimationType.IDLE);
        stateMachine.addStateTrigger(new StateTrigger(PlayerAnimationType.IDLE, PlayerAnimationTrigger.COMBO_ATTACK,8), PlayerAnimationType.COMBO_ATTACK);
        stateMachine.addStateTrigger(new StateTrigger(PlayerAnimationType.COMBO_ATTACK, PlayerAnimationTrigger.IDLE,9), PlayerAnimationType.IDLE);
        stateMachine.addStateTrigger(new StateTrigger(PlayerAnimationType.IDLE, PlayerAnimationTrigger.TAKING_DAMAGE,10), PlayerAnimationType.TAKING_DAMAGE);
        stateMachine.addStateTrigger(new StateTrigger(PlayerAnimationType.TAKING_DAMAGE, PlayerAnimationTrigger.IDLE,11), PlayerAnimationType.IDLE);
        stateMachine.addStateTrigger(new StateTrigger(PlayerAnimationType.IDLE, PlayerAnimationTrigger.SPELL_CAST,12), PlayerAnimationType.SPELL_CAST);
        stateMachine.addStateTrigger(new StateTrigger(PlayerAnimationType.SPELL_CAST, PlayerAnimationTrigger.IDLE,13), PlayerAnimationType.IDLE);
        stateMachine.addStateTrigger(new StateTrigger(PlayerAnimationType.IDLE, PlayerAnimationTrigger.DEATH,14), PlayerAnimationType.DEATH);
        stateMachine.addStateTrigger(new StateTrigger(PlayerAnimationType.DEATH, PlayerAnimationTrigger.IDLE,15), PlayerAnimationType.IDLE);
        stateMachine.addStateTrigger(new StateTrigger(PlayerAnimationType.IDLE, PlayerAnimationTrigger.DEFEND,16), PlayerAnimationType.DEFEND);
        stateMachine.addStateTrigger(new StateTrigger(PlayerAnimationType.DEFEND, PlayerAnimationTrigger.IDLE,17), PlayerAnimationType.IDLE);
        stateMachine.refreshTextures();
        go.addComponent(stateMachine);
        go.addComponent(new PlayerComponent());
        return go;
    }
    private static void addPlayerAnimation() {
        playerAnimation.add(idle);
        playerAnimation.add(walk);
        playerAnimation.add(run);
        playerAnimation.add(attack);
        playerAnimation.add(comboAttack);
        playerAnimation.add(takingDamage);
        playerAnimation.add(spellCast);
        playerAnimation.add(death);
        playerAnimation.add(defend);
    }

}
