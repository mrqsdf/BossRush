package fr.mrqsdf.engine2d.scenes;

import fr.mrqsdf.engine2d.AssetsGestion;
import fr.mrqsdf.engine2d.components.*;
import fr.mrqsdf.engine2d.editor.AssetsWindow;
import fr.mrqsdf.engine2d.jade.GameObject;
import fr.mrqsdf.engine2d.jade.Prefabs;
import fr.mrqsdf.engine2d.jade.Sound;
import fr.mrqsdf.engine2d.utils.AssetPool;
import fr.mrqsdf.engine2d.utils.EngineSettings;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;

import java.io.File;
import java.util.Collection;
import java.util.Objects;

public class LevelEditorSceneInitializer extends SceneInitializer {

    public GameObject levelEditorStuff;

    public LevelEditorSceneInitializer() {

    }

    @Override
    public void init(Scene scene) {
        AssetsGestion.setAssets();
        SpriteSheet gizmos = AssetPool.getSpriteSheet("assets/images/engine/gizmos.png");
        levelEditorStuff = scene.createGameObject("LevelEditor");
        levelEditorStuff.setNoSerialize();
        levelEditorStuff.addComponent(new KeyControls());
        levelEditorStuff.addComponent(new MouseControls());
        levelEditorStuff.addComponent(new GridLines());
        levelEditorStuff.addComponent(new EditorCamera(scene.camera()));
        levelEditorStuff.addComponent(new GizmoSystem(gizmos));
        scene.addGameObjectToScene(0,levelEditorStuff);
    }

    @Override
    public void loadResources(Scene scene){
        AssetsGestion.addAssets();

        scene.loadRessource(scene);

    }

    @Override
    public void imgui(){
        ImGui.begin("Level Editor Stuff");
        levelEditorStuff.imgui();
        ImGui.end();
        ImGui.begin("Objets");
        if (ImGui.beginTabBar("WindowTabBar")){
            drawSpritePlayerButtons(AssetPool.getSpriteSheet("assets/images/player.png"), "Player", 2, 2);
            for (AssetsWindow aw : AssetsWindow.assetsWindowList ){
                if (Objects.equals(aw.spritePath, "")) continue;
                if (aw.animation) continue;
                drawSpriteButtons(AssetPool.getSpriteSheet(aw.spritePath), aw.fileName, 2,2);
            }
            if (ImGui.beginTabItem("Prefabs")){
                drawPrefabsButtons();
                drawAddPrefabsButtons(AssetPool.getSpriteSheet("assets/images/AddPrefabs.png"), 1, 1);
                ImGui.endTabItem();
            }
            ImGui.endTabBar();
        }
        ImGui.end();

        ImGui.begin("Sounds");

        if (ImGui.beginTabBar("SoundTabBar")){
            if (ImGui.beginTabItem("Sounds")){
                Collection<Sound> sounds = AssetPool.getAllSounds();
                for (Sound sound : sounds){
                    File tmp = new File(sound.getFilepath());
                    if (ImGui.button(tmp.getName())){
                        if (sound.isPlaying()){
                            sound.stop();
                        } else {
                            sound.play();
                        }
                    }

                    if (ImGui.getContentRegionAvailX() > 100){
                        ImGui.sameLine();
                    }
                }
                ImGui.endTabItem();
            }
            ImGui.endTabBar();
        }


        ImGui.end();


    }

