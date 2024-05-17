package fr.mrqsdf.bossrush.component;

import fr.mrqsdf.engine2d.components.Component;
import fr.mrqsdf.engine2d.jade.GameObject;

import java.util.ArrayList;
import java.util.List;

public class InventoryComponent extends Component {

    private List<GameObject> items = new ArrayList<>();

    public final float offsetX = 0.23f;
    public final float offsetY = 0.23f;

    public final float newX = 0.34f;
    public final float newY = 0.35f;

    public float origineX;
    public float origineY;

    public float nextItemX;
    public float nextItemY;

    public InventoryComponent(float origineX, float origineY) {
        this.origineX = origineX;
        this.origineY = origineY;
        this.nextItemX = origineX;
        this.nextItemY = origineY;
    }

    public void addItem(GameObject item){
        items.add(item);
    }

    public void removeItem(GameObject item){
        items.remove(item);
    }

    public List<GameObject> getItems() {
        return items;
    }


    public void updateOrigine(){
        origineX = gameObject.transform.position.x - newX;
        origineY = gameObject.transform.position.y + newY;
    }

    @Override
    public void update(float dt) {
        updateOrigine();
        float x = origineX;
        float y = origineY;
        for (int i = 0; i < items.size(); i++) {
            if (i % 4 == 0 && i != 0) {
                x = origineX;
                y -= offsetY;
            }
            GameObject item = items.get(i);
            item.transform.position.x = x;
            item.transform.position.y = y;
            x += offsetX;

        }
    }

}
