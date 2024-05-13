package fr.mrqsdf.engine2d.editor.input;

import fr.mrqsdf.engine2d.jade.Window;
import fr.mrqsdf.engine2d.utils.Levels;

public class LevelInput extends InputNewFile{

    public LevelInput(){
        super("New Level File", "assets/Levels");
    }

    public void use() {
        Window.getScene().save();
        Levels levels = new Levels(fileName);
        Window.getScene().clearSerializableGameObjects();
        Window.getImGuiLayer().getPropertiesWindows().clearSelected();
        Window.getScene().setLevelID(levels.id);
        Window.getScene().save();
        Window.getScene().load();
        if (Window.DEBUG_BUILD) System.out.println("Created new level");
    }
}
