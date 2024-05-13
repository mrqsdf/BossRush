package fr.mrqsdf.engine2d.editor;

import fr.mrqsdf.engine2d.components.Sprite;
import fr.mrqsdf.engine2d.components.SpriteSheet;
import fr.mrqsdf.engine2d.editor.input.LevelInput;
import fr.mrqsdf.engine2d.editor.input.SpriteSheetInput;
import fr.mrqsdf.engine2d.jade.Window;
import fr.mrqsdf.engine2d.observers.EventSystem;
import fr.mrqsdf.engine2d.observers.events.Event;
import fr.mrqsdf.engine2d.observers.events.EventType;
import fr.mrqsdf.engine2d.renderer.Texture;
import fr.mrqsdf.engine2d.utils.AssetPool;
import fr.mrqsdf.engine2d.utils.Levels;
import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiMouseButton;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ContentBrowserPanel {


    private static Path assetsPath = Paths.get("assets");

    private static Object actualDrag = null;

    private static Path currentPath = assetsPath;

    static float[] padding = new float[]{16.0f};
    static float[] thumbnailSize = new float[]{100.0f};

    public static boolean isHover = false;
    public static Path hoverPath = null;

    private void setCurrentPath(Path path){
        currentPath = path;
    }

    public void imgui(){

        ImGui.begin("Content Browser");

        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.2f,0.2f, 0.2f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.5f,0.5f,0.5f,1.0f);
        ImGui.pushStyleColor(ImGuiCol.Button, 0,0,0,0);


        float cellSize = thumbnailSize[0] + padding[0];

        float panelWidth = ImGui.getContentRegionAvailX();
        int columnCount = (int) (panelWidth / cellSize);
        if (columnCount < 1){
            columnCount = 1;
        }

        ImGui.columns(columnCount, "mycolumns", false);
        if (!currentPath.toString().equals(assetsPath.toString())){
            SpriteSheet backSprite = AssetPool.getSpriteSheet("assets/images/engine/back.png");
            Sprite back = backSprite.getSprite(0);
            int id = back.getTexId();
            if (ImGui.imageButton(id, thumbnailSize[0], thumbnailSize[0], 0,1,1,0)){
                setCurrentPath(currentPath.getParent());
            }
            if (ImGui.beginDragDropTarget()){
                Object payload = ImGui.acceptDragDropPayloadObject("MOVE_BROWSER_ITEM");
                if (payload !=null){
                    if (payload instanceof String string){
                        Path source = Paths.get(string);
                        Path destination = currentPath.getParent();
                        try {
                            Files.move(source, destination.resolve(source.getFileName()));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                ImGui.endDragDropTarget();
            }
            ImGui.nextColumn();
        }
        int i =0;
        File assetsDirectory = new File(currentPath.toString());
        if (assetsDirectory.exists()){
            File[] files = assetsDirectory.listFiles();
            assert files != null;
            for (File file : files){
                ImGui.pushID(i++);
                String filePath = file.getPath();
                Path relativePath = assetsPath.relativize(file.toPath());
                String relativePathString = relativePath.toString();
                String fileName = relativePath.getFileName().toString();
                String stem = Paths.get(file.toString()).getFileName().toString().replaceFirst("[.][^.]+$", "");

                if (isImageFile(file)){
                    Texture texture = AssetPool.getTexture(filePath);
                    float spriteWidth = texture.getWidth()*2;
                    float spriteHeight = texture.getHeight()*2;
                    int id = texture.getId();
                    ImGui.imageButton(id, thumbnailSize[0], thumbnailSize[0], 0, 1, 1, 0);
                    if (ImGui.beginDragDropSource()){
                        ImGui.setDragDropPayloadObject("MOVE_BROWSER_ITEM", filePath);
                        ImGui.setDragDropPayloadObject("SPRITE_BROWSER_ITEM", filePath);
                        ImGui.image(id, spriteWidth, spriteHeight, 0, 1, 1, 0);
                        ImGui.textWrapped(stem);
                        ImGui.endDragDropSource();
                    }
                    if (ImGui.isItemHovered()){
                        ImGui.beginTooltip();
                        ImGui.image(id, spriteWidth, spriteHeight, 0, 1, 1, 0);
                        ImGui.endTooltip();
                    }
                } else if (isLevelFile(file)) {
                    Sprite sprite = AssetPool.getSpriteSheet("assets/images/engine/levels.png").getSprite(0);
                    int id = sprite.getTexId();
                    ImGui.imageButton(id, thumbnailSize[0]+10, thumbnailSize[0]+10, 0, 1, 1, 0);
                    Levels level = Levels.getLevel(stem);
                    if (ImGui.beginDragDropSource()){
                        ImGui.setDragDropPayloadObject("CONTENT_BROWSER_ITEM", level);
                        ImGui.setDragDropPayloadObject("MOVE_BROWSER_ITEM", filePath);
                        ImGui.image(id, thumbnailSize[0], thumbnailSize[0], 0, 1, 1, 0);
                        ImGui.textWrapped(stem);
                        ImGui.endDragDropSource();
                    }

                    if (ImGui.isItemHovered() && ImGui.isMouseDoubleClicked(ImGuiMouseButton.Left)){
                        Window.getScene().save();
                        Window.getScene().clearSerializableGameObjects();
                        Window.getImGuiLayer().getPropertiesWindows().clearSelected();
                        Window.getScene().setLevelID(level.id);
                        EventSystem.notify(null, new Event(EventType.LOAD_LEVEL));
                        if (Window.DEBUG_BUILD) System.out.println("Selected level : " + level.name);
                    }
                } else if (isSpriteSheetFile(file)) {
                    Sprite sprite = AssetPool.getSpriteSheet("assets/images/engine/spriteSheet.png").getSprite(0);
                    int id = sprite.getTexId();

                    ImGui.imageButton(id, thumbnailSize[0], thumbnailSize[0], 0, 1, 1, 0);

                    if (ImGui.beginDragDropSource()){
                        AssetsWindow assetsWindow = AssetsWindow.getAssetsWindowFile(file.getPath());
                        System.out.println(assetsWindow);
                        ImGui.setDragDropPayloadObject("CONTENT_BROWSER_ITEM", assetsWindow);
                        ImGui.setDragDropPayloadObject("MOVE_BROWSER_ITEM", filePath);
                        ImGui.image(id, thumbnailSize[0], thumbnailSize[0], 0, 1, 1, 0);
                        ImGui.textWrapped(stem);
                        ImGui.endDragDropSource();
                    }
                    if (ImGui.isItemHovered() && ImGui.isMouseDoubleClicked(ImGuiMouseButton.Left)){
                        if (Window.DEBUG_BUILD) System.out.println("SpriteSheet: " + file.getPath());
                        AssetsWindow assetsWindow = AssetsWindow.getAssetsWindowFile(file.getPath());
                        AssetsWindow.loadAssetsWindow(assetsWindow);
                    }
                    if (ImGui.isItemHovered() && ImGui.isMouseClicked(ImGuiMouseButton.Right, true)){
                        hoverPath = Paths.get(filePath);
                    }
                } else if (file.isDirectory()) {
                    Sprite sprite = AssetPool.getSpriteSheet("assets/images/engine/folder.png").getSprite(0);
                    int id = sprite.getTexId();
                    ImGui.imageButton(id, thumbnailSize[0], thumbnailSize[0], 0, 1, 1, 0);
                    if (ImGui.beginDragDropSource()) {
                        ImGui.setDragDropPayloadObject("CONTENT_BROWSER_ITEM", filePath);
                        ImGui.setDragDropPayloadObject("MOVE_BROWSER_ITEM", filePath);
                        ImGui.image(id, thumbnailSize[0], thumbnailSize[0], 0, 1, 1, 0);
                        ImGui.textWrapped(stem);
                        ImGui.endDragDropSource();
                    }

                    if (ImGui.beginDragDropTarget()){
                        Object payload = ImGui.acceptDragDropPayloadObject("MOVE_BROWSER_ITEM");
                        if (payload !=null){
                            if (payload instanceof String string){
                                Path source = Paths.get(string);
                                Path destination = Paths.get(filePath);
                                try {
                                    Files.move(source, destination.resolve(source.getFileName()));
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                        ImGui.endDragDropTarget();
                    }

                    if (ImGui.isItemHovered() && ImGui.isMouseDoubleClicked(ImGuiMouseButton.Left)) {
                        if (file.isDirectory()) {
                            currentPath = Paths.get(file.getPath());
                        }
                    }
                }

                    ImGui.textWrapped(fileName);
                ImGui.nextColumn();
                ImGui.popID();
            }

        }

        openDataMenu(true);

        ImGui.popStyleColor();
        ImGui.popStyleColor();
        ImGui.popStyleColor();
        ImGui.end();

        hoverPath = null;
    }

    public static boolean isImageFile(File file) {
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(".png") || fileName.endsWith(".jpeg") || fileName.endsWith(".jpg");
    }
    public static boolean isLevelFile(File file) {
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(".level");
    }

    public static boolean isSpriteSheetFile(File file) {
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(".spsheet");
    }


    private static void openDataMenu(boolean isHover){
        if (ImGui.beginPopupContextWindow("ADD_FILE_CONFIG")){
            if (ImGui.beginMenu("new file")){
                if (ImGui.menuItem("Spritesheet")){
                    SpriteSheetInput spriteSheetInput = Window.getImGuiLayer().getInputNewFile(SpriteSheetInput.class);
                    if (spriteSheetInput != null){
                        spriteSheetInput.pathFile = currentPath;
                        spriteSheetInput.isShow = true;

                    }
                }
                if (ImGui.menuItem("Level")){
                    LevelInput inputNewFile = Window.getImGuiLayer().getInputNewFile(LevelInput.class);
                    if (inputNewFile != null){
                        inputNewFile.isShow = true;
                        inputNewFile.pathFile = currentPath;
                    }
                }
                if (ImGui.menuItem("delete", "suppr", isHover)){
                    hoverPath = null;
                }
                ImGui.endMenu();
            }

            ImGui.endPopup();
        }
    }

}
