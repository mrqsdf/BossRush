package fr.mrqsdf.engine2d.utils;

import fr.mrqsdf.engine2d.components.Component;
import fr.mrqsdf.engine2d.physics2d.components.Box2DCollider;
import fr.mrqsdf.engine2d.physics2d.components.CircleCollider;
import fr.mrqsdf.engine2d.physics2d.components.RigidBody2D;

import java.util.ArrayList;
import java.util.List;

public class ComponentAdder {

    public static List<ComponentAdder> componentAdderList = new ArrayList<>();
    public String name;
    public Component component;

    public ComponentAdder(String name, Component component){
        this.name = name;
        this.component = component;
        componentAdderList.add(this);
    }

    static {
        new ComponentAdder("Rigide Body 2D", new RigidBody2D());
        new ComponentAdder("Circle Collider", new CircleCollider());
        new ComponentAdder("Box Collider", new Box2DCollider());
    }

}
