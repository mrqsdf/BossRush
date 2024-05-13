package fr.mrqsdf.engine2d.components;

import fr.mrqsdf.engine2d.editor.JImGui;
import fr.mrqsdf.engine2d.renderer.Texture;
import imgui.ImGui;
import org.joml.Vector2f;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class SpriteSheet {

    private Texture texture;
    private List<Sprite> sprites;
    private int spriteWidth, spriteHeight, numSprites, spacing;
    private String name;


    public SpriteSheet(Texture texture, int spriteWidth, int spriteHeight, int numSprites, int spacing, String name){
        this.sprites = new ArrayList<>();
        this.texture = texture;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.numSprites = numSprites;
        this.spacing = spacing;
        this.name = name;
        setSpriteSheet();
    }

    private void setSpriteSheet(){
        int currentX = 0;
        int currentY = texture.getHeight() - spriteHeight;
        for (int i =0; i < numSprites; i++){

            float topY = (currentY + spriteHeight) / (float) texture.getHeight();
            float rightX = (currentX + spriteWidth) / (float) texture.getWidth();
            float leftX = currentX / (float) texture.getWidth();
            float bottomY = currentY / (float) texture.getHeight();

            Vector2f[] texCoords = {
                    new Vector2f(rightX, topY),
                    new Vector2f(rightX, bottomY),
                    new Vector2f(leftX, bottomY),
                    new Vector2f(leftX, topY)
            };

            Sprite sprite = new Sprite();
            sprite.setTexture(this.texture);
            sprite.setTexCoords(texCoords);
            sprite.setWidth(spriteWidth);
            sprite.setHeight(spriteHeight);
            sprites.add(sprite);

            currentX += spriteWidth + spacing;
            if (currentX >= texture.getWidth()){
                currentX = 0;
                currentY -= spriteHeight + spacing;
            }
        }
    }

    public void imgui(){
        try{
            Field[] fields = this.getClass().getDeclaredFields();

            for (Field field : fields){

                boolean isTransient = Modifier.isTransient(field.getModifiers());
                if (isTransient){
                    continue;
                }
                boolean isPrivate = Modifier.isPrivate(field.getModifiers());
                if (isPrivate){
                    field.setAccessible(true);
                }

                Class type = field.getType();
                Object value = field.get(this);
                String name = field.getName();

                if (type == int.class){
                    int val = (int) value;
                    field.set(this, JImGui.dragInt(name, val));
                } else if (type == float.class) {
                    float val = (float) value;
                    field.set(this, JImGui.dragFloat(name, val));
                } else if (type == boolean.class) {
                    boolean val = (boolean) value;
                    if (ImGui.checkbox(name + ": ", val)){
                        val = !val;
                        field.set(this, val);
                    }
                }

                if (isPrivate){
                    field.setAccessible(false);
                }
            }
        }catch (IllegalAccessException e){
            e.printStackTrace();
        }
    }

    public Sprite getSprite(int index){
        return sprites.get(index);
    }
    public List<Sprite> getSprites() { return this.sprites; }
    public int size(){
        return sprites.size();
    }
    public String getName(){
        return name;
    }

}
