package fr.mrqsdf.bossrush.scene;

import fr.mrqsdf.bossrush.animation.mobs.SlimeAnimation;
import fr.mrqsdf.bossrush.animation.player.PlayerAnimation;
import fr.mrqsdf.bossrush.component.DisplayComponent;
import fr.mrqsdf.bossrush.component.GameCamera;
import fr.mrqsdf.bossrush.component.InventoryComponent;
import fr.mrqsdf.bossrush.res.DisplayState;
import fr.mrqsdf.engine2d.components.HudComponent;
import fr.mrqsdf.engine2d.components.HudObjectComponent;
import fr.mrqsdf.engine2d.components.SpriteRenderer;
import fr.mrqsdf.engine2d.editor.AssetsWindow;
import fr.mrqsdf.engine2d.jade.GameObject;
import fr.mrqsdf.engine2d.jade.Prefabs;
import fr.mrqsdf.engine2d.jade.Transform;
import fr.mrqsdf.engine2d.scenes.Scene;
import org.joml.Vector2f;

public class GamePlay {

    public static void generateLevel(Scene scene, GameObject gameCamera){
        //TODO
        generateHud(scene);
        Transform transform = generatePlayer(scene);
        generateEnemies(scene, gameCamera, transform);
    }

    private static void generateEnemies(Scene scene, GameObject gameCamera, Transform player){
        GameObject mobHealHud = scene.createGameObject("MobHealHud");
        mobHealHud.setNoSerialize();
        HudComponent mobHealHudComponent = new HudComponent();
        drawDisplay("MobHealHudFull",player.position.x + 3.5f,2.175f,-1f,0.175f, mobHealHudComponent, scene, DisplayState.MOB_HEAL_HUD_FULL,"assets/spritesheets/hud/HealBar.spsheet", 5);
        drawDisplay("MobHealHudEmpty",player.position.x + 3.5f,2.175f,-1f,0.175f, mobHealHudComponent, scene, DisplayState.MOB_HEAL_HUD_EMPTY,"assets/spritesheets/hud/HealBarEmpty.spsheet",4);

        mobHealHud.addComponent(mobHealHudComponent);
        scene.addGameObjectToScene(mobHealHud);

        GameObject slime = SlimeAnimation.playerGameObject(
                AssetsWindow.getSpriteSheet("assets/spritesheets/entity/slime/SlimeSpriteSheet.spsheet"),
                1f,1,player.position.x + 3.5f,0.73f);
        slime.setNoSerialize();
        scene.addGameObjectToScene(slime);

        gameCamera.getComponent(GameCamera.class).setActualEnemy(slime);
    }

    private static void generateHud(Scene scene){
        //HUDGAME
        GameObject hudGame = scene.createGameObject("HudGame");
        hudGame.setNoSerialize();
        HudComponent hudComponent = new HudComponent();
        drawDisplay("HudGame_Attack",2.63f,2f -0.08f, hudComponent, scene, DisplayState.ATTACK,0);
        drawDisplay("HudGame_Defence",2.63f,1.825f-0.08f, hudComponent, scene, DisplayState.DEFEND,1);
        //drawDisplay("HudGame_Inventory",3.75f,1.4f, hudComponent, scene, DisplayState.INVENTORY);
        hudGame.addComponent(hudComponent);
        scene.addGameObjectToScene(hudGame);

        //INVENTORY
        GameObject inventory = Prefabs.generateSpriteObject(AssetsWindow.getSpriteSheet("assets/spritesheets/hud/Inventory.spsheet").getSprite(0), 1f,1f, 2f, 1.55f, 0, 5);
        inventory.setNoSerialize();
        inventory.addComponent(new InventoryComponent(2,2));
        inventory.addComponent(new HudComponent());
        inventory.addComponent(new DisplayComponent(DisplayState.INVENTORY));
        scene.addGameObjectToScene(inventory);

        //PLAYERHEALHUD
        GameObject playerHealHud = scene.createGameObject("PlayerHealHud");
        playerHealHud.setNoSerialize();
        HudComponent playerHealHudComponent = new HudComponent();
        drawDisplay("PlayerHealHudFull",2,2.175f,1f,0.175f, playerHealHudComponent, scene, DisplayState.PLAYER_HEAL_HUD_FULL,"assets/spritesheets/hud/HealBar.spsheet", 5);
        drawDisplay("PlayerHealHudEmpty",2,2.175f,1f,0.175f, playerHealHudComponent, scene, DisplayState.PLAYER_HEAL_HUD_EMPTY,"assets/spritesheets/hud/HealBarEmpty.spsheet",4);

        playerHealHud.addComponent(playerHealHudComponent);
        scene.addGameObjectToScene(playerHealHud);
    }

    private static Transform generatePlayer(Scene scene){
        GameObject player = PlayerAnimation.playerGameObject(
                AssetsWindow.getSpriteSheet("assets/spritesheets/entity/player/PlayerSpriteSheet1.spsheet"),
                AssetsWindow.getSpriteSheet("assets/spritesheets/entity/player/PlayerSpriteSheet2.spsheet"),
                1f,1,2f,0.97f);
        player.setNoSerialize();
        scene.addGameObjectToScene(player);
        return player.transform;
    }

    private static void drawDisplay(String label, float posX, float posY, HudComponent hudComponent, Scene scene, DisplayState state, int sprite){
        GameObject hudAttack = scene.createGameObject(label);
        hudAttack.setNoSerialize();
        hudAttack.addComponent(new HudObjectComponent());
        SpriteRenderer hudAttackRenderer = new SpriteRenderer();
        hudAttackRenderer.setSprite(AssetsWindow.getSpriteSheet("assets/spritesheets/hud/Button.spsheet").getSprite(sprite));
        hudAttack.transform.scale = new Vector2f(0.25f,0.25f);
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
