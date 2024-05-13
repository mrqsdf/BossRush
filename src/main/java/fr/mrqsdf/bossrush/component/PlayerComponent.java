package fr.mrqsdf.bossrush.component;

import fr.mrqsdf.bossrush.res.DisplayState;
import fr.mrqsdf.bossrush.res.EntityType;
import fr.mrqsdf.engine2d.components.Component;
import fr.mrqsdf.engine2d.components.Sprite;
import fr.mrqsdf.engine2d.components.SpriteRenderer;
import fr.mrqsdf.engine2d.jade.GameObject;
import fr.mrqsdf.engine2d.jade.Window;
import org.joml.Vector2f;

public class PlayerComponent extends EntityComponent {

    public transient int experience = 0;
    public transient int experienceMax = 20;
    public transient GameObject xpBar;
    public transient GameObject xpBarEmpty;


    public PlayerComponent() {
        super(EntityType.PLAYER);
    }

    @Override
    public void start(){
        for (GameObject go : Window.getScene().getGameObjectsWithComponent(DisplayComponent.class)){
            if (go.getComponent(DisplayComponent.class).displayState == DisplayState.PLAYER_HEAL_HUD_FULL){
                healBar = go;
            }
            if (go.getComponent(DisplayComponent.class).displayState == DisplayState.PLAYER_HEAL_HUD_EMPTY){
                healBarEmpty = go;
            }
            if (go.getComponent(DisplayComponent.class).displayState == DisplayState.PLAYER_MANA_HUD_FULL){
                manaBar = go;
            }
            if (go.getComponent(DisplayComponent.class).displayState == DisplayState.PLAYER_MANA_HUD_EMPTY){
                manaBarEmpty = go;
            }
            if (go.getComponent(DisplayComponent.class).displayState == DisplayState.PLAYER_EXP_HUD_FULL){
                xpBar = go;
            }
            if (go.getComponent(DisplayComponent.class).displayState == DisplayState.PLAYER_EXP_HUD_EMPTY){
                xpBarEmpty = go;
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
        if (xpBar != null && xpBarEmpty != null){
            setXpBar();
        }
    }

    public float getXpPercent(){
        return (float) experience / experienceMax;
    }
    public void setXpBar(){
        Sprite sprite = xpBar.getComponent(SpriteRenderer.class).getSprite();
        Vector2f[] texCoords = {
                new Vector2f(getHealPercent(), 1),
                new Vector2f(getHealPercent(), 0),
                new Vector2f(0, 0),
                new Vector2f(0, 1)
        };
        xpBar.transform.scale.x = xpBarEmpty.transform.scale.x *getHealPercent();
        if (xpBarEmpty.transform.scale.x < 0) {
            xpBar.transform.position.x = xpBarEmpty.transform.position.x + (1 - getHealPercent()) / 2;
        } else if (xpBarEmpty.transform.scale.x >= 0) {
            xpBar.transform.position.x = xpBarEmpty.transform.position.x - (1 - getHealPercent()) / 2;
        }
        sprite.setTexCoords(texCoords);
    }


}
