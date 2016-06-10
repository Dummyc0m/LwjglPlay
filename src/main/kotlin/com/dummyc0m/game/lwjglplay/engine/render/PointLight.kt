package com.dummyc0m.game.lwjglplay.engine.render

import org.joml.Vector3f

/**
 * Created by Dummy on 6/11/16.
 */
class PointLight(var color: Vector3f, var position: Vector3f, var intensity: Float, var attenuation: Attenuation) {

    constructor (color: Vector3f, position: Vector3f, intensity: Float) : this(color, position, intensity, Attenuation(1f, 0f, 0f)) {
    }

    constructor (pointLight: PointLight) : this(Vector3f(pointLight.color), Vector3f(pointLight.position),
            pointLight.intensity, pointLight.attenuation) {
    }

    data class Attenuation(var constant: Float, var linear: Float, var exponent: Float)
}