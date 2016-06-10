package com.dummyc0m.game.lwjglplay.game

import com.dummyc0m.game.lwjglplay.engine.Entity
import com.dummyc0m.game.lwjglplay.engine.IGameLogic
import com.dummyc0m.game.lwjglplay.engine.MouseInput
import com.dummyc0m.game.lwjglplay.engine.Window
import com.dummyc0m.game.lwjglplay.engine.render.Camera
import com.dummyc0m.game.lwjglplay.engine.render.Mesh
import com.dummyc0m.game.lwjglplay.engine.render.PointLight
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW.*

/**
 * Created by Dummyc0m on 5/16/16.
 */
class TestGame : IGameLogic {
    private val renderer: Renderer;
    private val cameraInc: Vector3f;
    val camera: Camera = Camera();
    private lateinit var entities: Array<Entity>;

    private var ambientLight: Vector3f

    private var pointLight: PointLight

    init {
        renderer = Renderer();
        cameraInc = Vector3f();
        ambientLight = Vector3f(0.3f, 0.3f, 0.3f);
        val lightColor = Vector3f(1f, 1f, 1f)
        val lightPosition = Vector3f(0f, 2f, 1f)
        val lightIntensity = 5.0f
        pointLight = PointLight(lightColor, lightPosition, lightIntensity, PointLight.Attenuation(0.0f, 0.0f, 1.0f))
    }

    override fun init(window: Window) {
        renderer.init(window);

        val mesh = Mesh.loadFromObj("/bunny.obj");
        //mesh.texture = Texture("/grassblock.png");
        val entity = Entity(mesh);
        entity.scale = 0.5f
        entity.setPosition(0f, 0f, -2f);
        entities = Array(1, {entity});
    }

    override fun input(window: Window, mouseInput: MouseInput) {
        cameraInc.set(0f, 0f, 0f);
        if (window.isKeyPressed(GLFW_KEY_W)) {
            cameraInc.z = -1f;
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            cameraInc.z = 1f;
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            cameraInc.x = -1f;
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            cameraInc.x = 1f;
        }
        if (window.isKeyPressed(GLFW_KEY_Z)) {
            cameraInc.y = -1f;
        } else if (window.isKeyPressed(GLFW_KEY_X)) {
            cameraInc.y = 1f;
        }
    }

    override fun update(interval: Float, mouseInput: MouseInput) {
        // Update camera position
        camera.movePos(cameraInc.x * CAMERA_POS_STEP,
                cameraInc.y * CAMERA_POS_STEP,
                cameraInc.z * CAMERA_POS_STEP);

        // Update camera based on mouse
        if (mouseInput.rightButtonPressed) {
            val rotVec = mouseInput.displayVector;
            camera.moveRot(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0f);
        }
    }

    override fun render(window: Window) {
        renderer.render(window, entities, camera, ambientLight, pointLight);
    }

    override fun cleanup() {
        renderer.cleanup();
        for(entity in entities) {
            entity.mesh.cleanup();
        }
    }
}