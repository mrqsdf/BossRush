package fr.mrqsdf.engine2d.editor;

import fr.mrqsdf.engine2d.observers.EventSystem;
import fr.mrqsdf.engine2d.observers.events.Event;
import fr.mrqsdf.engine2d.observers.events.EventType;
import imgui.ImGui;

public class MenuBar {

    public void imgui(){
        ImGui.beginMainMenuBar();

        if (ImGui.beginMenu("File")){
            if (ImGui.menuItem("Save", "Ctrl+S")){
                EventSystem.notify(null, new Event(EventType.SAVE_LEVEL));
            }
            if (ImGui.menuItem("Load", "Ctrl+O")){
                EventSystem.notify(null, new Event(EventType.LOAD_LEVEL));
            }

            ImGui.endMenu();
        }

        ImGui.endMainMenuBar();
    }

}
