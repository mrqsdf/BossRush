package fr.mrqsdf.engine2d.editor.input;

import fr.mrqsdf.engine2d.editor.JImGui;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public abstract class InputNewFile {

    public boolean isShow = false;
    public Path pathFile;
    private static final Path assetsPath = Paths.get("assets");

    public String title;
    public String fileName = "";


    public InputNewFile(String title, String path){
        pathFile = Paths.get(path);
        this.title = title;
    }

    public void imgui(){
        if (isShow){
            boolean windowContentVisible = ImGui.begin(title, ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoDocking |ImGuiWindowFlags.NoResize |ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoScrollbar);
            if (!windowContentVisible){
                ImGui.end();
            } else {
                ImGui.text(pathFile.toString());
                ImGui.setWindowPos(300,300);
                ImGui.setWindowSize(500,135);
                fileName = JImGui.inputText("FileName", this.fileName);
                if (ImGui.button("Save") && !Objects.equals(fileName, "") && !nameUse(fileName)){
                    use();
                }
                ImGui.sameLine();
                if (ImGui.button("Cancel")){
                    isShow = false;
                    fileName = "";
                }
            }
            ImGui.end();
        }
    }

    private boolean nameUse(String fileName){
        File assetsDirectory = new File(pathFile.toString());
        File[] files = assetsDirectory.listFiles();
        boolean nameUse = false;
        if (files == null) files = new File[]{};
        for (File file : files){
            String stem = Paths.get(file.toString()).getFileName().toString().replaceFirst("[.][^.]+$", "");
            if (stem.equals(fileName)){
                nameUse = true;
                break;
            }
        }
        return nameUse;
    }

    public abstract void use();

}
