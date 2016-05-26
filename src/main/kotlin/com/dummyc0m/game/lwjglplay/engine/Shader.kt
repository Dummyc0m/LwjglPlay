package com.dummyc0m.game.lwjglplay.engine

import org.joml.Matrix4f
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL20.*;
import java.nio.FloatBuffer
import java.util.*

/**
 * Created by Dummyc0m on 5/18/16.
 */
class Shader {
    private var programId: Int = 0;

    private var vertexShaderId = 0;

    private var fragmentShaderId = 0;

    //null safety
    private val uniformMap: MutableMap<String, Int>;

    init {
        uniformMap = HashMap();
    }

    fun init() {
        programId = glCreateProgram();

        if (programId == 0) {
            throw Exception("Unable to create program");
        }
    }

    fun createVertexShader(shaderCode: String) {
        vertexShaderId = createShader(shaderCode, GL_VERTEX_SHADER);
    }

    fun createFragmentShader(shaderCode: String) {
        fragmentShaderId = createShader(shaderCode, GL_FRAGMENT_SHADER);
    }

    protected fun createShader(shaderCode: String, shaderType: Int): Int {
        val shaderId = glCreateShader(shaderType);

        if (shaderId == 0) {
            throw Exception("Unable to create shader, code: " + shaderCode);
        }

        glShaderSource(shaderId, shaderCode);

        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw Exception("Unable to compile shader, error: " + glGetShaderInfoLog(shaderId, 1024));
        }

        glAttachShader(programId, shaderId);

        return shaderId;
    }

    fun link() {
        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw Exception("Error linking Shader, code: " + glGetShaderInfoLog(programId, 1024));
        }

        glValidateProgram(programId);
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating Shader, code: " + glGetShaderInfoLog(programId, 1024));
        }
    }

    fun bind() {
        glUseProgram(programId);
    }

    fun unbind() {
        glUseProgram(0);
    }

    fun cleanup() {
        unbind();
        if (programId != 0) {
            if (vertexShaderId != 0) {
                glDetachShader(programId, vertexShaderId);
            }
            if (fragmentShaderId != 0) {
                glDetachShader(programId, fragmentShaderId);
            }
            glDeleteProgram(programId);
        }
    }

    fun createUniform(uniformName: String) {
        val uniformLocation = glGetUniformLocation(programId, uniformName);
        if (uniformLocation < 0) {
            throw RuntimeException("Could not find uniform:" +
                    uniformName);
        }
        uniformMap.put(uniformName, uniformLocation)
    }

    fun setUniform(uniformName: String, value: Matrix4f) {
        // Dump the matrix into a float buffer
        val fb: FloatBuffer = BufferUtils.createFloatBuffer(16);
        value.get(fb);
        glUniformMatrix4fv(uniformMap[uniformName]!!.toInt(), false, fb);
    }

    fun setUniform(uniformName: String, value: Int) {
        glUniform1i(uniformMap.get(uniformName)!!, value);
    }

    companion object {
        fun loadResource(name: String): String {
            var result = "";
            try {
                val input = Shader::class.java.getResourceAsStream(name);
                result = Scanner(input, "UTF-8").useDelimiter("\\A").next();
            } finally {
                return result;
            }
        }
    }
}