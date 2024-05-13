package fr.mrqsdf.bossrush;

import fr.mrqsdf.bossrush.component.PlayerComponent;
import fr.mrqsdf.bossrush.scene.GameSceneInitializer;
import fr.mrqsdf.engine2d.MainEngine;
import fr.mrqsdf.engine2d.jade.Window;
import fr.mrqsdf.engine2d.utils.ComponentAdder;

public class Main {
    public static void main(String[] args) {
        Window.setGameScene(new GameSceneInitializer());

        new ComponentAdder("add PlayerComponent", new PlayerComponent());

        MainEngine.main(args);
    }
}