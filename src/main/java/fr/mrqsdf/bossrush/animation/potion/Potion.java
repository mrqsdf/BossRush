package fr.mrqsdf.bossrush.animation.potion;

import fr.mrqsdf.bossrush.component.DisplayComponent;
import fr.mrqsdf.bossrush.component.PotionComponent;
import fr.mrqsdf.bossrush.res.DisplayState;
import fr.mrqsdf.bossrush.res.PotionType;
import fr.mrqsdf.engine2d.components.*;
import fr.mrqsdf.engine2d.editor.AssetsWindow;
import fr.mrqsdf.engine2d.jade.GameObject;
import fr.mrqsdf.engine2d.jade.Prefabs;
import fr.mrqsdf.engine2d.jade.Window;
import fr.mrqsdf.engine2d.scenes.Scene;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class Potion {

    private static final String potionPath = "assets/spritesheets/item/potion/";
    public static GameObject RedPotion(float posX, float posY){
        Scene scene = Window.getScene();
        SpriteSheet potionSpriteSheet = AssetsWindow.getSpriteSheet(potionPath + "RedPotion.spsheet");
        System.out.println(potionSpriteSheet.getSprite(0));
        GameObject potion = scene.createGameObject("RedPotion");
        SpriteRenderer spriteRenderer = new SpriteRenderer();
        spriteRenderer.setSprite(potionSpriteSheet.getSprite(0));
        potion.transform.position = new Vector2f(posX, posY);
        potion.transform.scale = new Vector2f(0.16f,0.16f);
        potion.transform.zIndex = 6;

        setStateMachine(potion, potionSpriteSheet);
        potion.addComponent(spriteRenderer);
        potion.addComponent(new DisplayComponent(DisplayState.ITEM));
        potion.addComponent(new PotionComponent(PotionType.HEAL));
        return potion;
    }

    public static GameObject BluePotion(float posX, float posY){
        SpriteSheet potionSpriteSheet = AssetsWindow.getSpriteSheet(potionPath + "BluePotion.spsheet");
        GameObject potion = Prefabs.generateSpriteObject(potionSpriteSheet.getSprite(0), 0.16f,0.16f, posX, posY, 0, 6);
        potion.name = "BluePotion";
        setStateMachine(potion, potionSpriteSheet);
        potion.addComponent(new DisplayComponent(DisplayState.ITEM));
        potion.addComponent(new PotionComponent(PotionType.MANA));
        return potion;
    }

    public static GameObject GreenPotion(float posX, float posY){
        SpriteSheet potionSpriteSheet = AssetsWindow.getSpriteSheet(potionPath + "GreenPotion.spsheet");
        GameObject potion = Prefabs.generateSpriteObject(potionSpriteSheet.getSprite(0), 0.16f,0.16f, posX, posY, 0, 6);
        potion.name = "GreenPotion";
        setStateMachine(potion, potionSpriteSheet);
        potion.addComponent(new DisplayComponent(DisplayState.ITEM));
        potion.addComponent(new PotionComponent(PotionType.POISON));
        return potion;
    }



    private static void setStateMachine(GameObject potion, SpriteSheet potionSpriteSheet){
        StateMachine stateMachine = new StateMachine();
        AnimationState loop = new AnimationState();
        loop.title = PotionAnimationType.LOOP.getName();
        loop.animationTypeName = PotionAnimationType.LOOP.getName();
        for (int i = 0; i < potionSpriteSheet.size(); i++){
            loop.addFrame(potionSpriteSheet.getSprite(i), 0.1f);
        }
        loop.setLoop(true);
        List<AnimationState> list = new ArrayList<>();
        list.add(loop);
        stateMachine.setStates(list);
        stateMachine.setDefaultState(PotionAnimationTrigger.LOOP);
        stateMachine.addStateTrigger(new StateTrigger(PotionAnimationType.LOOP, PotionAnimationTrigger.LOOP,0), PotionAnimationType.LOOP);
        stateMachine.refreshTextures();
        potion.addComponent(stateMachine);
    }

}
