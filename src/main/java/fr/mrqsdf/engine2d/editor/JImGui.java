package fr.mrqsdf.engine2d.editor;

import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiStyleVar;
import imgui.type.ImString;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class JImGui {

    private static float defaultColumnWidth = 220f;
    public static void drawVec2Control(String label, Vector2f values){
        drawVec2Control(label, values, 0.0f,defaultColumnWidth);
    }
    public static void drawVec2Control(String label, Vector2f values, float resetValue){
        drawVec2Control(label, values, resetValue,defaultColumnWidth);
    }

    public static void drawVec2Control(String label, Vector2f values, float resetValue, float columnWidth){
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, columnWidth);
        ImGui.text(label);
        ImGui.nextColumn();

        ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing,0, 0);

        float lineHeight = ImGui.getFontSize() + ImGui.getStyle().getFramePaddingY() * 2.0f;
        Vector2f buttonSize = new Vector2f(lineHeight + 3.0f, lineHeight);
        float widthEach = (ImGui.calcItemWidth() - buttonSize.x * 2.0f) / 2.0f;
        ImGui.pushItemWidth(widthEach);
        ImGui.pushStyleColor(ImGuiCol.Button, 0.8f, 0.1f,0.15f,1);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.9f, 0.2f,0.2f,1);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.8f, 0.1f,0.15f,1);
        if (ImGui.button("X", buttonSize.x, buttonSize.y)){
            values.x = resetValue;
        }
        ImGui.popStyleColor(3);
        ImGui.sameLine();
        float[] vecValueX = {values.x};
        ImGui.dragFloat("##X", vecValueX, 0.1f);
        ImGui.popItemWidth();
        ImGui.sameLine();

        ImGui.pushItemWidth(widthEach);
        ImGui.pushStyleColor(ImGuiCol.Button, 0.2f, 0.7f,0.2f,1);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.3f, 0.8f,0.3f,1);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.2f, 0.7f,0.2f,1);
        if (ImGui.button("Y", buttonSize.x, buttonSize.y)){
            values.y = resetValue;
        }
        ImGui.popStyleColor(3);
        ImGui.sameLine();
        float[] vecValueY = {values.y};
        ImGui.dragFloat("##Y", vecValueY, 0.1f);
        ImGui.popItemWidth();
        ImGui.sameLine();
        ImGui.nextColumn();

        values.x = vecValueX[0];
        values.y = vecValueY[0];

        ImGui.popStyleVar();
        ImGui.columns(1);
        ImGui.popID();
    }

    public static float dragFloat(String label,float value){
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, defaultColumnWidth);
        ImGui.text(label);
        ImGui.nextColumn();

        float[] val = {value};
        ImGui.dragFloat("##dragFloat", val, 0.1f);

        ImGui.columns(1);
        ImGui.popID();

        return val[0];
    }

    public static int dragInt(String label,int value){
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, defaultColumnWidth);
        ImGui.text(label);
        ImGui.nextColumn();

        int[] val = {value};
        ImGui.dragInt("##dragFloat", val, 0.1f);

        ImGui.columns(1);
        ImGui.popID();

        return val[0];
    }

    public static boolean colorPicker4(String label, Vector4f color){
        boolean res = false;
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, defaultColumnWidth);
        ImGui.text(label);
        ImGui.nextColumn();

        float[] imColor = {color.x, color.y, color.z, color.w};
        if (ImGui.colorEdit4("##colorPicker", imColor)){
            color.set(imColor[0], imColor[1], imColor[2], imColor[3]);
            res = true;
        }

        ImGui.columns(1);
        ImGui.popID();

        return res;
    }

    public static String inputText(String label, String text){
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, defaultColumnWidth);
        ImGui.text(label);
        ImGui.nextColumn();

        ImString outString = new ImString(text, 256);
        if (ImGui.inputText("##" + label, outString)){
            ImGui.columns(1);
            ImGui.popID();

            return outString.get();
        }

        ImGui.columns(1);
        ImGui.popID();

        return text;
    }

}
