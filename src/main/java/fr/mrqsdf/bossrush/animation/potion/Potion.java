package fr.mrqsdf.bossrush.animation.potion;

import fr.mrqsdf.bossrush.animation.PotionAnimationType;
import fr.mrqsdf.bossrush.component.DisplayComponent;
import fr.mrqsdf.bossrush.component.PotionComponent;
import fr.mrqsdf.bossrush.res.DisplayState;
import fr.mrqsdf.bossrush.res.PotionType;
import fr.mrqsdf.engine2d.components.AnimationState;
import fr.mrqsdf.engine2d.components.SpriteRenderer;
import fr.mrqsdf.engine2d.components.SpriteSheet;
import fr.mrqsdf.engine2d.components.StateMachine;
import fr.mrqsdf.engine2d.editor.AssetsWindow;
import fr.mrqsdf.engine2d.jade.GameObject;
import fr.mrqsdf.engine2d.jade.Prefabs;
import fr.mrqsdf.engine2d.jade.Window;

public class Potion {

    private static final String potionPath = "assets/spritesheets/item/potion/";
    public static GameObject RedPotion(float posX, float posY){
        SpriteSheet potionSpriteSheet = AssetsWindow.getSpriteSheet(potionPath + "RedPotion.spsheet");
        GameObject potion = Prefabs.generateSpriteObject(potionSpriteSheet.getSprite(0), 0.25f,0.25f, posX, posY, 0, 6);
        potion.name = "RedPotion";
        setStateMachine(potion, potionSpriteSheet);
        potion.addComponent(new DisplayComponent(DisplayState.ITEM));
        potion.addComponent(new PotionComponent(PotionType.HEAL));
        return potion;
    }

    public static GameObject BluePotion(float posX, float posY){
        SpriteSheet potionSpriteSheet = AssetsWindow.getSpriteSheet(potionPath + "BluePotion.spsheet");
        GameObject potion = Prefabs.generateSpriteObject(potionSpriteSheet.getSprite(0), 0.25f,0.25f, posX, posY, 0, 6);
        potion.name = "BluePotion";
        setStateMachine(potion, potionSpriteSheet);
        potion.addComponent(new PotionComponent(PotionType.MANA));
        return potion;
    }

    public static GameObject GreenPotion(float posX, float posY){
        SpriteSheet potionSpriteSheet = AssetsWindow.getSpriteSheet(potionPath + "GreenPotion.spsheet");
        GameObject potion = Prefabs.generateSpriteObject(potionSpriteSheet.getSprite(0), 0.25f,0.25f, posX, posY, 0, 6);
        potion.name = "GreenPotion";
        setStateMachine(potion, potionSpriteSheet);
        potion.addComponent(new PotionComponent(PotionType.POISON));
        return potion;
    }



    private static void setStateMachine(GameObject potion, SpriteSheet potionSpriteSheet){
        StateMachine stateMachine = new StateMachine();
        AnimationState loop = new AnimationState();
        loop.title = PotionAnimationType.LOOP.getName();
        loop.animationTypeName = PotionAnimationType.LOOP.name();
        loop.addFrame(potionSpriteSheet.getSprite(0), 0.1f);
        loop.addFrame(potionSpriteSheet.getSprite(1), 0.1f);
        loop.addFrame(potionSpriteSheet.getSprite(2), 0.1f);
        loop.addFrame(potionSpriteSheet.getSprite(3), 0.1f);
        loop.addFrame(potionSpriteSheet.getSprite(4), 0.1f);
        loop.addFrame(potionSpriteSheet.getSprite(5), 0.1f);
        loop.addFrame(potionSpriteSheet.getSprite(6), 0.1f);
        loop.addFrame(potionSpriteSheet.getSprite(7), 0.1f);
        loop.setLoop(true);
        stateMachine.addState(loop);
        potion.addComponent(stateMachine);
    }

}
