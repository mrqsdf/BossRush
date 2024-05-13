package fr.mrqsdf.engine2d.physics2d;

import fr.mrqsdf.engine2d.jade.GameObject;
import fr.mrqsdf.engine2d.jade.Transform;
import fr.mrqsdf.engine2d.physics2d.components.Box2DCollider;
import fr.mrqsdf.engine2d.physics2d.components.CircleCollider;
import fr.mrqsdf.engine2d.physics2d.components.PillboxCollider;
import fr.mrqsdf.engine2d.physics2d.components.RigidBody2D;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.joml.Vector2f;

public class Physics2D {

    private Vec2 gravity = new Vec2(0, -10.0f);
    private World world = new World(gravity);

    private float physicsTime = 0.0f;
    private float physicsTimeStep = 1.0f / 60.0f;
    private int velocityIterations = 8;
    private int positionIterations = 3;

    public void add(GameObject go) {
        RigidBody2D rb = go.getComponent(RigidBody2D.class);
        if (rb != null && rb.getRawBody() == null) {
            Transform transform = go.transform;

            BodyDef bodyDef = new BodyDef();
            bodyDef.angle = (float)Math.toRadians(transform.rotation);
            bodyDef.position.set(transform.position.x, transform.position.y);
            bodyDef.angularDamping = rb.getAngularDamping();
            bodyDef.linearDamping = rb.getLinearDamping();
            bodyDef.fixedRotation = rb.isFixedRotation();
            bodyDef.userData = rb.gameObject;
            bodyDef.gravityScale = rb.gravityScale;
            bodyDef.angularVelocity = rb.angularVelocity;
            bodyDef.bullet = rb.isContinuousCollision();

            switch (rb.getBodyType()) {
                case KINEMATIC: bodyDef.type = BodyType.KINEMATIC; break;
                case STATIC: bodyDef.type = BodyType.STATIC; break;
                case DYNAMIC: bodyDef.type = BodyType.DYNAMIC; break;
            }

            Body body = this.world.createBody(bodyDef);
            body.m_mass = rb.getMass();
            rb.setRawBody(body);
            CircleCollider circleCollider;
            Box2DCollider boxCollider;

            if ((circleCollider = go.getComponent(CircleCollider.class)) != null) {
                addCircleCollider(rb, circleCollider);
            }
            if ((boxCollider = go.getComponent(Box2DCollider.class)) != null) {
                addBox2DCollider(rb, boxCollider);
            }


        }
    }


    public void destroyGameObject(GameObject go) {
        RigidBody2D rb = go.getComponent(RigidBody2D.class);
        if (rb != null) {
            if (rb.getRawBody() != null) {
                world.destroyBody(rb.getRawBody());
                rb.setRawBody(null);
            }
        }
    }

    public void update(float dt) {
        physicsTime += dt;
        if (physicsTime >= 0.0f) {
            physicsTime -= physicsTimeStep;
            world.step(physicsTimeStep, velocityIterations, positionIterations);
        }
    }

    public void resetCircleCollider(RigidBody2D rb, CircleCollider circleCollider){
        Body body = rb.getRawBody();
        if (body == null) return;

        int size = fixtureListSize(body);
        for (int i = 0; i < size; i++){
            body.destroyFixture(body.getFixtureList());
        }

        addCircleCollider(rb, circleCollider);
        body.resetMassData();

    }

    public void resetBox2DCollider(RigidBody2D rb, Box2DCollider boxCollider){
        Body body = rb.getRawBody();
        if (body == null) return;

        int size = fixtureListSize(body);
        for (int i = 0; i < size; i++){
            body.destroyFixture(body.getFixtureList());
        }

        addBox2DCollider(rb, boxCollider);
        body.resetMassData();

    }

    public void addBox2DCollider(RigidBody2D rb, Box2DCollider boxCollider){
        Body body = rb.getRawBody();
        assert body !=null : "Raw body must not be null";

        PolygonShape shape = new PolygonShape();
        Vector2f halfSize = new Vector2f(boxCollider.getHalfSize()).mul(0.5f);
        Vector2f offset = boxCollider.getOffset();
        Vector2f origin = new Vector2f(boxCollider.getOrigin());
        shape.setAsBox(halfSize.x, halfSize.y, new Vec2(offset.x, offset.y), 0);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1;
        fixtureDef.friction = rb.getFriction();
        fixtureDef.userData = boxCollider.gameObject;
        fixtureDef.isSensor = rb.isSensor();
        body.createFixture(fixtureDef);
    }

    private void addCircleCollider(RigidBody2D rb, CircleCollider circleCollider) {
        Body body = rb.getRawBody();
        assert body !=null : "Raw body must not be null";

        CircleShape shape = new CircleShape();
        shape.setRadius(circleCollider.getRadius());
        shape.m_p.set(circleCollider.getOffset().x, circleCollider.getOffset().y);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1;
        fixtureDef.friction = rb.getFriction();
        fixtureDef.userData = circleCollider.gameObject;
        fixtureDef.isSensor = rb.isSensor();
        body.createFixture(fixtureDef);


    }
    public RaycastInfo raycast(GameObject requestingObject, Vector2f point1, Vector2f point2){
        RaycastInfo callback = new RaycastInfo(requestingObject);
        world.raycast(callback, new Vec2(point1.x, point1.y), new Vec2(point2.x, point2.y));
        return callback;
    }

    public int fixtureListSize(Body body){
        int size = 0;
        for (Fixture fixture = body.getFixtureList(); fixture != null; fixture = fixture.m_next){
            size++;
        }
        return size;
    }


    public void setIsSensor(RigidBody2D rb){
        Body body = rb.getRawBody();
        if (body != null){
            for (Fixture fixture = body.getFixtureList(); fixture != null; fixture = fixture.m_next){
                fixture.setSensor(true);
            }
        }
    }
    public void setNotSensor(RigidBody2D rb){
        Body body = rb.getRawBody();
        if (body != null){
            for (Fixture fixture = body.getFixtureList(); fixture != null; fixture = fixture.m_next){
                fixture.setSensor(false);
            }
        }
    }

    public boolean isLocked() {
        return world.isLocked();
    }

    public void resetPillboxCollider(RigidBody2D rb, PillboxCollider pb) {
        Body body = rb.getRawBody();
        if (body == null) return;

        int size = fixtureListSize(body);
        for (int i = 0; i < size; i++) {
            body.destroyFixture(body.getFixtureList());
        }

        addPillboxCollider(rb, pb);
        body.resetMassData();
    }
    public Vector2f getGravity() {
        return new Vector2f(world.getGravity().x, world.getGravity().y);
    }
    public void addPillboxCollider(RigidBody2D rb, PillboxCollider pb) {
        Body body = rb.getRawBody();
        assert body != null : "Raw body must not be null";

        addBox2DCollider(rb, pb.getBox());
        addCircleCollider(rb, pb.getTopCircle());
        addCircleCollider(rb, pb.getBottomCircle());
    }



}
