package fr.mrqsdf.engine2d.components;

import imgui.ImGui;
import imgui.type.ImBoolean;
import imgui.type.ImString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StateMachine extends Component {

    public HashMap<StateTrigger, String> stateTransfers = new HashMap<>();
    private List<AnimationState> states = new ArrayList<>();
    private transient AnimationState currentState = null;
    private String  defaultStateTypeName = "";

    public void refreshTextures(){
        for (AnimationState state : states){
            state.refreshTextures();
        }
    }

    public void addStateTrigger(AnimationType from, AnimationType to, TriggerType triggerType, int id){
        StateTrigger trigger = new StateTrigger(from, triggerType, id);
        stateTransfers.put(trigger, to.getName());
    }
    public void addStateTrigger(StateTrigger stateTrigger, AnimationType to){
        stateTransfers.put(stateTrigger, to.getName());
    }
    public void addState(AnimationState state){
        states.add(state);
        /*if (currentState == null){
            currentState = state;
        }*/
    }
    public void setDefaultState(AnimationType animationType){
        for (AnimationState state : states){
            if (state.animationTypeName.equals(animationType.getName())){
                defaultStateTypeName = animationType.getName();
                if (currentState == null){
                    currentState = state;
                    return;
                }
            }
        }
        System.out.println("Unable to find state: " + defaultStateTypeName);
    }
    public void setDefaultState(TriggerType triggerType){
        for (AnimationState state : states){
            if (state.animationTypeName.equals(triggerType.getName())){
                defaultStateTypeName = triggerType.getName();
                if (currentState == null){
                    currentState = state;
                    return;
                }
            }
        }
        System.out.println("Unable to find state: " + defaultStateTypeName);
    }
    public void setStates(List<AnimationState> states){
        this.states = states;
    }
    public void trigger(TriggerType triggerType){
        for (StateTrigger state : stateTransfers.keySet()){
            if (state.state.equals(currentState.animationTypeName) && state.triggerTypeName.equals(triggerType.getName())){
                if (stateTransfers.get(state) != null){
                    int newStateIndex = -1;
                    int index = 0;
                    for (AnimationState s : states){
                        if (s.animationTypeName.equals(stateTransfers.get(state))){
                            newStateIndex = index;
                            break;
                        }
                        index++;
                    }
                    SpriteRenderer sprite = gameObject.getComponent(SpriteRenderer.class);
                    sprite.setSprite(currentState.animationFrames.get(0).sprite);
                    if (newStateIndex > -1){
                        currentState = states.get(newStateIndex);
                        currentState.currentSprite = 0;
                    }
                }
                return;
            }
        }
        System.out.println("Unable to find trigger: " + triggerType);
    }
    private void trigger(String triggerType){
        for (StateTrigger state : stateTransfers.keySet()){
            if (state.state.equals(currentState.animationTypeName) && state.triggerTypeName.equals(triggerType)){
                if (stateTransfers.get(state) != null){
                    int newStateIndex = -1;
                    int index = 0;
                    for (AnimationState s : states){
                        if (s.animationTypeName.equals(stateTransfers.get(state))){
                            newStateIndex = index;
                            break;
                        }
                        index++;
                    }
                    SpriteRenderer sprite = gameObject.getComponent(SpriteRenderer.class);
                    sprite.setSprite(currentState.animationFrames.get(0).sprite);
                    if (newStateIndex > -1){
                        currentState = states.get(newStateIndex);
                        currentState.currentSprite = 0;

                    }
                }
                return;
            }
        }
        System.out.println("Unable to find trigger: " + triggerType);
    }

    @Override
    public void start(){
        for (AnimationState state : states){
            if (state.animationTypeName.equals(defaultStateTypeName)){
                currentState = state;
                break;
            }
        }
    }

    @Override
    public void update(float dt){
        if (currentState != null){
            currentState.update(dt);
            SpriteRenderer sprite = gameObject.getComponent(SpriteRenderer.class);
            if (sprite != null){
                sprite.setSprite(currentState.getCurrentSprite());
            }
            if (currentState.animationEnd){
                currentState.animationEnd = false;
                trigger(defaultStateTypeName);
            }
        }
    }

    @Override
    public void editorUpdate(float dt){
        if (currentState != null){
            currentState.update(dt);
            SpriteRenderer sprite = gameObject.getComponent(SpriteRenderer.class);
            if (sprite != null){
                sprite.setSprite(currentState.getCurrentSprite());
            }
            if (currentState.animationEnd){
                currentState.animationEnd = false;
                trigger(defaultStateTypeName);
            }
        }
    }

    @Override
    public void imgui(){
        int index = 0;
        for (AnimationState state : states){
            ImString title = new ImString(state.title);
            ImGui.inputText("State : ", title);
            state.title = title.get();

            ImBoolean doesLoop = new ImBoolean(state.doesLoop);
            ImGui.checkbox("Does Loop", doesLoop);
            state.setLoop(doesLoop.get());
            for (Frame frame : state.animationFrames){
                float[] tmp = new float[1];
                tmp[0] = frame.frameTime;
                ImGui.dragFloat("Frame(" + index + ") Time : ", tmp, 0.01f);
                frame.frameTime = tmp[0];
                index++;
            }

        }
    }


}
