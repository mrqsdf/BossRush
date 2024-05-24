package fr.mrqsdf.bossrush.util;

import fr.mrqsdf.bossrush.component.DisplayComponent;
import fr.mrqsdf.bossrush.component.GameCamera;
import fr.mrqsdf.bossrush.component.ItemComponent;
import fr.mrqsdf.bossrush.res.DisplayState;
import fr.mrqsdf.engine2d.components.HudComponent;
import fr.mrqsdf.engine2d.editor.AssetsWindow;
import fr.mrqsdf.engine2d.font.TextComponent;
import fr.mrqsdf.engine2d.jade.GameObject;
import fr.mrqsdf.engine2d.jade.Prefabs;
import fr.mrqsdf.engine2d.jade.Window;
import org.joml.Vector4f;

public class DisplayItemInfo {

    public static GameObject itemInfo(GameObject item, GameCamera gameCamera){
        float pos1 = gameCamera.descriptionItemX;
        float pos2 = gameCamera.descriptionItemY;
        ItemComponent itemComponent = item.getComponent(ItemComponent.class);
        if (itemComponent == null) return null;
        GameObject itemInfo = Prefabs.generateSpriteObject(AssetsWindow.getSpriteSheet("assets/spritesheets/hud/display64.16.spsheet").getSprite(0),1,0.5f, pos1, pos2,0,10);
        itemInfo.setNoSerialize();
        HudComponent itemInfoComponent = new HudComponent();
        itemInfo.addComponent(itemInfoComponent);
        GameObject itemName = Window.getScene().createGameObject("ItemName");
        itemName.setNoSerialize();
        itemName.transform.position.x = pos1 - 0.1f;
        itemName.transform.position.y = pos2 - 0.1f;
        itemName.transform.zIndex = 11;
        TextComponent textComponent = new TextComponent(itemComponent.name, new Vector4f(0), 0.2f);
        textComponent.setHudComponent(itemInfoComponent);
        itemName.addComponent(textComponent);
        itemInfoComponent.addObject(itemName);
        GameObject itemDescription = Window.getScene().createGameObject("ItemDescription");
        itemDescription.setNoSerialize();
        itemDescription.transform.position.x = pos1 + 0.1f;
        itemDescription.transform.position.y = pos2 - 0.15f;
        itemDescription.transform.zIndex = 11;
        TextComponent textComponent1 = new TextComponent(itemComponent.effect, new Vector4f(0), 0.2f);
        textComponent1.setHudComponent(itemInfoComponent);
        itemDescription.addComponent(textComponent1);
        itemInfoComponent.addObject(itemDescription);
        if (itemComponent.usable){
            usableItem(pos1 + 0.1f, pos2 - 0.3f, itemInfoComponent);
        }

        Window.getScene().addGameObjectToScene(itemInfo);
        Window.getScene().addGameObjectToScene(itemName);
        Window.getScene().addGameObjectToScene(itemDescription);

        return itemInfo;
    }

    private static GameObject usableItem(float pos1, float pos2, HudComponent hudComponent){
        GameObject usableItem = Prefabs.generateSpriteObject(AssetsWindow.getSpriteSheet("assets/spritesheets/hud/display64.16.spsheet").getSprite(0),0.25f,0.175f, pos1, pos2,0,11);
        usableItem.setNoSerialize();
        usableItem.addComponent(new DisplayComponent(DisplayState.USABLE));
        hudComponent.addObject(usableItem);
        GameObject itemName = Window.getScene().createGameObject("ItemName");
        itemName.setNoSerialize();
        itemName.transform.position.x = pos1 + 0.1f;
        itemName.transform.position.y = pos2 - 0.1f;
        itemName.transform.zIndex = 11;
        TextComponent textComponent = new TextComponent("Use", new Vector4f(0), 0.2f);
        textComponent.setHudComponent(hudComponent);
        itemName.addComponent(textComponent);
        hudComponent.addObject(itemName);
        Window.getScene().addGameObjectToScene(usableItem);
        Window.getScene().addGameObjectToScene(itemName);
        return usableItem;
    }

}
