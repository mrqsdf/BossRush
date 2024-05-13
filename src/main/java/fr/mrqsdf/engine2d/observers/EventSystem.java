package fr.mrqsdf.engine2d.observers;

import fr.mrqsdf.engine2d.jade.GameObject;
import fr.mrqsdf.engine2d.observers.events.Event;

import java.util.ArrayList;
import java.util.List;

public class EventSystem {

    private static List<Observer> observers = new ArrayList<>();

    public static void addObserver(Observer observer) {
        observers.add(observer);
    }

    public static void notify(GameObject object, Event event) {
        for (Observer observer : observers) {
            observer.onNotify(object, event);
        }
    }

}
