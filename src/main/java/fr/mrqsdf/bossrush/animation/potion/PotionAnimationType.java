package fr.mrqsdf.bossrush.animation.potion;

import fr.mrqsdf.engine2d.components.AnimationType;

public enum PotionAnimationType implements AnimationType {

    LOOP;

    public final String name;

    PotionAnimationType(){
        name = this.name().toLowerCase();
    }

    @Override
    public String getName() {
        return name;
    }

}
