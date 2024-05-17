package fr.mrqsdf.bossrush.animation.potion;

import fr.mrqsdf.engine2d.components.TriggerType;

public enum PotionAnimationTrigger implements TriggerType {

    LOOP;

    public final String name;

    PotionAnimationTrigger(){
        name = this.name().toLowerCase();
    }

    @Override
    public String getName() {
        return name;
    }

}
