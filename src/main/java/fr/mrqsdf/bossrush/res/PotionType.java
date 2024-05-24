package fr.mrqsdf.bossrush.res;

public enum PotionType {

    HEAL("Heal Potion", "heal Player for 5 HP"),
    MANA("Mana Potion", "Regen mana player for 5 MP"),
    POISON("Poison Potion", "Poison the enemy for 1 HP");

    public String name;
    public String effect;

    PotionType(String name, String effect){
        this.name = name;
        this.effect = effect;
    }

}
