package fr.mrqsdf.engine2d.font;

import fr.mrqsdf.engine2d.components.Sprite;

import java.util.HashMap;
import java.util.Map;

public class TextData {

    public static Map<Character, Sprite> characters = new HashMap<>();

    public static void addCharacter(char c, Sprite sprite){
        characters.put(c, sprite);
    }

    public static Sprite getCharacter(char c){
        return characters.get(c);
    }

    public static float spaceEscape = 0.25f;
    public static float spaceWidth = 0.7f;



}
