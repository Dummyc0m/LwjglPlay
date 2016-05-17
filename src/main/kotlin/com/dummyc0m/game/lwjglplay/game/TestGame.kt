package com.dummyc0m.game.lwjglplay.game

import com.dummyc0m.game.lwjglplay.engine.IGameLogic
import com.dummyc0m.game.lwjglplay.engine.Window
import org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN
import org.lwjgl.glfw.GLFW.GLFW_KEY_UP
import org.lwjgl.opengl.GL11.glViewport

/**
 * Created by Dummyc0m on 5/16/16.
 */
class TestGame: IGameLogic {
    var direction = 0;

    var color = 0.0f;

    val renderer: Renderer;

    init {
        renderer = Renderer();
    }

    override fun init() {
        renderer.init();
    }

    override fun input(window: Window) {
        if (window.isKeyPressed(GLFW_KEY_UP)) {
            direction = 1;
        } else if ( window.isKeyPressed(GLFW_KEY_DOWN) ) {
            direction = -1;
        } else {
            direction = 0;
        }
    }

    override fun update(interval: Float) {
        color += direction * 0.01f;
        if (color > 1) {
            color = 1.0f;
        } else if ( color < 0 ) {
            color = 0.0f;
        }
    }

    override fun render(window: Window) {
        if (window.resized) {
            glViewport(0, 0, window.width, window.height);
            window.resized = false;
        }

        window.setClearColor(color, color, color, 0.0f);
        renderer.clear();
    }
}