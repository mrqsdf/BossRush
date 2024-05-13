package fr.mrqsdf.bossrush.component;

import fr.mrqsdf.bossrush.res.EntityType;
import fr.mrqsdf.engine2d.components.Component;
import fr.mrqsdf.engine2d.components.Sprite;
import fr.mrqsdf.engine2d.components.SpriteRenderer;
import fr.mrqsdf.engine2d.jade.GameObject;
import org.joml.Vector2f;

public abstract class EntityComponent extends Component {

    private int heal = 20;
    private int maxHeal = 20;
    private int defaultHeal = 20;
    private int mana = 20;
    private int maxMana = 20;
    private int defaultMana = 20;
    private int armor = 0;
    private int maxArmor = 0;
    private int defaultArmor = 0;
    private int damage = 2;
    private int defaultDamage = 2;
    public double criticalChance = 0;
    public double criticalDamage = 0;

    public transient int level = 1;

    public boolean isDefending = false;

    public transient GameObject healBar;
    public transient GameObject healBarEmpty;
    public transient GameObject manaBar;
    public transient GameObject manaBarEmpty;

    private boolean alive = true;
    private EntityType entityType;

    public EntityComponent(EntityType entityType){
        this.entityType = entityType;
        setLevel();
    }

    public EntityComponent(EntityType entityType, int level){
        this.entityType = entityType;
        this.level = level;
        setLevel();
    }

    private void setLevel(){
        maxHeal = defaultHeal + 5 * level-1;
        heal = maxHeal;
        maxMana = defaultMana + 5 * level-1;
        mana = maxMana;
        maxArmor = defaultArmor * level-1;
        armor = maxArmor;
        damage = defaultDamage + 2 * level-1;
    }

    public void levelUp(){
        level++;
        setLevel();
    }

    public int getDamage(){
        return damage;
    }

    public int getHeal() {
        return heal;
    }

    public int getMaxHeal() {
        return maxHeal;
    }

    public int getMana() {
        return mana;
    }

    public int getMaxMana() {
        return maxMana;
    }

    public int getArmor() {
        return armor;
    }

    public int getMaxArmor() {
        return maxArmor;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive){
        this.alive = alive;
    }

    public void setHeal(int heal){
        this.heal = heal;
    }

    public void setMana(int mana){
        this.mana = mana;
    }

    public void setDefaultHeal(int defaultHeal) {
        this.defaultHeal = defaultHeal;
    }

    public void setDefaultMana(int defaultMana) {
        this.defaultMana = defaultMana;
    }

    public void setDefaultArmor(int defaultArmor) {
        this.defaultArmor = defaultArmor;
    }

    public void setDefaultDamage(int defaultDamage) {
        this.defaultDamage = defaultDamage;
    }

    public void receiveDamage(int damageTake){
        if (heal > 0) {
            heal -= Math.max(0, damageTake - (isDefending ? armor : 0));
        } else {
            heal = 0;
        }
        if (heal <=0) alive = false;
        isDefending = false;
    }

    public void receiveHeal(int helling){
        heal += helling;
        if (heal > maxHeal) heal = maxHeal;
        isDefending = false;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public float getHealPercent(){
        return (float) this.heal / this.maxHeal;
    }
    public float getManaPercent(){
        return (float) mana / maxMana;
    }

    public void setHealBar(){
        Sprite sprite = healBar.getComponent(SpriteRenderer.class).getSprite();
        Vector2f[] texCoords = {
                new Vector2f(getHealPercent(), 1),
                new Vector2f(getHealPercent(), 0),
                new Vector2f(0, 0),
                new Vector2f(0, 1)
        };
        healBar.transform.scale.x = healBarEmpty.transform.scale.x * getHealPercent();
        if (healBarEmpty.transform.scale.x < 0) {
            healBar.transform.position.x = healBarEmpty.transform.position.x + (1 - getHealPercent()) / 2;
        } else if (healBarEmpty.transform.scale.x >= 0) {
            healBar.transform.position.x = healBarEmpty.transform.position.x - (1 - getHealPercent()) / 2;
        }
        sprite.setTexCoords(texCoords);
    }
    public void setManaBar(){
        Sprite sprite = manaBar.getComponent(SpriteRenderer.class).getSprite();
        Vector2f[] texCoords = {
                new Vector2f(getHealPercent(), 1),
                new Vector2f(getHealPercent(), 0),
                new Vector2f(0, 0),
                new Vector2f(0, 1)
        };
        healBar.transform.scale.x = manaBarEmpty.transform.scale.x * getHealPercent();
        if (manaBarEmpty.transform.scale.x < 0) {
            manaBar.transform.position.x = manaBarEmpty.transform.position.x + (1 - getHealPercent()) / 2;
        } else if (manaBarEmpty.transform.scale.x >= 0) {
            manaBar.transform.position.x = manaBarEmpty.transform.position.x - (1 - getHealPercent()) / 2;
        }
        sprite.setTexCoords(texCoords);
    }

}
