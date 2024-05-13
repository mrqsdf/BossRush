package fr.mrqsdf.engine2d.observers;

import fr.mrqsdf.engine2d.jade.GameObject;
import fr.mrqsdf.engine2d.observers.events.Event;

public interface Observer {

    void onNotify(GameObject object, Event event);

}
