package fr.mrqsdf.bossrush.res;

public enum EntityType {

    PLAYER("player"),
    SLIME("slime");

    EntityType(String name){
        this.name = name;
    }

    private final String name;

    public String getName(){
        return name;
    }

}
