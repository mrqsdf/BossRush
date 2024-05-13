package fr.mrqsdf.engine2d.utils;

import fr.mrqsdf.engine2d.editor.AssetsWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Folder {

    public List<Folder> folders = new ArrayList<>();
    public List<File> images = new ArrayList<>();
    public List<File> spriteSheets = new ArrayList<>();

    public String path;

    public Folder(String path) {
        this.path = path;
        File folder = new File(path);
        if (!folder.exists()) {
            createFolder(folder);
        }

        File[] files = folder.listFiles();
        assert files != null;
        for (File file : files) {
            if (file.isDirectory()) {
                folders.add(new Folder(file.getPath()));
            } else {
                if (isSpriteSheet(file)){
                    AssetsWindow as = AssetsWindow.getAssetsWindow(file.getPath());
                    AssetsWindow.loadSpriteSheets(as);
                    AssetsWindow.assetsWindowList.add(as);
                }
            }
        }
    }

    public static boolean isImageFile(File file) {
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(".png") || fileName.endsWith(".jpeg") || fileName.endsWith(".jpg");
    }

    public static boolean isSpriteSheet(File file){
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(".spsheet");
    }

    public void createFolder(File folder) {
        folder.mkdir();
    }

    public String getPath() {
        return path;
    }

    public String getPreviousPath() {
        if (path.contains("/")){
            return path.substring(0, path.lastIndexOf("/"));
        } else {
            return path;
        }
    }

    public Folder getFolder(String folderName) {
        for (Folder folder : folders) {
            if (folder.path.endsWith(folderName)) {
                return folder;
            }
        }
        return null;
    }

    public Folder getFolderPath(String folderPath) {
        for (Folder folder : folders) {
            if (folder.path.equals(folderPath)) {
                return folder;
            }
        }
        return null;
    }




}
