package com.dummyc0m.game.lwjglplay.game

import com.dummyc0m.game.lwjglplay.engine.*
import com.dummyc0m.game.lwjglplay.engine.Shader
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
        shaderProgram.createVertexShader(Shader.loadResource("/vertex.glsl"));
        shaderProgram.createFragmentShader(Shader.loadResource("/fragment.glsl"));
        shaderProgram.link();

        transformation.setProjectionMatrix(FOV, window.width, window.height, Z_NEAR, Z_FAR);

        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("modelViewMatrix");
        shaderProgram.createUniform("texture_sampler");
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