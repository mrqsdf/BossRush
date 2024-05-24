package fr.mrqsdf.bossrush.res;

import fr.mrqsdf.bossrush.component.DisplayComponent;
import fr.mrqsdf.bossrush.component.GameCamera;
import fr.mrqsdf.bossrush.component.InventoryComponent;
import fr.mrqsdf.bossrush.component.ItemComponent;
import fr.mrqsdf.engine2d.components.HudComponent;
import fr.mrqsdf.engine2d.components.SpriteSheet;
import fr.mrqsdf.engine2d.editor.AssetsWindow;
import fr.mrqsdf.engine2d.font.TextComponent;
import fr.mrqsdf.engine2d.jade.GameObject;
import fr.mrqsdf.engine2d.jade.Prefabs;
import fr.mrqsdf.engine2d.jade.Window;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public class DisplayItem {

    private GameObject background;
    private GameObject usedButton;
    public static GameObject hud;

    private List<GameObject> textGameObject = new ArrayList<>();

    public GameObject item;

    private float newX = 0.375f;
    private float newX2 = 0.4f;


    public DisplayItem(){
        SpriteSheet backgroundSprite = AssetsWindow.getSpriteSheet("assets/spritesheets/hud/display64.16.spsheet");
        SpriteSheet usedSprite = AssetsWindow.getSpriteSheet("assets/spritesheets/hud/display2.spsheet");

        hud = Window.getScene().createGameObject("PlayerHealHud");
        hud.setNoSerialize();
        HudComponent hudComponent = new HudComponent();
        hud.addComponent(hudComponent);
        Window.getScene().addGameObjectToScene(hud);

        background = Prefabs.generateSpriteObject(backgroundSprite.getSprite(0), 1,0.5f,3.75f,2f,0,10);
        hudComponent.addObject(background);
        background.setNoSerialize();
        background.transform.isVisible = false;
        Window.getScene().addGameObjectToScene(background);

        usedButton = Prefabs.generateSpriteObject(usedSprite.getSprite(0), 0.25f,0.175f,4.125f,2.25f,0,10);
        usedButton.setNoSerialize();
        hudComponent.addObject(usedButton);
        TextComponent textComponent = new TextComponent("Use", new Vector4f(1), 0.1f, usedButton, hudComponent);
        textComponent.setDefaultPosition(4.1f, 2.25f);
        textComponent.addCharacterComponent(new DisplayComponent(DisplayState.USABLE));
        usedButton.addComponent(textComponent);
        usedButton.transform.isVisible = false;
        usedButton.addComponent(new DisplayComponent(DisplayState.USABLE));
        Window.getScene().addGameObjectToScene(usedButton);

        System.out.println(hudComponent.getObjects().size());

//        gameCamera.addHud(hud);

    }


    public void show(GameObject item){
        background.transform.isVisible = true;
        usedButton.transform.isVisible = true;
        this.item = item;
        ItemComponent itemComponent = item.getComponent(ItemComponent.class);
        assert itemComponent != null;
        addText(itemComponent.name, background.transform.position.x - newX, 2.1f, 0.1f);
        addText(itemComponent.effect, background.transform.position.x - newX2, 1.875f, itemComponent.effect.toCharArray().length > 20 ? 0.057f : 0.07f);
        System.out.println(background.transform.position.x + " " + background.transform.position.y);
    }

    public void hide(){
        background.transform.isVisible = false;
        usedButton.transform.isVisible = false;
        this.item = null;
        this.textGameObject.forEach(gameObject -> {
            TextComponent textComponent = gameObject.getComponent(TextComponent.class);
            if (textComponent != null){
                textComponent.removeCharacter();
            }
            gameObject.destroy();
        });
        this.textGameObject.clear();
    }

    private void addText(String text, float x, float y, float size){
        HudComponent hudComponent = hud.getComponent(HudComponent.class);
        GameObject textGameObject = Window.getScene().createGameObject("Text");
        textGameObject.setNoSerialize();
        textGameObject.transform.position.x = x;
        textGameObject.transform.position.y = y;
        textGameObject.transform.zIndex = 11;
        hudComponent.addObject(textGameObject);
        TextComponent textComponent = new TextComponent(text, new Vector4f(1), size,textGameObject, hudComponent);
        textGameObject.addComponent(textComponent);
        this.textGameObject.add(textGameObject);
        textGameObject.transform.isVisible = true;
        Window.getScene().addGameObjectToScene(textGameObject);
    }

    public void use(GameCamera gameCamera, GameObject inventory){
        ItemComponent itemComponent = item.getComponent(ItemComponent.class);
        InventoryComponent inventoryComponent = inventory.getComponent(InventoryComponent.class);
        assert itemComponent != null;
        itemComponent.use(gameCamera);
        inventoryComponent.removeItem(item);
        item.destroy();
        hide();
    }

}
