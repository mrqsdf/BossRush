package fr.mrqsdf.engine2d.components;

import fr.mrqsdf.engine2d.editor.JImGui;
import fr.mrqsdf.engine2d.jade.Transform;
import fr.mrqsdf.engine2d.renderer.Texture;
import fr.mrqsdf.engine2d.utils.AssetPool;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class SpriteRenderer extends Component {
    private Vector4f color = new Vector4f(1,1,1,1);
    private Sprite sprite = new Sprite();

    private transient Transform lastTransform;
    public transient boolean isDirty = true;

    @Override
    public void start(){
        if (this.sprite.getTexture() != null) {
            this.sprite.setTexture(AssetPool.getTexture(this.sprite.getTexture().getFilepath()));
        }
        this.lastTransform = gameObject.transform.copy();
    }
    @Override
    public void update(float dt){
        if (!this.lastTransform.equals(gameObject.transform)){
            gameObject.transform.copy(this.lastTransform);
            isDirty = true;
        }
    }
    @Override
    public void editorUpdate(float dt){
        if (!this.lastTransform.equals(gameObject.transform)){
            gameObject.transform.copy(this.lastTransform);
            isDirty = true;
        }
    }
    @Override
    public void imgui(){
        if (JImGui.colorPicker4("Color Picker", color)){
            this.isDirty = true;
        }
    }


    public Vector4f getColor(){
        return this.color;
    }

    public Texture getTexture(){
        return sprite.getTexture();
    }

    public Vector2f[] getTexCoords() {
        return sprite.getTexCoords();
    }
    public void setSprite(Sprite sprite){
        this.sprite = sprite;
        isDirty = true;
    }

    public void setColor(Vector4f color){
        if (!this.color.equals(color)){
            this.isDirty = true;
            this.color.set(color);
        }

    }

    public boolean isDirty() {
        return isDirty;
    }
    public void setDirty(){
        isDirty = true;
    }
    public void setClean(){
        isDirty = false;
    }


    public void setTexture(Texture texture){
        this.sprite.setTexture(texture);
        //isDirty = true;
    }

    public Sprite getSprite(){
        return this.sprite;
    }

}
