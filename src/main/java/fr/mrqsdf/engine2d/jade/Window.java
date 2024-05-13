package fr.mrqsdf.engine2d.jade;

import fr.mrqsdf.engine2d.observers.EventSystem;
import fr.mrqsdf.engine2d.observers.Observer;
import fr.mrqsdf.engine2d.observers.events.Event;
import fr.mrqsdf.engine2d.observers.events.EventType;
import fr.mrqsdf.engine2d.physics2d.Physics2D;
import fr.mrqsdf.engine2d.renderer.*;
import fr.mrqsdf.engine2d.scenes.LevelEditorSceneInitializer;
import fr.mrqsdf.engine2d.scenes.Scene;
import fr.mrqsdf.engine2d.scenes.SceneInitializer;
import fr.mrqsdf.engine2d.utils.*;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;
import org.lwjgl.opengl.GL;

import java.lang.reflect.InvocationTargetException;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window implements Observer {

    private final ImageParser resource_01 = ImageParser.load_image("assets/images/engine/levels.png");
    private static SceneInitializer editorScene = new LevelEditorSceneInitializer();
    private static SceneInitializer gameScene = editorScene;
    private int width;
    private int height;
    private String title;
    private long glfwWindow;
    private ImGuiLayer imGuiLayer;
    private Framebuffer frameBuffer;
    public PickingTexture pickingTexture;
    public boolean runtimePlaying = false;

    private static Window window = null;

    private long audioContext;
    private long audioDevice;

    private static Scene currentScene;

    public static final boolean RELEASE_BUILD = false;
    public static final boolean DEBUG_BUILD = false;

    private int fps = 0;

    private long lastSecondTime = 0;

    private Window(){
        width = EngineSettings.WINDOW_WIDTH;
        height = EngineSettings.WINDOW_HEIGHT;
        title = "Jade";
        EventSystem.addObserver(this);
    }

    public static void changeScene(SceneInitializer sceneInitializer, int levelID){
        if (currentScene != null){
            currentScene.destroy();
        }
        if (!RELEASE_BUILD) {
            getImGuiLayer().getPropertiesWindows().setActiveGameObject(null);
        }

        currentScene = new Scene(sceneInitializer, levelID);

        currentScene.load();
        currentScene.init();
        currentScene.start();


    }
    public static Window get(){
        if (window == null){
            window = new Window();
        }
        return window;
    }

    public static Physics2D getPhysics(){
        return currentScene.getPhysics();
    }
    public static Scene getScene(){
        return currentScene;
    }
    public static void setGameScene(SceneInitializer sceneInitializer){
        gameScene = sceneInitializer;
    }

    public void run(){
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");
        System.out.println("Running window");
        init();
        loop();

        EventSystem.notify(null, new Event(EventType.SAVE_LEVEL));
        Levels.saveAllLevels();
        Prefabs.save();
        alcDestroyContext(audioContext);
        alcCloseDevice(audioDevice);

        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void init(){
        GLFWErrorCallback.createPrint(System.err).set();
        lastSecondTime = System.currentTimeMillis();
        if (!glfwInit()){
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, 0, 0);
        if (glfwWindow == NULL){
            throw new IllegalStateException("Failed to create GLFW window");
        }
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);
        glfwSetWindowSizeCallback(glfwWindow, (w, newWidth, newHeight) ->{
            Window.setWidth(newWidth);
            Window.setHeight(newHeight);
        });

        glfwMakeContextCurrent(glfwWindow);

        glfwSwapInterval(1);

        glfwShowWindow(glfwWindow);
        GLFWImage image = GLFWImage.malloc();
        GLFWImage.Buffer imagebf = GLFWImage.malloc(1);
        image.set(resource_01.get_width(), resource_01.get_heigh(), resource_01.get_image());
        imagebf.put(0, image);
        glfwSetWindowIcon(glfwWindow, imagebf);
        String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
        audioDevice = alcOpenDevice(defaultDeviceName);

        int[] attributes = {0};
        audioContext = alcCreateContext(audioDevice, attributes);
        alcMakeContextCurrent(audioContext);

        ALCCapabilities alcCapabilities = ALC.createCapabilities(audioDevice);
        ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);

        if (!alCapabilities.OpenAL10) {
            throw new IllegalStateException("audio library not supported");
        }


        GL.createCapabilities();

        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        this.frameBuffer = new Framebuffer(this.width*2, this.height*2);
        this.pickingTexture = new PickingTexture(this.width*2, this.height*2);
        glViewport(0, 0, this.width*2, this.height*2);
        Levels.loadLevels();
        if (Levels.levels.isEmpty()) {
            new Levels("New Level", 0);
        }
        new Folder("assets");
        if (RELEASE_BUILD) {
            runtimePlaying = true;
            try {
                gameScene = gameScene.getClass().getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            Window.changeScene(gameScene, 0); //todo change this to a main game scene
        } else {
            this.imGuiLayer = new ImGuiLayer(glfwWindow, pickingTexture);
            this.imGuiLayer.initImGui();
            try {
                editorScene = editorScene.getClass().getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            Window.changeScene(editorScene, 0);
        }
    }

    public void loop(){
        float beginTime = (float) glfwGetTime();
        float endTime;
        float dt = -1.0f;
        float saveTime = 0.0f;
        float maxSaveTime = EngineSettings.TIME_SAVE * 60;

        Shader defaultShader = AssetPool.getShader("assets/shaders/default.glsl");
        Shader pickingShader = AssetPool.getShader("assets/shaders/pickingShader.glsl");
        Prefabs.load();
        while (!glfwWindowShouldClose(glfwWindow)){

            glfwPollEvents();

            glDisable(GL_BLEND);
            pickingTexture.enableWriting();

            glViewport(0, 0, this.width*2, this.height*2);
            glClearColor(0,0,0,0);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            Renderer.bindShader(pickingShader);
            currentScene.render();


            pickingTexture.disableWriting();
            glEnable(GL_BLEND);

            DebugDraw.beginFrame();
            frameBuffer.bind();
            glClearColor(1,1,1,1);
            glClear(GL_COLOR_BUFFER_BIT);

            if (dt >= 0) {
                Renderer.bindShader(defaultShader);
                if (runtimePlaying) {
                    currentScene.update(dt);
                } else {
                    currentScene.editorUpdate(dt);
                }
                currentScene.render();
                DebugDraw.draw();
            }
            if (RELEASE_BUILD) {
                // NOTE: This is the most complicated piece for release builds. In release builds
                //       we want to just blit the framebuffer to the main window so we can see the game
                //
                //       In non-release builds, we usually draw the framebuffer to an ImGui component as an image.
                glBindFramebuffer(GL_READ_FRAMEBUFFER, frameBuffer.getFboID());
                glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0);
                glBlitFramebuffer(0, 0, frameBuffer.width, frameBuffer.height, 0, 0, this.width, this.height,
                        GL_COLOR_BUFFER_BIT, GL_NEAREST);
            } else {
                this.imGuiLayer.update(dt, currentScene);
            }
            frameBuffer.unbind();


            MouseListener.endFrame();
            KeyListener.endFrame();

            glfwSwapBuffers(glfwWindow);

            endTime = (float) glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;
            fps = (int)(1.0f/dt);
            long timer = System.currentTimeMillis();
            if (timer - lastSecondTime > 1000){
                lastSecondTime = timer;
                glfwSetWindowTitle(glfwWindow, title + " | " + fps + " fps");
            }
            saveTime += dt;
            if (saveTime >= maxSaveTime){
                currentScene.save();
                saveTime = 0.0f;
            }
        }

    }


    public static int getWidth(){
        return get().width;
    }
    public static int getHeight(){
        return get().height;
    }

    public static void setWidth(int newWidth){
        get().width = newWidth;
    }
    public static void setHeight(int newHeight){
        get().height = newHeight;
    }

    public static Framebuffer getFramebuffer(){
        return get().frameBuffer;
    }

    public static float getTargetAspectRatio(){
        return EngineSettings.WINDOW_WIDTH / (float) EngineSettings.WINDOW_HEIGHT;
    }

    public static ImGuiLayer getImGuiLayer(){
        return get().imGuiLayer;
    }

    @Override
    public void onNotify(GameObject object, Event event) {
        try {
            gameScene = gameScene.getClass().getDeclaredConstructor().newInstance();
            editorScene = editorScene.getClass().getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        switch (event.type) {
            case GAME_ENGINE_START_PLAY:
                this.runtimePlaying = true;
                currentScene.save();
                Window.changeScene(gameScene, currentScene.getLevelID());
                break;
            case GAME_ENGINE_STOP_PLAY:
                this.runtimePlaying = false;
                Window.changeScene(editorScene, currentScene.getLevelID());
                break;
            case LOAD_LEVEL:
                Window.changeScene(editorScene, currentScene.getLevelID());
                currentScene.render();
                break;
            case SAVE_LEVEL:
                currentScene.save();
                break;
        }
    }


}
