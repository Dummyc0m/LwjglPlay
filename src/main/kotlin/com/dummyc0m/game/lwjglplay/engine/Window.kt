package com.dummyc0m.game.lwjglplay.engine

import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.glfw.GLFWKeyCallback
import org.lwjgl.glfw.GLFWWindowSizeCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*;
import org.lwjgl.system.MemoryUtil

/**
 * Created by Dummyc0m on 5/16/16.
 */
class Window(val vsync: Boolean, val title: String, var height: Int, var width: Int) {
    var windowHandle: Long = 0L;
    var resized = false;

    fun init() {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) {
            throw IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);

        // Create the window
        windowHandle = glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
        if (windowHandle == MemoryUtil.NULL) {
            throw RuntimeException("Failed to create the GLFW window");
        }

        val keyCallback: GLFWKeyCallback = object : GLFWKeyCallback() {
            override fun invoke(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                    glfwSetWindowShouldClose(window, true);
                }
            }
        }
        glfwSetKeyCallback(windowHandle, keyCallback);

        glfwSetWindowSizeCallback(windowHandle, object: GLFWWindowSizeCallback() {
            override fun invoke(window: Long, width: Int, height: Int) {
                this@Window.width = width;
                this@Window.height = height;
                this@Window.resized = true;
            }
        });

        val vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        glfwSetWindowPos(
                windowHandle,
                (vidmode.width() - width) / 2,
                (vidmode.height() - height) / 2
        );

        glfwMakeContextCurrent(windowHandle);
        //vsync
        if(vsync) {
            glfwSwapInterval(1);
        }
        glfwShowWindow(windowHandle);


        GL.createCapabilities();

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    }

    fun destroy() {
        try {
            Callbacks.glfwFreeCallbacks(windowHandle);
            glfwDestroyWindow(windowHandle);
        } finally {
            glfwTerminate();
            glfwSetErrorCallback(null).free();
        }
    }

    fun update() {
        glfwSwapBuffers(windowHandle);
        glfwPollEvents();
    }

    fun setClearColor(r: Float, g: Float, b: Float, alpha: Float) {
        glClearColor(r, g, b, alpha);
    }

    fun windowShouldClose(): Boolean {
        return glfwWindowShouldClose(windowHandle);
    }

    fun isKeyPressed(keyCode: Int): Boolean {
        return glfwGetKey(windowHandle, keyCode) == GLFW_PRESS;
    }
}