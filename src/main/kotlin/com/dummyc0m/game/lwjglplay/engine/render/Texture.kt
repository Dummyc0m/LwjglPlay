package com.dummyc0m.game.lwjglplay.engine.render

import de.matthiasmann.twl.utils.PNGDecoder
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL30.glGenerateMipmap
import java.nio.ByteBuffer

/**
 * Created by Dummy on 5/25/16.
 */
class Texture() {
    private var _textureId: Int
    val textureId: Int
        get() = _textureId;

    init {
        _textureId = -1
    }

    constructor(fileName: String) : this() {
        val decoder = PNGDecoder(Texture::class.java.getResourceAsStream(fileName));
        val buf = ByteBuffer.allocateDirect(4 * decoder.width * decoder.height);
        decoder.decode(buf, decoder.width * 4, PNGDecoder.Format.RGBA);
        buf.flip();
        // Create a new OpenGL texture
        _textureId = glGenTextures();
        // Bind the texture
        glBindTexture(GL_TEXTURE_2D, textureId);

        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, decoder.width, decoder.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);
        glGenerateMipmap(GL_TEXTURE_2D);
    }

    fun bind() {
        glBindTexture(GL_TEXTURE_2D, textureId);
    }

    fun cleanup() {
        if (_textureId != -1) {
            glDeleteTextures(textureId);
        }
    }
}