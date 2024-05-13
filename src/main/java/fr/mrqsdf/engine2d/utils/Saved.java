package fr.mrqsdf.engine2d.utils;

public class Saved {

    public static String toBase64(String str){
        return java.util.Base64.getEncoder().encodeToString(str.getBytes());
    }

    public static String fromBase64(String str){
        return new String(java.util.Base64.getDecoder().decode(str));
    }

}
