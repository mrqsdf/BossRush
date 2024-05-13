package fr.mrqsdf.engine2d.components;

import fr.mrqsdf.engine2d.utils.AssetPool;

import java.util.ArrayList;
import java.util.List;

public class AnimationState {
    public String title;
    public String animationTypeName;
    public List<Frame> animationFrames = new ArrayList<>();

    private static Sprite defaultSprite = new Sprite();
    private transient float timeTracker = 0;
    public transient int currentSprite = 0;
    public boolean doesLoop = false;
    public boolean animationEnd = false;
    public boolean returnToDefault = true;

    public void refreshTextures(){
        for (Frame frame : animationFrames){
            frame.sprite.setTexture(AssetPool.getTexture(frame.sprite.getTexture().getFilepath()));
        }
    }

    public AnimationState(){}

    public void addFrame(Sprite sprite, float frameTime){
        animationFrames.add(new Frame(sprite, frameTime));
    }

    public void setLoop(boolean doesLoop){
        this.doesLoop = doesLoop;
    }

    public void update(float dt){
        if (currentSprite < animationFrames.size()){
            timeTracker -= dt;
            if (timeTracker <= 0){
                if (currentSprite < animationFrames.size() - 1 || doesLoop){
                    currentSprite = (currentSprite + 1) % animationFrames.size();
                } else if (currentSprite == animationFrames.size() - 1 && returnToDefault){
                    animationEnd = true;
                }
                timeTracker = animationFrames.get(currentSprite).frameTime;
            }
        }
    }

    public Sprite getCurrentSprite(){
        if (currentSprite < animationFrames.size()){
            return animationFrames.get(currentSprite).sprite;
        }
        return defaultSprite;
    }


}
