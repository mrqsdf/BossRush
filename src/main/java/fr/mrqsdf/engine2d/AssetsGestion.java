package fr.mrqsdf.engine2d;

import fr.mrqsdf.engine2d.components.SpriteSheet;
import fr.mrqsdf.engine2d.utils.AssetPool;

public class AssetsGestion {

    public static void setAssets(){
        SpriteSheet gizmos = AssetPool.getSpriteSheet("assets/images/engine/gizmos.png");

    }

    public static void addAssets(){
        //important
        AssetPool.getShader("assets/shaders/default.glsl");
        AssetPool.addSpriteSheet("assets/images/engine/gizmos.png",new SpriteSheet(
                AssetPool.getTexture("assets/images/engine/gizmos.png"),24,48,3,0,"gizmos"));
        AssetPool.addSpriteSheet("assets/images/AddPrefabs.png", new SpriteSheet(
                AssetPool.getTexture("assets/images/engine/AddPrefabs.png"), 64, 64, 1, 0, "AddPrefabs"));
        AssetPool.addSpriteSheet("assets/images/engine/back.png", new SpriteSheet(
                AssetPool.getTexture("assets/images/engine/back.png"), 64, 64, 1, 0, "back"));
        AssetPool.addSpriteSheet("assets/images/engine/folder.png", new SpriteSheet(
                AssetPool.getTexture("assets/images/engine/folder.png"), 64, 64, 1, 0, "folder"));
        AssetPool.addSpriteSheet("assets/images/engine/levels.png", new SpriteSheet(
                AssetPool.getTexture("assets/images/engine/levels.png"), 64, 64, 1, 0, "level"));
        AssetPool.addSpriteSheet("assets/images/engine/spriteSheet.png", new SpriteSheet(
                AssetPool.getTexture("assets/images/engine/spriteSheet.png"), 64, 64, 1, 0, "spriteSheet"));

        //test
        AssetPool.addSpriteSheet("assets/images/player.png", new SpriteSheet(
                AssetPool.getTexture("assets/images/player.png"), 32, 32, 4, 0, "Player"));
    }

}
