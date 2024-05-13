package fr.mrqsdf.engine2d.components;

import java.util.Objects;

public class StateTrigger {

    public String state;
    public String triggerTypeName;

    private int id;

    public StateTrigger(){}
    public StateTrigger(AnimationType state, TriggerType triggerType, int id){
        this.state = state.getName();
        this.triggerTypeName = triggerType.getName();
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof StateTrigger other){
            return other.state.equals(this.state) && other.triggerTypeName.equals(this.triggerTypeName);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(state, triggerTypeName);
    }

}
