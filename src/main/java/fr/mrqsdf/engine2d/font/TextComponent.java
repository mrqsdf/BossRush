package fr.mrqsdf.engine2d.font;

import fr.mrqsdf.engine2d.components.*;
import fr.mrqsdf.engine2d.jade.GameObject;
import fr.mrqsdf.engine2d.jade.Prefabs;
import fr.mrqsdf.engine2d.jade.Window;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public class TextComponent extends Component {

    public String text;
    public Vector4f color;
    public float size;
    private List<GameObject> characters = new ArrayList<>();
    private boolean isVisible = true;
    private List<Component> characterComponents = new ArrayList<>();
    private HudComponent hudComponent;

    private float defaultX = 0;
    private float defaultY = 0;

    public TextComponent(String text, Vector4f color, float size){
        this.text = text;
        this.color = color;
        this.size = size;
    }
    public TextComponent(String text, Vector4f color, float size, float defaultX, float defaultY){
        this.text = text;
        this.color = color;
        this.size = size;
        this.defaultX = defaultX;
        this.defaultY = defaultY;
    }
    public TextComponent(String text, Vector4f color, float size, GameObject gameObject){
        this.text = text;
        this.color = color;
        this.size = size;
        this.defaultX = gameObject.transform.position.x;
        this.defaultY = gameObject.transform.position.y;
    }
    public TextComponent(String text, Vector4f color, float size, GameObject gameObject, HudComponent hudComponent){
        this.text = text;
        this.color = color;
        this.size = size;
        this.defaultX = gameObject.transform.position.x;
        this.defaultY = gameObject.transform.position.y;
        this.hudComponent = hudComponent;
    }


    public void addCharacterComponent(Component component){
        characterComponents.add(component);
    }

    public void setHudComponent(HudComponent hudComponent){
        this.hudComponent = hudComponent;
    }

    public void removeCharacter(){
        for (GameObject character : characters){
            hudComponent.removeObject(character);
            character.destroy();
        }
    }


    public void setDefaultX(float defaultX){
        this.defaultX = defaultX;
    }

    public void setDefaultY(float defaultY){
        this.defaultY = defaultY;
    }

    public void setDefaultPosition(float defaultX, float defaultY){
        this.defaultX = defaultX;
        this.defaultY = defaultY;
    }

    @Override
    public void start(){
        float x =0;
        float y =0;
        for (Character c : text.toCharArray()){
            Sprite sprite = TextData.getCharacter(c);
            float posX = defaultX + x;
            float posY = defaultY + y;
            GameObject character = Prefabs.generateSpriteObject(sprite, size, size, 0, 0, 0, gameObject.transform.zIndex+1);
            character.transform.position = new Vector2f(posX, posY);
            character.getComponent(SpriteRenderer.class).setColor(color);
            characters.add(character);
            character.setNoSerialize();
            character.name = "TextCharacter";
            if (hudComponent != null){
                character.addComponent(new HudObjectComponent());
                hudComponent.addObject(character);
            }
            for (Component component : characterComponents){
                if (component instanceof HudComponent || component instanceof HudObjectComponent) continue;
                character.addComponent(component);
            }
            Window.getScene().addGameObjectToScene(character);
            if (c == ' ') x += TextData.spaceEscape * size;
            else x += TextData.spaceWidth * size;
        }
    }

    @Override
    public void update(float dt){
        if (gameObject.transform.isVisible && !isVisible){
            for (GameObject character : characters){
                character.transform.isVisible = true;
            }
            isVisible = true;
        } else if (!gameObject.transform.isVisible && isVisible){
            for (GameObject character : characters){
                character.transform.isVisible = false;
            }
            isVisible = false;
        }
    }


}