    private void drawAddPrefabsButtons(SpriteSheet spriteSheet, int widthMultiplier, int heightMultiplier){
        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);
        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);
        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);

        float windowX2 = windowPos.x + windowSize.x;
        for (int i =0; i< spriteSheet.size(); i++) {
            Sprite sprite = spriteSheet.getSprite(i);
            float spriteWidth = sprite.getWidth() * widthMultiplier;
            float spriteHeight = sprite.getHeight() * heightMultiplier;
            int id = sprite.getTexId();
            Vector2f[] texCoords = sprite.getTexCoords();
            ImGui.pushID(i);
            if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                if (levelEditorStuff.getComponent(MouseControls.class).holdingObject != null){
                    Prefabs.addPrefab(levelEditorStuff.getComponent(MouseControls.class).holdingObject.copy());
                }
            }
            ImGui.popID();

            ImVec2 lastButtonPos = new ImVec2();
            ImGui.getItemRectMax(lastButtonPos);
            float lastButtonX2 = lastButtonPos.x;
            float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
            if (i + 1 < spriteSheet.size() && nextButtonX2 < windowX2) {
                ImGui.sameLine();
            }
        }
    }
    private void drawPrefabsButtons(){
        for (int i = 0; i < Prefabs.prefabs.size(); i++){
            ImVec2 windowPos = new ImVec2();
            ImGui.getWindowPos(windowPos);
            ImVec2 windowSize = new ImVec2();
            ImGui.getWindowSize(windowSize);
            ImVec2 itemSpacing = new ImVec2();
            ImGui.getStyle().getItemSpacing(itemSpacing);
            float windowX2 = windowPos.x + windowSize.x;
            Prefabs.Prefab prefab = Prefabs.prefabs.get(i);
            Sprite sprite = prefab.gameObject.getComponent(SpriteRenderer.class).getSprite();
            SpriteSheet spriteSheet = AssetPool.getSpriteSheet(sprite.getTexture().getFilepath());
            float spriteWidth = sprite.getWidth() * 64/sprite.getWidth();
            float spriteHeight = sprite.getHeight() * 64/sprite.getHeight();
            int id = sprite.getTexId();
            Vector2f[] texCoords = sprite.getTexCoords();
            ImGui.pushID(i);
            if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                levelEditorStuff.getComponent(MouseControls.class).pickUpObject(Prefabs.getPrefab(prefab.id));
            }

            ImGui.popID();
            ImVec2 lastButtonPos = new ImVec2();
            ImGui.getItemRectMax(lastButtonPos);
            float lastButtonX2 = lastButtonPos.x;
            float nextButtonX2 = lastButtonX2 + itemSpacing.x + 64/sprite.getWidth();
            if (i+1 < Prefabs.prefabs.size() && nextButtonX2 < windowX2){
                ImGui.sameLine();
            }
        }


    }


    private void drawSpriteButtons(SpriteSheet spriteSheet, String label, int widthMultiplier, int heightMultiplier){
        if (ImGui.beginTabItem(label)) {
            ImVec2 windowPos = new ImVec2();
            ImGui.getWindowPos(windowPos);
            ImVec2 windowSize = new ImVec2();
            ImGui.getWindowSize(windowSize);
            ImVec2 itemSpacing = new ImVec2();
            ImGui.getStyle().getItemSpacing(itemSpacing);

            float windowX2 = windowPos.x + windowSize.x;
            for (int i =0; i< spriteSheet.size(); i++){
                Sprite sprite = spriteSheet.getSprite(i);
                float spriteWidth = sprite.getWidth() * widthMultiplier;
                float spriteHeight = sprite.getHeight() * heightMultiplier;
                int id = sprite.getTexId();
                Vector2f[] texCoords = sprite.getTexCoords();
                ImGui.pushID(i);
                if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)){
                    GameObject object = Prefabs.generateSpriteObject(sprite, EngineSettings.GRID_WIDTH, EngineSettings.GRID_HEIGHT);
                    levelEditorStuff.getComponent(MouseControls.class).pickUpObject(object);
                }
                ImGui.popID();

                ImVec2 lastButtonPos = new ImVec2();
                ImGui.getItemRectMax(lastButtonPos);
                float lastButtonX2 = lastButtonPos.x;
                float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
                if (i+1 < spriteSheet.size() && nextButtonX2 < windowX2){
                    ImGui.sameLine();
                }

            }
            ImGui.endTabItem();
        }
    }

    private void drawSpritePlayerButtons(SpriteSheet spriteSheet, String label, int widthMultiplier, int heightMultiplier){
        if (ImGui.beginTabItem(label)) {
            Sprite sprite = spriteSheet.getSprite(0);
            ImVec2 windowPos = new ImVec2();
            ImGui.getWindowPos(windowPos);
            ImVec2 windowSize = new ImVec2();
            ImGui.getWindowSize(windowSize);
            ImVec2 itemSpacing = new ImVec2();
            ImGui.getStyle().getItemSpacing(itemSpacing);
            float windowX2 = windowPos.x + windowSize.x;
            float spriteWidth = sprite.getWidth() * widthMultiplier;
            float spriteHeight = sprite.getHeight() * heightMultiplier;
            int id = sprite.getTexId();
            Vector2f[] texCoords = sprite.getTexCoords();
            if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)){
                GameObject object = Prefabs.generatePlayer(spriteSheet, label, 0, true);
                levelEditorStuff.getComponent(MouseControls.class).pickUpObject(object);
            }
            if (ImGui.isItemHovered()){
                ImGui.beginTooltip();
                ImGui.text("Player");
                //ImGui.image(id, sprite.getWidth() * 4, sprite.getHeight() * 4, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y);
                ImGui.endTooltip();
            }
            ImGui.sameLine();

            ImGui.endTabItem();
        }

    }
    private void drawSpriteAnimatedButtons(SpriteSheet spriteSheet, String label, int widthMultiplier, int heightMultiplier, int defaultFrame, int endFrame, int startFrame, float defaultFrameTime, boolean isLoop ){
        if (ImGui.beginTabItem(label)) {
            Sprite sprite = spriteSheet.getSprite(0);
            ImVec2 windowPos = new ImVec2();
            ImGui.getWindowPos(windowPos);
            ImVec2 windowSize = new ImVec2();
            ImGui.getWindowSize(windowSize);
            ImVec2 itemSpacing = new ImVec2();
            ImGui.getStyle().getItemSpacing(itemSpacing);

            float windowX2 = windowPos.x + windowSize.x;
            float spriteWidth = sprite.getWidth() * widthMultiplier;
            float spriteHeight = sprite.getHeight() * heightMultiplier;
            int id = sprite.getTexId();
            Vector2f[] texCoords = sprite.getTexCoords();
            if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)){
                //GameObject object = Prefabs.generateMario(spriteSheet, label, 0, true);
                GameObject object = Prefabs.generateAnimation(spriteSheet, label, defaultFrame, defaultFrameTime, isLoop, endFrame,startFrame );
                levelEditorStuff.getComponent(MouseControls.class).pickUpObject(object);
            }
            ImGui.sameLine();

            ImGui.endTabItem();
        }
    }


}
