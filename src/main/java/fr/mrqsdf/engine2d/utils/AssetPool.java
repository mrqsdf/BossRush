package fr.mrqsdf.engine2d.utils;

import fr.mrqsdf.engine2d.components.SpriteSheet;
import fr.mrqsdf.engine2d.jade.Sound;
import fr.mrqsdf.engine2d.renderer.Shader;
import fr.mrqsdf.engine2d.renderer.Texture;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AssetPool {
    private static Map<String, Shader> shaders = new HashMap<>();
    private static Map<String, Texture> textures = new HashMap<>();
    private static Map<String, SpriteSheet> spriteSheets = new HashMap<>();
    private static Map<String, Sound> sounds = new HashMap<>();
    public static Shader getShader(String resourceName){
        File file = new File(resourceName);
        if (AssetPool.shaders.containsKey(file.getAbsolutePath())){
            return AssetPool.shaders.get(file.getAbsolutePath());
        } else {
            Shader shader = new Shader(resourceName);
            shader.compile();
            AssetPool.shaders.put(file.getAbsolutePath(), shader);
            return shader;
        }
    }

    public static Texture getTexture(String resourceName){
        File file = new File(resourceName);
        if (AssetPool.textures.containsKey(file.getAbsolutePath())){
            return AssetPool.textures.get(file.getAbsolutePath());
        } else {
            Texture texture = new Texture();
            texture.init(resourceName);
            AssetPool.textures.put(file.getAbsolutePath(), texture);
            return texture;
        }
    }

    public static void addSpriteSheet(String resourceName, SpriteSheet spriteSheet){
        File file = new File(resourceName);
        if (!AssetPool.spriteSheets.containsKey(file.getAbsolutePath())){
            AssetPool.spriteSheets.put(file.getAbsolutePath(), spriteSheet);
        }
    }
    public static void addOrReplaceSpriteSheet(String resourceName, SpriteSheet spriteSheet){
        File file = new File(resourceName);
        if (!AssetPool.spriteSheets.containsKey(file.getAbsolutePath())){
            AssetPool.spriteSheets.put(file.getAbsolutePath(), spriteSheet);
        }else {
            AssetPool.spriteSheets.replace(file.getAbsolutePath(), spriteSheet);
        }
    }

    public static SpriteSheet getSpriteSheet(String resourceName){
        File file = new File(resourceName);
        if (!AssetPool.spriteSheets.containsKey(file.getAbsolutePath())){
            assert false : "Sprite sheet " + resourceName + " does not exist";
        }
        return AssetPool.spriteSheets.getOrDefault(file.getAbsolutePath(), null);
    }
    public static void addSpriteSheet(String resourceName, int spriteWidth, int spriteHeight, int numSprites, int spacing, String name){
        File file = new File(resourceName);
        if (!AssetPool.spriteSheets.containsKey(file.getAbsolutePath())){
            SpriteSheet spriteSheet = new SpriteSheet(getTexture(resourceName), spriteWidth, spriteHeight, numSprites, spacing,name);
            AssetPool.spriteSheets.put(file.getAbsolutePath(), spriteSheet);
        }
    }

    public static Collection<Sound> getAllSounds(){
        return sounds.values();
    }

    public static Sound getSound(String resourceName){
        File file = new File(resourceName);
        if (sounds.containsKey(file.getAbsolutePath())){
            return sounds.get(file.getAbsolutePath());
        } else {
            assert false : "Sound " + resourceName + " does not exist";
        }

        return null;
    }

    public static Sound addSound(String resourceName, boolean loops){
        File file = new File(resourceName);
        if (!sounds.containsKey(file.getAbsolutePath())){
            Sound sound = new Sound(resourceName, loops);
            sounds.put(file.getAbsolutePath(), sound);
            return sound;
        }
        return sounds.get(file.getAbsolutePath());
    }

}
