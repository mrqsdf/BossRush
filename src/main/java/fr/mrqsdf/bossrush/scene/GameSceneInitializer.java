package fr.mrqsdf.bossrush.scene;

import fr.mrqsdf.bossrush.animation.mobs.SlimeAnimation;
import fr.mrqsdf.bossrush.animation.player.PlayerAnimation;
import fr.mrqsdf.bossrush.component.*;
import fr.mrqsdf.bossrush.res.DisplayState;
import fr.mrqsdf.engine2d.AssetsGestion;
import fr.mrqsdf.engine2d.components.HudComponent;
import fr.mrqsdf.engine2d.components.HudObjectComponent;
import fr.mrqsdf.engine2d.components.SpriteRenderer;
import fr.mrqsdf.engine2d.editor.AssetsWindow;
import fr.mrqsdf.engine2d.jade.GameObject;
import fr.mrqsdf.engine2d.jade.Prefabs;
import fr.mrqsdf.engine2d.scenes.Scene;
import fr.mrqsdf.engine2d.scenes.SceneInitializer;
import org.joml.Vector2f;

public class GameSceneInitializer extends SceneInitializer {


    @Override
    public void init(Scene scene) {
        AssetsGestion.setAssets();
        GameObject gameCamera = scene.createGameObject("GameCamera");
        gameCamera.setNoSerialize();
        gameCamera.addComponent(new MouseControls());
        gameCamera.addComponent(new KeyControls());
        gameCamera.addComponent(new GameCamera(scene.camera()));
        gameCamera.start();
        scene.addGameObjectToScene(gameCamera);

        GamePlay.generateLevel(scene, gameCamera);




    }

    @Override
    public void loadResources(Scene scene) {
        AssetsGestion.addAssets();

        scene.loadRessource(scene);
    }

    @Override
    public void imgui() {

    }



}
