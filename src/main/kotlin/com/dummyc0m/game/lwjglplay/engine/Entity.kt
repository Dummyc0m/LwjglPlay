package com.dummyc0m.game.lwjglplay.engine

import com.dummyc0m.game.lwjglplay.engine.util.Mesh
import org.joml.Vector3f

/**
 * Created by Dummyc0m on 5/24/16.
 */
class Entity(val mesh: Mesh) {
    val translation: Vector3f = Vector3f();
    val rotation: Vector3f = Vector3f();
    var scale: Float = 1f;

    fun setTranslation(x: Float, y: Float, z: Float) {
        translation.x = x;
        translation.y = y;
        translation.z = z;
    }
    
    fun setRotation(x: Float, y: Float, z: Float) {
        rotation.x = x;
        rotation.y = y;
        rotation.z = z;
    }
}