package fr.mrqsdf.bossrush.component;

import fr.mrqsdf.bossrush.res.ItemType;
import fr.mrqsdf.bossrush.res.PotionType;
import fr.mrqsdf.engine2d.components.Component;
import fr.mrqsdf.engine2d.jade.GameObject;
import fr.mrqsdf.engine2d.jade.Window;

public class PotionComponent extends ItemComponent {

    PotionType potionType;

    public PotionComponent(PotionType type) {
        super(ItemType.POTION);
        this.potionType = type;
        this.usable = true;
        this.name = type.name;
        this.effect = type.effect;
    }

    @Override
    public void use(GameCamera gameCamera){
        switch (potionType){
            case HEAL:
                gameCamera.cameraGameObject.getComponent(PlayerComponent.class).receiveHeal(5);
                break;
            case MANA:
                // increase player speed
                break;
            case POISON:
                gameCamera.getActualEnemy().getComponent(MobsComponent.class).setPoisonDamage(2);
                break;
        }
    }

}
