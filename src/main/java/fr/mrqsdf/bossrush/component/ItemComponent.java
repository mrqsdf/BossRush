package fr.mrqsdf.bossrush.component;

import fr.mrqsdf.bossrush.res.ItemType;
import fr.mrqsdf.engine2d.components.Component;

public abstract class ItemComponent extends Component {

    public ItemType itemType;
    public String name;
    public String effect;
    public boolean usable = false;

    public ItemComponent(ItemType type) {
        this.itemType = type;
    }

    public void use(GameCamera gameCamera){

    }




}
