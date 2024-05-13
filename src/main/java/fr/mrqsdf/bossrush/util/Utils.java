package fr.mrqsdf.bossrush.util;

import fr.mrqsdf.bossrush.component.EntityComponent;
import fr.mrqsdf.engine2d.jade.GameObject;

public class Utils {

    public static void attack(GameObject attacker, GameObject target){
        EntityComponent attackerComponent = attacker.getComponent(EntityComponent.class);
        EntityComponent targetComponent = target.getComponent(EntityComponent.class);
        targetComponent.receiveDamage(attackerComponent.getDamage());
    }

}
