package fr.mrqsdf.bossrush.animation;

import fr.mrqsdf.engine2d.components.AnimationType;

public enum PotionAnimationType implements AnimationType {

    LOOP;

    @Override
    public String getName() {
        return this.name();
    }

}
