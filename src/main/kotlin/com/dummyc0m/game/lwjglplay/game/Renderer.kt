package com.dummyc0m.game.lwjglplay.game

import com.dummyc0m.game.lwjglplay.engine.Entity
import com.dummyc0m.game.lwjglplay.engine.Transformation
import com.dummyc0m.game.lwjglplay.engine.Window
import com.dummyc0m.game.lwjglplay.engine.render.Camera
import com.dummyc0m.game.lwjglplay.engine.render.Shader
import com.dummyc0m.game.lwjglplay.engine.util.Util
import org.lwjgl.opengl.GL11.*

/**
 * Created by Dummyc0m on 5/16/16.
 */
class Renderer {
    private var shaderProgram: Shader = Shader();
    private val transformation: Transformation = Transformation();
    val camera: Camera = Camera();

    fun init(window: Window) {
        shaderProgram.init();
        shaderProgram.createVertexShader(Util.loadResource("/vertex.glsl"));
        shaderProgram.createFragmentShader(Util.loadResource("/fragment.glsl"));
        shaderProgram.link();

        transformation.setProjectionMatrix(FOV, window.width, window.height, Z_NEAR, Z_FAR)

        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("modelViewMatrix");
        shaderProgram.createUniform("texture_sampler");

        shaderProgram.createUniform("color");
        shaderProgram.createUniform("useColor");
    }

    fun render(window: Window, entities: Array<Entity>) {
        clear();

        shaderProgram.bind();

        if (window.resized) {
            glViewport(0, 0, window.width, window.height);
            window.resized = false;
            transformation.setProjectionMatrix(FOV, window.width, window.height, Z_NEAR, Z_FAR);
        }
        val viewMatrix = transformation.getViewMatrix(camera);

        shaderProgram.setUniform("projectionMatrix", transformation.projectionMatrix);
        shaderProgram.setUniform("texture_sampler", 0);

        for(entity in entities) {
            val modelViewMatrix = transformation.getModelViewMatrix(entity, viewMatrix);
            shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);

            shaderProgram.setUniform("color", entity.mesh.color);
            shaderProgram.setUniform("useColor", if (entity.mesh.texture == null) 1 else 0);

            entity.mesh.render();
        }

        shaderProgram.unbind();
    }

    fun clear() {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT);
    }

    fun cleanup() {
        shaderProgram.cleanup();
    }
}