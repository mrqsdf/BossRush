package fr.mrqsdf.bossrush.animation.mobs.slime;

import fr.mrqsdf.engine2d.components.AnimationType;
import fr.mrqsdf.engine2d.components.TriggerType;

public enum SlimeAnimationTrigger implements TriggerType {

    IDLE("IDLE"),
    ATTACK("ATTACK"),
    TAKING_DAMAGE("TAKING_DAMAGE"),
    DEATH("DEATH");

    SlimeAnimationTrigger(String name){
        this.name = name;
    }
    private final String name;

    @Override
    public String getName() {
        return name;
    }

}
