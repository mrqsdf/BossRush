package fr.mrqsdf.bossrush.animation.mobs;

import fr.mrqsdf.engine2d.components.TriggerType;

public enum MobAnimationTrigger implements TriggerType {

    IDLE,
    ATTACK,
    TAKING_DAMAGE,
    DEATH;

    MobAnimationTrigger(){
        this.name = this.name().toLowerCase();
    }
    private final String name;

    @Override
    public String getName() {
        return name;
    }

}
