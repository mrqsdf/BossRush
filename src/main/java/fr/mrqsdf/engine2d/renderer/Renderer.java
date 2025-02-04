package fr.mrqsdf.engine2d.renderer;

import fr.mrqsdf.engine2d.components.SpriteRenderer;
import fr.mrqsdf.engine2d.jade.GameObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Renderer {

    private final int MAX_BATCH_SIZE = 1000;
    private List<RenderBatch> batches;
    private static Shader currentShader;

    public Renderer(){
        this.batches = new ArrayList<>();
    }

    public void add(GameObject go){
        SpriteRenderer spr = go.getComponent(SpriteRenderer.class);
        if (spr != null && !isPresent(spr)){
            add(spr);
        }
    }

    public void remove(GameObject go){
        SpriteRenderer spr = go.getComponent(SpriteRenderer.class);
        if (spr != null && isPresent(spr)){
            remove(spr);
        }
    }

    public boolean isPresent(SpriteRenderer spr){
        for (RenderBatch batch : batches){
            if (batch.spritePresent(spr)){
                return true;
            }
        }
        return false;
    }

    public void destroyGameObject(GameObject go){
        if (go.getComponent(SpriteRenderer.class) == null) return;
        for (RenderBatch batch : batches){
            if (batch.destroyIfExist(go)){
                return;
            }
        }
    }

    private void add(SpriteRenderer sprite){
        boolean added = false;
        for (RenderBatch batch : batches){
            if (batch.hasRoom() && batch.zIndex() == sprite.gameObject.transform.zIndex) {
                Texture tex = sprite.getTexture();
                if (tex == null || !batch.hasTexture(tex) || batch.hasTextureRoom()){
                    batch.addSprite(sprite);
                    added = true;
                    break;
                }
            }
        }
        if (!added){
            RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE,
                    sprite.gameObject.transform.zIndex, this);
            newBatch.start();
            batches.add(newBatch);
            newBatch.addSprite(sprite);
            Collections.sort(batches);
        }
    }

    private void remove(SpriteRenderer sprite){
        for (RenderBatch batch : batches){
            if (batch.zIndex() == sprite.gameObject.transform.zIndex){
                batch.removeSprite(sprite);
            }
        }
    }

    public static void bindShader(Shader shader){
        currentShader = shader;
    }

    public static Shader getBoundShader(){
        return currentShader;
    }
    public void render(){
        currentShader.use();
        for (int i=0; i < batches.size(); i++){
            RenderBatch batch = batches.get(i);
            batch.render();
        }
    }

}
