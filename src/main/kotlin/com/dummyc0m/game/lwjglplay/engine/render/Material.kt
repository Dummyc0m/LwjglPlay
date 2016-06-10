package com.dummyc0m.game.lwjglplay.engine.render

import org.joml.Vector3f

/**
 * Created by Dummy on 6/10/16.
 */
class Material(var reflectance: Float) {
    private var _texture: Texture
    var texture: Texture
        get() = _texture
        set(value) {
            _texture = value
            _textured = true
        };

    private var _textured: Boolean

    val textured: Boolean
        get() = _textured

    var color: Vector3f;

    init {
        _textured = false
        color = DEFAULT_COLOR
        _texture = DEFAULT_TEXTURE
    }

    constructor() : this(0f) {
    }

    constructor (color: Vector3f, reflectance: Float) : this(reflectance) {
        this.color = color
    }

    constructor (texture: Texture, reflectance: Float) : this(reflectance) {
        this.texture = texture
        _textured = true
    }
}

private val DEFAULT_COLOR = Vector3f(1f, 0f, 1f);
private val DEFAULT_TEXTURE = Texture()