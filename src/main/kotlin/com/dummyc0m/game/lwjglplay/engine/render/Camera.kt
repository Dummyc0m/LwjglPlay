package com.dummyc0m.game.lwjglplay.engine.render

import org.joml.Vector3f

/**
 * Created by Dummy on 5/26/16.
 */
class Camera(val position: Vector3f, val rotation: Vector3f) {
    
    constructor() : this(Vector3f(0f, 0f, 0f), Vector3f(0f, 0f, 0f)) { }
    
    fun setPos(x: Float, y: Float, z: Float) {
        position.x = x;
        position.y = y;
        position.z = z;
    }
    
    fun setRot(x: Float, y: Float, z: Float) {
        rotation.x = x;
        rotation.y = y;
        rotation.z = z;
    }

    fun movePos(offsetX: Float, offsetY: Float, offsetZ: Float) {
        if ( offsetZ != 0f ) {
            position.x += (Math.sin(Math.toRadians(rotation.y.toDouble())) * -1.0f * offsetZ).toFloat();
            position.z += (Math.cos(Math.toRadians(rotation.y.toDouble())) * offsetZ).toFloat();
        }
        if ( offsetX != 0f) {
            position.x += (Math.sin(Math.toRadians((rotation.y - 90f).toDouble())) * -1.0f * offsetX).toFloat();
            position.z += (Math.cos(Math.toRadians((rotation.y - 90f).toDouble())) * offsetX).toFloat();
        }
        position.y += offsetY;
    }

    fun moveRot(offsetX: Float, offsetY: Float, offsetZ: Float) {
        rotation.x += offsetX;
        rotation.y += offsetY;
        rotation.y += offsetZ;
    }
}