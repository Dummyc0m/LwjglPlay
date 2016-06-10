package com.dummyc0m.game.lwjglplay.game

import com.dummyc0m.game.lwjglplay.engine.Entity
import com.dummyc0m.game.lwjglplay.engine.Transformation
import com.dummyc0m.game.lwjglplay.engine.Window
import com.dummyc0m.game.lwjglplay.engine.render.Camera
import com.dummyc0m.game.lwjglplay.engine.render.PointLight
import com.dummyc0m.game.lwjglplay.engine.render.Shader
import com.dummyc0m.game.lwjglplay.engine.util.Util
import org.joml.Vector3f
import org.joml.Vector4f
import org.lwjgl.opengl.GL11.*

/**
 * Created by Dummyc0m on 5/16/16.
 */
class Renderer {
    private var shaderProgram: Shader = Shader();
    private val transformation: Transformation = Transformation();
    var specularPower = 10f

    fun init(window: Window) {
        shaderProgram.init();
        shaderProgram.createVertexShader(Util.loadResource("/vertex.glsl"));
        shaderProgram.createFragmentShader(Util.loadResource("/fragment.glsl"));
        shaderProgram.link();

        transformation.setProjectionMatrix(FOV, window.width, window.height, Z_NEAR, Z_FAR)

        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("modelViewMatrix");
        shaderProgram.createUniform("texture_sampler");
        // Create uniform for material
        shaderProgram.createMaterialUniform("material")
        // Create lighting related uniforms
        shaderProgram.createUniform("camera_pos")
        shaderProgram.createUniform("specularPower")
        shaderProgram.createUniform("ambientLight")
        shaderProgram.createPointLightUniform("pointLight")
    }

    fun render(window: Window, entities: Array<Entity>, camera: Camera, ambientLight: Vector3f, pointLight: PointLight) {
        clear();

        shaderProgram.bind();

        if (window.resized) {
            glViewport(0, 0, window.width, window.height);
            window.resized = false;
            transformation.setProjectionMatrix(FOV, window.width, window.height, Z_NEAR, Z_FAR);
        }
        val viewMatrix = transformation.getViewMatrix(camera);

        shaderProgram.setUniform("camera_pos", camera.position)
        shaderProgram.setUniform("ambientLight", ambientLight)
        shaderProgram.setUniform("specularPower", specularPower)


        val currPointLight = PointLight(pointLight)
        val lightPos = currPointLight.position
        val aux = Vector4f(lightPos, 1f)
        aux.mul(viewMatrix)
        lightPos.x = aux.x
        lightPos.y = aux.y
        lightPos.z = aux.z
        shaderProgram.setUniform("pointLight", currPointLight)

        shaderProgram.setUniform("projectionMatrix", transformation.projectionMatrix);
        shaderProgram.setUniform("texture_sampler", 0);

        for(entity in entities) {
            val modelViewMatrix = transformation.getModelViewMatrix(entity, viewMatrix);
            shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
            shaderProgram.setUniform("material", entity.mesh.material);

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