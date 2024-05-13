package fr.mrqsdf.bossrush.scene;

import fr.mrqsdf.bossrush.animation.mobs.SlimeAnimation;
import fr.mrqsdf.bossrush.animation.player.PlayerAnimation;
import fr.mrqsdf.bossrush.component.DisplayComponent;
import fr.mrqsdf.bossrush.component.GameCamera;
import fr.mrqsdf.bossrush.component.KeyControls;
import fr.mrqsdf.bossrush.component.MouseControls;
import fr.mrqsdf.bossrush.res.DisplayState;
import fr.mrqsdf.engine2d.AssetsGestion;
import fr.mrqsdf.engine2d.components.HudComponent;
import fr.mrqsdf.engine2d.components.HudObjectComponent;
import fr.mrqsdf.engine2d.components.SpriteRenderer;
import fr.mrqsdf.engine2d.editor.AssetsWindow;
import fr.mrqsdf.engine2d.jade.GameObject;
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

        GameObject hudGame = scene.createGameObject("HudGame");
        hudGame.setNoSerialize();
        HudComponent hudComponent = new HudComponent();

        drawDisplay("HudGame_Attack",3.75f,2f, hudComponent, scene, DisplayState.ATTACK);
        drawDisplay("HudGame_Defence",3.75f,1.70f, hudComponent, scene, DisplayState.DEFEND);
        drawDisplay("HudGame_Inventory",3.75f,1.4f, hudComponent, scene, DisplayState.INVENTORY);

        hudGame.addComponent(hudComponent);
        scene.addGameObjectToScene(hudGame);

        GameObject playerHealHud = scene.createGameObject("PlayerHealHud");
        playerHealHud.setNoSerialize();
        HudComponent playerHealHudComponent = new HudComponent();
        drawDisplay("PlayerHealHudFull",2,2.175f,1f,0.175f, playerHealHudComponent, scene, DisplayState.PLAYER_HEAL_HUD_FULL,"assets/spritesheets/hud/HealBar.spsheet", 5);
        drawDisplay("PlayerHealHudEmpty",2,2.175f,1f,0.175f, playerHealHudComponent, scene, DisplayState.PLAYER_HEAL_HUD_EMPTY,"assets/spritesheets/hud/HealBarEmpty.spsheet",4);

        playerHealHud.addComponent(playerHealHudComponent);
        scene.addGameObjectToScene(playerHealHud);

        GameObject player = PlayerAnimation.playerGameObject(
                AssetsWindow.getSpriteSheet("assets/spritesheets/entity/player/PlayerSpriteSheet1.spsheet"),
                AssetsWindow.getSpriteSheet("assets/spritesheets/entity/player/PlayerSpriteSheet2.spsheet"),
                1f,1,2f,0.97f);
        player.setNoSerialize();
        scene.addGameObjectToScene(player);

        GameObject mobHealHud = scene.createGameObject("MobHealHud");
        mobHealHud.setNoSerialize();
        HudComponent mobHealHudComponent = new HudComponent();
        drawDisplay("MobHealHudFull",player.transform.position.x + 3.5f,2.175f,-1f,0.175f, mobHealHudComponent, scene, DisplayState.MOB_HEAL_HUD_FULL,"assets/spritesheets/hud/HealBar.spsheet", 5);
        drawDisplay("MobHealHudEmpty",player.transform.position.x + 3.5f,2.175f,-1f,0.175f, mobHealHudComponent, scene, DisplayState.MOB_HEAL_HUD_EMPTY,"assets/spritesheets/hud/HealBarEmpty.spsheet",4);

        mobHealHud.addComponent(mobHealHudComponent);
        scene.addGameObjectToScene(mobHealHud);

        GameObject slime = SlimeAnimation.playerGameObject(
                AssetsWindow.getSpriteSheet("assets/spritesheets/entity/slime/SlimeSpriteSheet.spsheet"),
                1f,1,player.transform.position.x + 3.5f,0.73f);
        slime.setNoSerialize();
        scene.addGameObjectToScene(slime);

        gameCamera.getComponent(GameCamera.class).setActualEnemy(slime);

    }

    @Override
    public void loadResources(Scene scene) {
        AssetsGestion.addAssets();

        scene.loadRessource(scene);
    }

    @Override
    public void imgui() {

    }

    private static void drawDisplay(String label, float posX, float posY, HudComponent hudComponent, Scene scene, DisplayState state){
        GameObject hudAttack = scene.createGameObject(label);
        hudAttack.setNoSerialize();
        hudAttack.addComponent(new HudObjectComponent());
        SpriteRenderer hudAttackRenderer = new SpriteRenderer();
        hudAttackRenderer.setSprite(AssetsWindow.getSpriteSheet("assets/spritesheets/hud/display64.16.spsheet").getSprite(0));
        hudAttack.transform.scale = new Vector2f(1f,0.25f);
        hudAttack.transform.zIndex = 5;
        hudAttack.transform.position = new Vector2f(posX, posY);
        hudAttack.addComponent(hudAttackRenderer);
        hudAttack.addComponent(new DisplayComponent(state));
        scene.addGameObjectToScene(hudAttack);
        hudComponent.addObject(hudAttack);
    }
    public static void drawDisplay(String label, float posX, float posY,float sizeX, float sizeY, HudComponent hudComponent, Scene scene, DisplayState state, String filePath, int zIndex){
        GameObject hudAttack = scene.createGameObject(label);
        hudAttack.setNoSerialize();
        hudAttack.addComponent(new HudObjectComponent());
        SpriteRenderer hudAttackRenderer = new SpriteRenderer();
        hudAttackRenderer.setSprite(AssetsWindow.getSpriteSheet(filePath).getSprite(0).clone());
        hudAttack.transform.scale = new Vector2f(sizeX,sizeY);
        hudAttack.transform.zIndex = zIndex;
        hudAttack.transform.position = new Vector2f(posX, posY);
        hudAttack.addComponent(hudAttackRenderer);
        hudAttack.addComponent(new DisplayComponent(state));
        scene.addGameObjectToScene(hudAttack);
        hudComponent.addObject(hudAttack);
    }
}
