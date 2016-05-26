package com.dummyc0m.game.lwjglplay.engine

import org.lwjgl.BufferUtils
import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.glfw.GLFWFramebufferSizeCallback
import org.lwjgl.glfw.GLFWKeyCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryUtil

/**
 * Created by Dummyc0m on 5/16/16.
 */
class Window(val vsync: Boolean, val title: String, var width: Int, var height: Int) {
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

        val widthBuf = BufferUtils.createIntBuffer(1);
        val heightBuf = BufferUtils.createIntBuffer(1);
        glfwGetFramebufferSize(windowHandle, widthBuf, heightBuf);
        width = widthBuf.get();
        height = heightBuf.get();

        val keyCallback: GLFWKeyCallback = object : GLFWKeyCallback() {
            override fun invoke(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                    glfwSetWindowShouldClose(window, true);
                }
            }
        }
        glfwSetKeyCallback(windowHandle, keyCallback);


        //switch to using frame buffer size
        glfwSetFramebufferSizeCallback(windowHandle, object : GLFWFramebufferSizeCallback() {
            override fun invoke(p0: Long, p1: Int, p2: Int) {
                this@Window.width = p1;
                this@Window.height = p2;
                this@Window.resized = true;
            }
        })

//        glfwSetWindowSizeCallback(windowHandle, object : GLFWWindowSizeCallback() {
//            override fun invoke(window: Long, width: Int, height: Int) {
//                this@Window.width = width;
//                this@Window.height = height;
//                this@Window.resized = true;
//            }
//        });

        val vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        glfwSetWindowPos(
                windowHandle,
                (vidMode.width() - width) / 2,
                (vidMode.height() - height) / 2
        );

        glfwMakeContextCurrent(windowHandle);
        //vsync
        if (vsync) {
            glfwSwapInterval(1);
        }
        glfwShowWindow(windowHandle);

        GL.createCapabilities();

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        glEnable(GL_DEPTH_TEST);
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