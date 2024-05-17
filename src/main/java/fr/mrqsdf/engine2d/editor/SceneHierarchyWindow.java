package fr.mrqsdf.engine2d.editor;

import fr.mrqsdf.engine2d.jade.GameObject;
import fr.mrqsdf.engine2d.jade.Window;
import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;

import java.util.List;

public class SceneHierarchyWindow {

    private static String payloadDragDropType = "SceneHierarchy";
    public void imgui(){
        ImGui.begin("Scene Hierarchy");
        List<GameObject> gameObjects = Window.getScene().getGameObjects();

        int index = 0;
        for (GameObject gameObject : gameObjects){
            if (!gameObject.doSerialisation()){
                continue;
            }

            boolean treeNodeOpen = doTreeNode(gameObject, index);

            //si le noeud est cliqué, set le gameObject selectionné
            if (ImGui.isItemClicked()){
                Window.getImGuiLayer().getPropertiesWindows().setActiveGameObject(gameObject);
            }

            if (treeNodeOpen){
                ImGui.treePop();
            }
            index++;
        }



        ImGui.end();
    }

    private boolean doTreeNode(GameObject gameObject, int index){
        ImGui.pushID(index);
        boolean treeNodeOpen = ImGui.treeNodeEx(
                gameObject.name,
                ImGuiTreeNodeFlags.DefaultOpen |
                        ImGuiTreeNodeFlags.OpenOnArrow |
                        ImGuiTreeNodeFlags.FramePadding |
                        ImGuiTreeNodeFlags.SpanAvailWidth,
                gameObject.name
        );
        ImGui.popID();



        if (ImGui.beginDragDropSource()){
            ImGui.setDragDropPayloadObject(payloadDragDropType, gameObject);
            ImGui.text(gameObject.name);
            ImGui.endDragDropSource();
        }
        if (ImGui.beginDragDropTarget()){
            Object payload = ImGui.acceptDragDropPayloadObject(payloadDragDropType);
            if (payload != null){
                if (payload.getClass().isAssignableFrom(GameObject.class)){
                    GameObject payloadGameObject = (GameObject) payload;
                    System.out.println("Dropped " + payloadGameObject.name + " onto " + gameObject.name);
                }
            }
            ImGui.endDragDropTarget();
        }

        return treeNodeOpen;
    }

}
