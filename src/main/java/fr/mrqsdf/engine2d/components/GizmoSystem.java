package fr.mrqsdf.engine2d.components;

import fr.mrqsdf.engine2d.jade.KeyListener;
import fr.mrqsdf.engine2d.jade.Window;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_T;


public class GizmoSystem extends Component{

    private SpriteSheet gizmos;
    private int usingGizmo = 0;

    public GizmoSystem(SpriteSheet gizmos){
        this.gizmos = gizmos;
    }

    @Override
    public void start(){
        gameObject.addComponent(new TranslateGizmo(gizmos.getSprite(1),
                Window.get().getImGuiLayer().getPropertiesWindows()));
        gameObject.addComponent(new ScaleGizmo(gizmos.getSprite(2),
                Window.get().getImGuiLayer().getPropertiesWindows()));
    }

    @Override
    public void editorUpdate(float dt){
        if (usingGizmo == 0){
            gameObject.getComponent(TranslateGizmo.class).setUsing();
            gameObject.getComponent(ScaleGizmo.class).setNotUsing();
        } else if (usingGizmo == 1){
            gameObject.getComponent(TranslateGizmo.class).setNotUsing();
            gameObject.getComponent(ScaleGizmo.class).setUsing();
        }

        if (KeyListener.isKeyPressed(GLFW_KEY_T)){
            usingGizmo = 0;
        } else if (KeyListener.isKeyPressed(GLFW_KEY_S)){
            usingGizmo = 1;
        }
    }

}
