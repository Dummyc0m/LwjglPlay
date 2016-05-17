package com.dummyc0m.game.lwjglplay.game

import org.lwjgl.opengl.GL11.*;

/**
 * Created by Dummyc0m on 5/16/16.
 */
class Renderer {
    fun init() {

    }

    fun clear() {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT);
    }
}