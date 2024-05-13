package fr.mrqsdf.bossrush.animation.player;

import fr.mrqsdf.engine2d.components.AnimationType;

public enum PlayerAnimationType implements AnimationType {

    IDLE("idle"),
    WALK("walk"),
    RUN("run"),
    ATTACK("attack"),
    COMBO_ATTACK("comboAttack"),
    DEFEND("defend"),
    TAKING_DAMAGE("takingDamage"),
    SPELL_CAST("spellCast"),
    DEATH("death");

    PlayerAnimationType(String name){
        this.name = name;
    }
    public String name;

    @Override
    public String getName() {
        return name;
    }


}
