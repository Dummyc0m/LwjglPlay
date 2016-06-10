package com.dummyc0m.game.lwjglplay.engine.render

import org.joml.Matrix4f
import org.joml.Vector3f
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

    fun createPointLightUniform(uniformName: String) {
        createUniform(uniformName + ".color")
        createUniform(uniformName + ".position")
        createUniform(uniformName + ".intensity")
        createUniform(uniformName + ".att.constant")
        createUniform(uniformName + ".att.linear")
        createUniform(uniformName + ".att.exponent")
    }

    fun createMaterialUniform(uniformName: String) {
        createUniform(uniformName + ".color")
        createUniform(uniformName + ".useColor")
        createUniform(uniformName + ".reflectance")
    }

    fun setUniform(uniformName: String, pointLight: PointLight) {
        setUniform(uniformName + ".color", pointLight.color)
        setUniform(uniformName + ".position", pointLight.position)
        setUniform(uniformName + ".intensity", pointLight.intensity)
        val att = pointLight.attenuation
        setUniform(uniformName + ".att.constant", att.constant)
        setUniform(uniformName + ".att.linear", att.linear)
        setUniform(uniformName + ".att.exponent", att.exponent)
    }

    fun setUniform(uniformName: String, material: Material) {
        setUniform(uniformName + ".color", material.color)
        setUniform(uniformName + ".useColor", if (material.textured) 0 else 1)
        setUniform(uniformName + ".reflectance", material.reflectance)
    }

    fun setUniform(uniformName: String, value: Matrix4f) {
        // Dump the matrix into a float buffer
        val fb: FloatBuffer = BufferUtils.createFloatBuffer(16);
        value.get(fb);
        glUniformMatrix4fv(uniformMap[uniformName]!!.toInt(), false, fb);
    }

    fun setUniform(uniformName: String, value: Vector3f) {
        glUniform3f(uniformMap.get(uniformName)!!, value.x, value.y, value.z)
    }

    fun setUniform(uniformName: String, value: Int) {
        glUniform1i(uniformMap.get(uniformName)!!, value);
    }

    fun setUniform(uniformName: String, value: Float) {
        glUniform1f(uniformMap.get(uniformName)!!, value)
    }
}