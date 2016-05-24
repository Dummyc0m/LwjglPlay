package com.dummyc0m.game.lwjglplay.engine

import org.joml.Matrix4f
import org.joml.Vector3f

/**
 * Created by Dummyc0m on 5/24/16.
 */
class Transformation {
    val projectionMatrix: Matrix4f;
    private val worldMatrix: Matrix4f;

    init {
        projectionMatrix = Matrix4f();
        worldMatrix = Matrix4f();
    }

    fun setProjectionMatrix(fov: Float, width: Int, height: Int, zNear: Float, zFar: Float): Matrix4f {
        val aspectRatio = width.toFloat() / height;
        projectionMatrix.identity()
                .perspective(fov, aspectRatio, zNear, zFar);
        return projectionMatrix;
    }

    fun getWorldMatrix(offset: Vector3f, rotation: Vector3f, scale: Float): Matrix4f {
        worldMatrix.identity().translate(offset).
                rotateX(Math.toRadians(rotation.x.toDouble()).toFloat()).
                rotateY(Math.toRadians(rotation.y.toDouble()).toFloat()).
                rotateZ(Math.toRadians(rotation.z.toDouble()).toFloat()).
                scale(scale);
        return worldMatrix;
    }
}