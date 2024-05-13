package fr.mrqsdf.engine2d.physics2d.components;

import fr.mrqsdf.engine2d.components.Component;
import org.joml.Vector2f;

public class CircleCollider extends Component {
    protected Vector2f offset = new Vector2f();
    private float radius = 1.0f;

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public Vector2f getOffset() {
        return offset;
    }
    public void setOffset(Vector2f offset) {
        this.offset.set(offset);
    }

}
