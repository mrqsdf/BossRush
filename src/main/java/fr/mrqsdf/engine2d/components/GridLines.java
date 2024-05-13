package fr.mrqsdf.engine2d.components;

import fr.mrqsdf.engine2d.jade.Camera;
import fr.mrqsdf.engine2d.jade.Window;
import fr.mrqsdf.engine2d.renderer.DebugDraw;
import fr.mrqsdf.engine2d.utils.EngineSettings;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class GridLines extends Component{

    @Override
    public void editorUpdate(float dt) {
        Camera camera = Window.getScene().camera();
        Vector2f cameraPos = camera.position;
        Vector2f projectionSize = camera.getProjectionSize();

        float firstX = ((int)Math.floor(cameraPos.x / EngineSettings.GRID_WIDTH) - 1) * EngineSettings.GRID_HEIGHT;
        float firstY = ((int)Math.floor(cameraPos.y / EngineSettings.GRID_HEIGHT) - 1) * EngineSettings.GRID_HEIGHT;

        int numVtLines = (int)(projectionSize.x * camera.getZoom() / EngineSettings.GRID_WIDTH) + 2;
        int numHzLines = (int)(projectionSize.y * camera.getZoom() / EngineSettings.GRID_HEIGHT) + 2;


        float width = (int)(projectionSize.x * camera.getZoom())+ EngineSettings.GRID_WIDTH * 2;
        float height = (int)(projectionSize.y * camera.getZoom()) + EngineSettings.GRID_HEIGHT * 2;

        int maxLines = Math.max(numVtLines, numHzLines);
        Vector3f color = new Vector3f(0.2f, 0.2f, 0.2f);
        for (int i=0; i < maxLines; i++) {
            float x = firstX + (EngineSettings.GRID_WIDTH * i);
            float y = firstY + (EngineSettings.GRID_HEIGHT * i);

            if (i < numVtLines) {
                DebugDraw.addLine2D(new Vector2f(x, firstY), new Vector2f(x, firstY + height), color);
            }

            if (i < numHzLines) {
                DebugDraw.addLine2D(new Vector2f(firstX, y), new Vector2f(firstX + width, y), color);
            }
        }
    }

}
