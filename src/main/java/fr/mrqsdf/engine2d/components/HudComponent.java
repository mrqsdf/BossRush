package fr.mrqsdf.engine2d.components;

import fr.mrqsdf.engine2d.jade.GameObject;

import java.util.ArrayList;
import java.util.List;

public class HudComponent extends Component {

    private List<GameObject> objects = new ArrayList<>();

    private Boolean[] objectPlaced = new Boolean[12];

    public HudComponent(List<GameObject> objects) {
        this.objects = objects;
    }
    public HudComponent() {
    }

    public void addObject(GameObject object) {
        objects.add(object);
    }

    public void removeObject(GameObject object) {
        objects.remove(object);
    }

    public void updateObject(GameObject object) {
        objects.set(objects.indexOf(object), object);
    }

    public void updateObjects(List<GameObject> objects) {
        this.objects = objects;
    }

    public List<GameObject> getObjects() {
        return objects;
    }

    public void setObjectPlaced(int index, Boolean value) {
        objectPlaced[index] = value;
    }

    public Boolean getObjectPlaced(int index) {
        return objectPlaced[index];
    }

    public void moveHud(float x, float y){
        gameObject.transform.position.x += x;
        gameObject.transform.position.y += y;
        for (GameObject go : objects){
            go.transform.position.x += x;
            go.transform.position.y += y;
        }
    }

    public void teleportHud(float x, float y){
        gameObject.transform.position.x = x;
        gameObject.transform.position.y = y;
        for (GameObject go : objects){
            go.transform.position.x = x;
            go.transform.position.y = y;
        }
    }

}
