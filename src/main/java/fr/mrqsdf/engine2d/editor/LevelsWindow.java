package fr.mrqsdf.engine2d.editor;

import fr.mrqsdf.engine2d.jade.Window;
import fr.mrqsdf.engine2d.observers.EventSystem;
import fr.mrqsdf.engine2d.observers.events.Event;
import fr.mrqsdf.engine2d.observers.events.EventType;
import fr.mrqsdf.engine2d.utils.Levels;
import imgui.ImGui;

public class LevelsWindow {

    public void imgui(){
        PropertiesWindows propertiesWindow = Window.getImGuiLayer().getPropertiesWindows();
        ImGui.begin("Levels");
        //bouton pour créer un nouveau level et le nommer
        if (ImGui.button("New Level")){
            Window.getScene().save();
            Levels levels = new Levels("New Level " + (Levels.levels.size() + 1));
            Window.getScene().clearSerializableGameObjects();
            propertiesWindow.clearSelected();
            Window.getScene().setLevelID(levels.id);
            Window.getScene().save();
            Window.getScene().load();
            if (Window.DEBUG_BUILD) System.out.println("Created new level");
        }
        //list des différents levels, cliqué dessus pour changer de level
        for (Levels levels : Levels.levels){
            int id = levels.id;
            if (ImGui.collapsingHeader(id + " : " + levels.name)){
                levels.name = JImGui.inputText("Name", levels.name);
                ImGui.text("ID : " + id);
                if (ImGui.button("Select")){
                    if (!levels.isLoaded){
                        Window.getScene().save();
                        Window.getScene().clearSerializableGameObjects();
                        propertiesWindow.clearSelected();
                        Window.getScene().setLevelID(id);
                        EventSystem.notify(null, new Event(EventType.LOAD_LEVEL));
                        if (Window.DEBUG_BUILD) System.out.println("Selected level : " + levels.name);
                    }
                }
                if (ImGui.button("Delete")){
                    if (!levels.isDeleted){
                        levels.isDeleted = true;
                        Levels.saveLevels();
                    } else {
                        Levels.levels.remove(levels);
                        Levels.deleteLevel();
                        Levels.saveLevels();
                        Window.getScene().clearSerializableGameObjects();
                        propertiesWindow.clearSelected();
                        Window.getScene().setLevelID(Levels.getMinId());
                        Window.getScene().load();
                    }
                }
            }
        }
        ImGui.end();
    }
}
