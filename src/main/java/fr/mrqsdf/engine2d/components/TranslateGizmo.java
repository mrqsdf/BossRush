package fr.mrqsdf.engine2d.components;

import fr.mrqsdf.engine2d.editor.PropertiesWindows;
import fr.mrqsdf.engine2d.jade.MouseListener;

public class TranslateGizmo extends Gizmo{

    public TranslateGizmo(Sprite arrowSprite, PropertiesWindows propertiesWindows){
        super(arrowSprite, propertiesWindows);
    }

    @Override
    public void editorUpdate(float dt) {
        if (activeGameObject != null) {
            if (xAxisActive && !yAxisActive) {
                activeGameObject.transform.position.x -= MouseListener.getWorldDx();
            } else if (yAxisActive) {
                activeGameObject.transform.position.y -= MouseListener.getWorldDy();
            }
        }

        super.editorUpdate(dt);
    }

}
