package fr.mrqsdf.bossrush.component;

import fr.mrqsdf.bossrush.res.PotionType;
import fr.mrqsdf.engine2d.components.Component;

public class PotionComponent extends ItemComponent {

    PotionType type;

    public PotionComponent(PotionType type) {
        this.type = type;
    }

}
