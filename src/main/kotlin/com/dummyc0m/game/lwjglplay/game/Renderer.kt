package com.dummyc0m.game.lwjglplay.game

import com.dummyc0m.game.lwjglplay.engine.*
import com.dummyc0m.game.lwjglplay.engine.util.Shader
import org.lwjgl.opengl.GL11.*

/**
 * Created by Dummyc0m on 5/16/16.
 */
class Renderer {
    private var shaderProgram: Shader = Shader();
    private val transformation: Transformation = Transformation();

    fun init(window: Window) {
        shaderProgram.init();
        shaderProgram.createVertexShader(Shader.loadResource("/vertex.glsl"));
        shaderProgram.createFragmentShader(Shader.loadResource("/fragment.glsl"));
        shaderProgram.link();

        transformation.setProjectionMatrix(FOV, window.width, window.height, Z_NEAR, Z_FAR);

        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("worldMatrix");
    }

    fun render(window: Window, entities: Array<Entity>) {
        clear();

        if (window.resized) {
            glViewport(0, 0, window.width, window.height);
            window.resized = false;
            transformation.setProjectionMatrix(FOV, window.width, window.height, Z_NEAR, Z_FAR);
        }

        shaderProgram.bind();
        shaderProgram.setUniform("projectionMatrix", transformation.projectionMatrix);

        for(entity in entities) {
            val worldMatrix =
            transformation.getWorldMatrix(
                    entity.translation,
                    entity.rotation,
                    entity.scale);
            shaderProgram.setUniform("worldMatrix", worldMatrix);

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