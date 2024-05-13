package fr.mrqsdf.engine2d.components;

import fr.mrqsdf.engine2d.renderer.Texture;
import org.joml.Vector2f;

public class Sprite implements Cloneable {

    private float width, height;
    private Texture texture = null;
    private Vector2f[] texCoords = {
                new Vector2f(1, 1),
                new Vector2f(1, 0),
                new Vector2f(0, 0),
                new Vector2f(0, 1)
        };


    public Texture getTexture(){
        return this.texture;
    }

    public Vector2f[] getTexCoords(){
        return this.texCoords;
    }
    public void setTexture(Texture tex){
        this.texture = tex;
    }
    public void setTexCoords(Vector2f[] texCoords){
        this.texCoords = texCoords;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public int getTexId(){
        return texture == null ? -1 : texture.getId();
    }

    @Override
    public Sprite clone() {
        try {
            Sprite clone = (Sprite) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
