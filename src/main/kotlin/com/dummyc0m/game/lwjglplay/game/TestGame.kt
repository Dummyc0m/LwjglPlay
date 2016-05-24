package com.dummyc0m.game.lwjglplay.game

import com.dummyc0m.game.lwjglplay.engine.Entity
import com.dummyc0m.game.lwjglplay.engine.IGameLogic
import com.dummyc0m.game.lwjglplay.engine.Window
import com.dummyc0m.game.lwjglplay.engine.util.Mesh
import org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN
import org.lwjgl.glfw.GLFW.GLFW_KEY_UP

/**
 * Created by Dummyc0m on 5/16/16.
 */
class TestGame : IGameLogic {
    private var direction = 0;
    private var color = 0.0f;
    private val renderer: Renderer;
    private lateinit var mesh: Mesh;

    init {
        renderer = Renderer();
    }

    override fun init(window: Window) {
        renderer.init(window);
        val positions = floatArrayOf(
            -0.5f, 0.5f, -1.05f,
            -0.5f, -0.5f, -1.05f,
            0.5f, -0.5f, -1.05f,
            0.5f, 0.5f, -1.05f);
        val colors = floatArrayOf(
            0.5f, 0.0f, 0.0f,
            0.0f, 0.5f, 0.0f,
            0.0f, 0.0f, 0.5f,
            0.0f, 0.5f, 0.5f);
        val indices = intArrayOf(0, 1, 3, 3, 1, 2);
        mesh = Mesh(positions, colors, indices);
    }

    override fun input(window: Window) {
        if (window.isKeyPressed(GLFW_KEY_UP)) {
            direction = 1;
        } else if (window.isKeyPressed(GLFW_KEY_DOWN)) {
            direction = -1;
        } else {
            direction = 0;
        }
    }

    override fun update(interval: Float) {
        color += direction * 0.01f;
        if (color > 1) {
            color = 1.0f;
        } else if (color < 0) {
            color = 0.0f;
        }
    }

    override fun render(window: Window) {
        window.setClearColor(color, color, color, 0.0f);
        renderer.render(window, Array(1, {Entity(mesh)}));
    }

    override fun cleanup() {
        renderer.cleanup();
        mesh.cleanup();
    }

}