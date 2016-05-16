package com.dummyc0m.game.lwjglplay

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import org.lwjgl.glfw.Callbacks.*;
import org.lwjgl.glfw.GLFW.*;
import org.lwjgl.opengl.GL11.*;
import org.lwjgl.system.MemoryUtil.*;

/**
 * Created by Dummy on 5/15/16.
 */
fun main(args : Array<String>) {
    SharedLibraryLoader.load();
    Main().run();
}

class Main() {

    var window: Long = 0L;

    fun run() {
        println("Hello LWJGL " + Version.getVersion() + "!");

        try {
            init();
            loop();

            // Release window and window callbacks
            glfwFreeCallbacks(window);
            glfwDestroyWindow(window);
        } finally {
            // Terminate GLFW and release the GLFWerrorfun
            glfwTerminate();
            glfwSetErrorCallback(null).free();
        }
    }

    fun init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit()) {
            throw IllegalStateException("Unable to initialize GLFW");
        }

        // Configure our window
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); // the window will be resizable

        var WIDTH = 300;
        var HEIGHT = 300;

        // Create the window
        window = glfwCreateWindow(WIDTH, HEIGHT, "Hello World!", NULL, NULL);
        if (window == NULL) {
            throw RuntimeException("Failed to create the GLFW window");
        }

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        var keyCallback: GLFWKeyCallback = object : GLFWKeyCallback() {
            override fun invoke(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                    glfwSetWindowShouldClose(window, true); // We will detect this in our rendering loop
                }
            }
        }
        glfwSetKeyCallback(window, keyCallback);

        // Get the resolution of the primary monitor
        val vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        // Center our window
        glfwSetWindowPos(
                window,
                (vidmode.width() - WIDTH) / 2,
                (vidmode.height() - HEIGHT) / 2
        );

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);
    }

    fun loop() {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the ContextCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        // Set the clear color
        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( !glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            glfwSwapBuffers(window); // swap the color buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
        }
    }
}