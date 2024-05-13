package fr.mrqsdf.engine2d.editor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.mrqsdf.engine2d.components.SpriteSheet;
import fr.mrqsdf.engine2d.jade.Window;
import fr.mrqsdf.engine2d.utils.AssetPool;
import fr.mrqsdf.engine2d.utils.Saved;
import imgui.ImGui;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AssetsWindow {

    public transient static AssetsWindow actualAssetsWindow = null;
    public static List<AssetsWindow> assetsWindowList = new ArrayList<>();

    private String savePath = "";
    public String fileName = "";
    public String spritePath = "";
    public boolean animation = false;

    public int spriteWidth = 0;
    public int spriteHeight = 0;
    public int numSprites = 0;
    public int spacing = 0;

    public AssetsWindow(){
        savePath = "assets/spritesheets";
    }
    public AssetsWindow(String savePath){
        this.savePath = savePath;
    }
    public AssetsWindow(String savePath, String fileName, String spritePath, int spriteWidth, int spriteHeight, int numSprites, int spacing){
        this.savePath = savePath;
        this.fileName = fileName;
        this.spritePath = spritePath;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.numSprites = numSprites;
        this.spacing = spacing;
    }

    public void imgui(){
        ImGui.begin("Assets");
        if (ImGui.button("Add SpriteSheet")){
            actualAssetsWindow = new AssetsWindow();
        }

        if (actualAssetsWindow !=null){
            if (Window.DEBUG_BUILD)actualAssetsWindow.savePath = JImGui.inputText("Save Path", actualAssetsWindow.savePath);
            actualAssetsWindow.fileName = JImGui.inputText("File Name", actualAssetsWindow.fileName);
            ImGui.pushID(1);
            ImGui.columns(2);
            ImGui.setColumnWidth(0, 220f);
            ImGui.text("Sprite Path");
            ImGui.nextColumn();
            ImGui.button(actualAssetsWindow.spritePath, 250f,30);
            if (ImGui.beginDragDropTarget()){
                Object payload = ImGui.acceptDragDropPayloadObject("SPRITE_BROWSER_ITEM");
                if (payload !=null){
                    if (payload instanceof String string){
                        actualAssetsWindow.spritePath = string;
                    }
                }
            }
            ImGui.columns(1);
            ImGui.popID();
            try{
                Field[] fields = actualAssetsWindow.getClass().getDeclaredFields();

                for (Field field : fields){

                    boolean isTransient = Modifier.isTransient(field.getModifiers());
                    if (isTransient){
                        continue;
                    }
                    boolean isPrivate = Modifier.isPrivate(field.getModifiers());
                    if (isPrivate){
                        field.setAccessible(true);
                    }

                    Class type = field.getType();
                    Object value = field.get(actualAssetsWindow);
                    String name = field.getName();

                    if (type == int.class){
                        int val = (int) value;
                        field.set(actualAssetsWindow, JImGui.dragInt(name, val));
                    } else if (type == float.class) {
                        float val = (float) value;
                        field.set(actualAssetsWindow, JImGui.dragFloat(name, val));
                    } else if (type == boolean.class) {
                        boolean val = (boolean) value;
                        if (ImGui.checkbox(name + ": ", val)){
                            val = !val;
                            field.set(actualAssetsWindow, val);
                        }
                    }

                    if (isPrivate){
                        field.setAccessible(false);
                    }
                }
            }catch (IllegalAccessException e){
                e.printStackTrace();
            }
            if (ImGui.button("Save")){
                saveAssetsWindow(actualAssetsWindow);
                loadSpriteSheets(actualAssetsWindow);
                actualAssetsWindow = null;
            }
        }

        ImGui.end();
    }

    public static void loadSpriteSheets(AssetsWindow assetsWindow){
        AssetPool.addOrReplaceSpriteSheet(assetsWindow.spritePath, new SpriteSheet(
                AssetPool.getTexture(assetsWindow.spritePath), assetsWindow.spriteWidth, assetsWindow.spriteHeight, assetsWindow.numSprites, assetsWindow.spacing, assetsWindow.fileName
        ));
    }

    public static void saveAssetsWindow(AssetsWindow assetsWindow){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(assetsWindow);
        String base64 = Saved.toBase64(json);
        String path = assetsWindow.savePath +"/"+ assetsWindow.fileName + ".spsheet";
        try {
            FileWriter writer = new FileWriter(path);
            writer.write(base64);
            writer.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
        if (!assetsWindowList.contains(assetsWindow)){
            assetsWindowList.add(assetsWindow);
        }
    }

    public static void loadAssetsWindow(String filePath){
        String json;
        try {
            json = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String base64 = Saved.fromBase64(json);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        AssetsWindow loaded = gson.fromJson(base64, AssetsWindow.class);
        actualAssetsWindow = loaded;
    }
    public static void loadAssetsWindow(AssetsWindow assetsWindow){
        actualAssetsWindow = assetsWindow;
    }

    public static AssetsWindow getAssetsWindow(String filePath){
        String json;
        try {
            json = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String base64 = Saved.fromBase64(json);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        AssetsWindow loaded = gson.fromJson(base64, AssetsWindow.class);
        return loaded;
    }
    public static AssetsWindow getAssetsWindowFile(String filePath){

        for (AssetsWindow as : assetsWindowList){
            String savePath = as.savePath + "/" + as.fileName +".spsheet";
            savePath = savePath.replace('/', '\\');
            filePath = filePath.replace('/', '\\');
            if (Objects.equals(savePath, filePath)) return as;
        }
        return null;
    }

    public static SpriteSheet getSpriteSheet(String filePath){
        AssetsWindow as = getAssetsWindowFile(filePath);
        if (as != null){
            return AssetPool.getSpriteSheet(as.spritePath);
        }
        return null;
    }
    public static SpriteSheet getSpriteSheet(AssetsWindow assetsWindow){
        return AssetPool.getSpriteSheet(assetsWindow.spritePath);
    }

}
