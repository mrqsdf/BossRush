package fr.mrqsdf.bossrush.util;

import fr.mrqsdf.bossrush.animation.mobs.MobAnimationTrigger;
import fr.mrqsdf.bossrush.animation.player.PlayerAnimationTrigger;
import fr.mrqsdf.bossrush.component.EntityComponent;
import fr.mrqsdf.bossrush.component.PlayerComponent;
import fr.mrqsdf.engine2d.components.StateMachine;
import fr.mrqsdf.engine2d.jade.GameObject;

public class Utils {

    public static void attack(GameObject attacker, GameObject target, boolean isCritical){
        if (target != null && attacker != null){
            EntityComponent attackerComponent = attacker.getComponent(EntityComponent.class);
            EntityComponent targetComponent = target.getComponent(EntityComponent.class);
            targetComponent.receiveDamage((int) (attackerComponent.getDamage() * (isCritical ? attackerComponent.criticalDamage : 1)));
            if (target.getComponent(PlayerComponent.class) != null){
                if (targetComponent.isAlive()) target.getComponent(StateMachine.class).trigger(PlayerAnimationTrigger.TAKING_DAMAGE);
                else target.getComponent(StateMachine.class).trigger(PlayerAnimationTrigger.DEATH);
            } else {
                if (targetComponent.isAlive()) target.getComponent(StateMachine.class).trigger(MobAnimationTrigger.TAKING_DAMAGE);
                else target.getComponent(StateMachine.class).trigger(MobAnimationTrigger.DEATH);
            }
            if (attacker.getComponent(PlayerComponent.class) != null){
                attacker.getComponent(StateMachine.class).trigger(PlayerAnimationTrigger.ATTACK);
            } else {
                attacker.getComponent(StateMachine.class).trigger(MobAnimationTrigger.ATTACK);
            }
        }
    }

}
