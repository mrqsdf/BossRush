package fr.mrqsdf.bossrush.animation.mobs.slime;

import fr.mrqsdf.engine2d.components.AnimationType;

public enum SlimeAnimationType implements AnimationType {

    IDLE("idle"),
    ATTACK("attack"),
    TAKING_DAMAGE("takingDamage"),
    DEATH("death");

    SlimeAnimationType(String name){
        this.name = name;
    }
    private final String name;

    @Override
    public String getName() {
        return name;
    }

}
