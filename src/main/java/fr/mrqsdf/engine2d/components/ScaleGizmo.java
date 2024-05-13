package fr.mrqsdf.engine2d.components;

import fr.mrqsdf.engine2d.editor.PropertiesWindows;
import fr.mrqsdf.engine2d.jade.MouseListener;

public class ScaleGizmo extends Gizmo{

    public ScaleGizmo(Sprite scaleSprite, PropertiesWindows propertiesWindows){
        super(scaleSprite, propertiesWindows);
    }

    @Override
    public void editorUpdate(float dt) {
        if (activeGameObject != null) {
            if (xAxisActive && !yAxisActive) {
                activeGameObject.transform.scale.x -= MouseListener.getWorldDx();
            } else if (yAxisActive) {
                activeGameObject.transform.scale.y -= MouseListener.getWorldDy();
            }
        }

        super.editorUpdate(dt);
    }

}
