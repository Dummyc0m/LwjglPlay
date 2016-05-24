package com.dummyc0m.game.lwjglplay.game

import com.dummyc0m.game.lwjglplay.engine.Window
import com.dummyc0m.game.lwjglplay.engine.util.Mesh
import com.dummyc0m.game.lwjglplay.engine.util.Shader
import org.joml.Matrix4f
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL20.glDisableVertexAttribArray
import org.lwjgl.opengl.GL20.glEnableVertexAttribArray
import org.lwjgl.opengl.GL30.glBindVertexArray

/**
 * Created by Dummyc0m on 5/16/16.
 */
/**
 * Field of View in Radians
 */
val FOV = Math.toRadians(60.0).toFloat();

val Z_NEAR = 0.01f;

val Z_FAR = 1000f;
class Renderer {
    private var shaderProgram: Shader = Shader();
    private lateinit var projectionMatrix: Matrix4f;

    fun init(window: Window) {
        shaderProgram.init();
        shaderProgram.createVertexShader(Shader.loadResource("/vertex.glsl"));
        shaderProgram.createFragmentShader(Shader.loadResource("/fragment.glsl"));
        shaderProgram.link();


        val aspectRatio = window.width.toFloat() / window.height;
        projectionMatrix = Matrix4f().perspective(FOV, aspectRatio,
                Z_NEAR, Z_FAR);

        shaderProgram.createUniform("projectionMatrix");
    }

    fun render(window: Window, mesh: Mesh) {
        clear();

        if (window.resized) {
            glViewport(0, 0, window.width, window.height);
            window.resized = false;
            val aspectRatio = window.width.toFloat() / window.height;
            projectionMatrix = Matrix4f().perspective(FOV, aspectRatio,
                    Z_NEAR, Z_FAR);
        }

        shaderProgram.bind();
        shaderProgram.setUniform("projectionMatrix", projectionMatrix);

        glBindVertexArray(mesh.vaoId);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glDrawElements(GL_TRIANGLES, mesh.vertexCount, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glBindVertexArray(0);

        shaderProgram.unbind();
    }

    fun clear() {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT);
    }

    fun cleanup() {
        shaderProgram.cleanup();
    }
}