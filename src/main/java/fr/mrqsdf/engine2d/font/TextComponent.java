package fr.mrqsdf.engine2d.font;

import fr.mrqsdf.engine2d.components.Component;
import fr.mrqsdf.engine2d.components.Sprite;
import fr.mrqsdf.engine2d.components.SpriteRenderer;
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
    private boolean isVisible = false;

    public TextComponent(String text, Vector4f color, float size){
        this.text = text;
        this.color = color;
        this.size = size;
    }
    @Override
    public void start(){
        float x =0;
        float y =0;
        for (Character c : text.toCharArray()){
            Sprite sprite = TextData.getCharacter(c);
            float posX = gameObject.transform.position.x + x;
            float posY = gameObject.transform.position.y + y;
            GameObject character = Prefabs.generateSpriteObject(sprite, size, size, 0, 0, 0, 6);
            character.transform.position = new Vector2f(posX, posY);
            System.out.println("posX: " + posX + " posY: " + posY);
            character.getComponent(SpriteRenderer.class).setColor(color);
            characters.add(character);
            character.setNoSerialize();
            character.name = "TextCharacter";
            Window.getScene().addGameObjectToScene(character);
            x += 1 * size;
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
