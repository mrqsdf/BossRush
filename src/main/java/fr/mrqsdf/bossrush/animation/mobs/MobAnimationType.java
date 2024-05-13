package fr.mrqsdf.bossrush.animation.mobs;

import fr.mrqsdf.engine2d.components.AnimationType;

public enum MobAnimationType implements AnimationType {

    IDLE("idle"),
    ATTACK("attack"),
    TAKING_DAMAGE("takingDamage"),
    DEATH("death");

    MobAnimationType(String name){
        this.name = name;
    }
    private final String name;

    @Override
    public String getName() {
        return name;
    }

}
