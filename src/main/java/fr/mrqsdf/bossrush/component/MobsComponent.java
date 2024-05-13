package fr.mrqsdf.bossrush.component;

import fr.mrqsdf.bossrush.res.DisplayState;
import fr.mrqsdf.bossrush.res.EntityType;
import fr.mrqsdf.engine2d.jade.GameObject;
import fr.mrqsdf.engine2d.jade.Window;

public class MobsComponent extends EntityComponent {


    public MobsComponent(EntityType entityType, int level){
        super(entityType, level);
    }

    @Override
    public void start(){
        for (GameObject go : Window.getScene().getGameObjectsWithComponent(DisplayComponent.class)){
            if (go.getComponent(DisplayComponent.class).displayState == DisplayState.MOB_HEAL_HUD_FULL){
                healBar = go;
            }
            if (go.getComponent(DisplayComponent.class).displayState == DisplayState.MOB_HEAL_HUD_EMPTY){
                healBarEmpty = go;
            }
            if (go.getComponent(DisplayComponent.class).displayState == DisplayState.MOB_MANA_HUD_FULL){
                manaBar = go;
            }
            if (go.getComponent(DisplayComponent.class).displayState == DisplayState.MOB_MANA_HUD_EMPTY){
                manaBarEmpty = go;
            }
        }
    }

    @Override
    public void update(float dt){
        if (healBar != null && healBarEmpty != null){
            setHealBar();
        }
        if (manaBar != null && manaBarEmpty != null){
            setManaBar();
        }
    }

}

