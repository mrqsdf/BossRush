package fr.mrqsdf.engine2d.editor.input;

import fr.mrqsdf.engine2d.editor.AssetsWindow;

import java.nio.file.Paths;

public class SpriteSheetInput extends InputNewFile{


    public SpriteSheetInput() {
        super("New SpriteSheet File", "assets/spritesheets");
    }


    public void use(){
        AssetsWindow assetsWindow = new AssetsWindow(pathFile.toString(), fileName, "",0,0,0,0);
        AssetsWindow.saveAssetsWindow(assetsWindow);
        AssetsWindow.loadAssetsWindow(assetsWindow);
        pathFile = Paths.get("assets/spritesheets");
        fileName = "";
        isShow = false;
    }

}
