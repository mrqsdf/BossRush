package fr.mrqsdf.engine2d.editor;

import fr.mrqsdf.engine2d.jade.MouseListener;
import fr.mrqsdf.engine2d.jade.Window;
import fr.mrqsdf.engine2d.observers.EventSystem;
import fr.mrqsdf.engine2d.observers.events.Event;
import fr.mrqsdf.engine2d.observers.events.EventType;
import fr.mrqsdf.engine2d.utils.Levels;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;
import org.joml.Vector2f;

public class GameViewWindow {

    private float leftX, rightX, topY, bottomY;

    private boolean isPlaying = false;
    public void imgui() {
        ImGui.begin("Game Viewport", ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse
                    | ImGuiWindowFlags.MenuBar | ImGuiWindowFlags.NoCollapse);

        ImGui.beginMenuBar();
        if (ImGui.menuItem("Play", "", isPlaying, !isPlaying)) {
            isPlaying = true;
            EventSystem.notify(null, new Event(EventType.GAME_ENGINE_START_PLAY));
        }
        if (ImGui.menuItem("Stop", "", !isPlaying, isPlaying)) {
            isPlaying = false;
            EventSystem.notify(null, new Event(EventType.GAME_ENGINE_STOP_PLAY));
        }
        ImGui.endMenuBar();

        ImGui.setCursorPos(ImGui.getCursorPosX(), ImGui.getCursorPosY());
        ImVec2 windowSize = getLargestSizeForViewport();
        ImVec2 windowPos = getCenteredPositionForViewport(windowSize);
        ImGui.setCursorPos(windowPos.x, windowPos.y);
        leftX = windowPos.x + 10;
        rightX = windowPos.x + windowSize.x + 10;
        bottomY = windowPos.y;
        topY = windowPos.y + windowSize.y;

        int textureId = Window.getFramebuffer().getTextureId();
        ImGui.image(textureId, windowSize.x, windowSize.y, 0, 1, 1, 0);

        MouseListener.setGameViewportPos(new Vector2f(windowPos.x + 10, windowPos.y));
        MouseListener.setGameViewportSize(new Vector2f(windowSize.x, windowSize.y));

        if (ImGui.beginDragDropTarget()){

            Object payload = ImGui.acceptDragDropPayloadObject("CONTENT_BROWSER_ITEM");
            if (payload != null){
                if (payload instanceof Levels level){
                    Window.getScene().save();
                    Window.getScene().clearSerializableGameObjects();
                    Window.getImGuiLayer().getPropertiesWindows().clearSelected();
                    Window.getScene().setLevelID(level.id);
                    EventSystem.notify(null, new Event(EventType.LOAD_LEVEL));
                    if (Window.DEBUG_BUILD) System.out.println("Selected level : " + level.name);
                } else if (payload instanceof AssetsWindow assetsWindow) {
                    AssetsWindow.loadAssetsWindow(assetsWindow);
                } else if (payload instanceof String path){
                    System.out.println(path);
                }

            }


            ImGui.endDragDropTarget();
        }
        ImGui.end();
    }

    public boolean getWantCaptureMouse() {
        return MouseListener.getX() >= leftX && MouseListener.getX() <= rightX &&
                MouseListener.getY() >= bottomY && MouseListener.getY() <= topY;
    }

    private ImVec2 getLargestSizeForViewport() {
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);

        float aspectWidth = windowSize.x;
        float aspectHeight = aspectWidth / Window.getTargetAspectRatio();
        if (aspectHeight > windowSize.y) {
            // We must switch to pillarbox mode
            aspectHeight = windowSize.y;
            aspectWidth = aspectHeight * Window.getTargetAspectRatio();
        }

        return new ImVec2(aspectWidth, aspectHeight);
    }

    private ImVec2 getCenteredPositionForViewport(ImVec2 aspectSize) {
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);

        float viewportX = (windowSize.x / 2.0f) - (aspectSize.x / 2.0f);
        float viewportY = (windowSize.y / 2.0f) - (aspectSize.y / 2.0f);

        return new ImVec2(viewportX + ImGui.getCursorPosX(), viewportY + ImGui.getCursorPosY());
    }


}
