package com.dummyc0m.game.lwjglplay.engine

import com.dummyc0m.game.lwjglplay.engine.render.Camera
import org.joml.Matrix4f
import org.joml.Vector3f

/**
 * Created by Dummyc0m on 5/24/16.
 */
class Transformation {
    val projectionMatrix: Matrix4f;
    val viewMatrix: Matrix4f;
    val modelViewMatrix: Matrix4f;

    init {
        projectionMatrix = Matrix4f();
        modelViewMatrix = Matrix4f();
        viewMatrix = Matrix4f();
    }

    fun setProjectionMatrix(fov: Float, width: Int, height: Int, zNear: Float, zFar: Float): Matrix4f {
        val aspectRatio = width.toFloat() / height;
        projectionMatrix.identity()
                .perspective(fov, aspectRatio, zNear, zFar);
        return projectionMatrix;
    }

    fun getModelViewMatrix(entity: Entity, viewMatrix: Matrix4f): Matrix4f {
        val rotation = entity.rotation;
        modelViewMatrix.identity().translate(entity.position).
                rotateX(Math.toRadians(-rotation.x.toDouble()).toFloat()).
                rotateY(Math.toRadians(-rotation.y.toDouble()).toFloat()).
                rotateZ(Math.toRadians(-rotation.z.toDouble()).toFloat()).
                scale(entity.scale);
        val currView = Matrix4f(viewMatrix);
        return currView.mul(modelViewMatrix);
    }

    fun getViewMatrix(camera: Camera): Matrix4f {
        val cameraPos = camera.position;
        val rotation = camera.rotation;

        viewMatrix.identity();
        // First do the rotation so camera rotates over its position
        viewMatrix.rotate(Math.toRadians(rotation.x.toDouble()).toFloat(), Vector3f(1f, 0f, 0f))
                .rotate(Math.toRadians(rotation.y.toDouble()).toFloat(), Vector3f(0f, 1f, 0f));
        // Then do the position
        viewMatrix.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        return viewMatrix;
    }
}