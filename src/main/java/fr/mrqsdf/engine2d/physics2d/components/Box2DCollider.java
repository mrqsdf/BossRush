package fr.mrqsdf.engine2d.physics2d.components;

import fr.mrqsdf.engine2d.components.Component;
import fr.mrqsdf.engine2d.renderer.DebugDraw;
import org.joml.Vector2f;

public class Box2DCollider extends Component {

    private Vector2f halfSize = new Vector2f(1);
    private Vector2f origin = new Vector2f();
    protected Vector2f offset = new Vector2f();

    public Vector2f getHalfSize() {
        return halfSize;
    }

    public void setHalfSize(Vector2f halfSize) {
        this.halfSize = halfSize;
    }

    public Vector2f getOrigin() {
        return this.origin;
    }

    @Override
    public void editorUpdate(float dt) {
        Vector2f center = new Vector2f(this.gameObject.transform.position).add(this.offset);
        DebugDraw.addBox2D(center, this.halfSize, this.gameObject.transform.rotation);
    }

    public void setOffset(Vector2f offset) {
        this.offset.set(offset);
    }
    public Vector2f getOffset() {
        return offset;
    }



}
