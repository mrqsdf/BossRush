package fr.mrqsdf.bossrush.animation.player;

import fr.mrqsdf.engine2d.components.TriggerType;

public enum PlayerAnimationTrigger implements TriggerType {

    IDLE,
    WALK,
    RUN,
    ATTACK,
    COMBO_ATTACK,
    DEFEND,
    TAKING_DAMAGE,
    SPELL_CAST,
    DEATH;

    private final String name;

    PlayerAnimationTrigger() {
        this.name = this.name().toLowerCase();
    }


    @Override
    public String getName() {
        return name;
    }
}
