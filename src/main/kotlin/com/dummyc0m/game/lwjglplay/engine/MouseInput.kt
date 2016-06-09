package com.dummyc0m.game.lwjglplay.engine

import org.joml.Vector2d
import org.joml.Vector2f
import org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWCursorEnterCallback
import org.lwjgl.glfw.GLFWCursorPosCallback
import org.lwjgl.glfw.GLFWMouseButtonCallback

/**
 * Created by Dummy on 5/26/16.
 */
class MouseInput {

    private var previousPos: Vector2d

    private var currentPos: Vector2d

    val displayVector: Vector2f

    private var inWindow = false

    private var _leftButtonPressed = false
    val leftButtonPressed: Boolean
        get() = _leftButtonPressed

    private var _rightButtonPressed = false
    val rightButtonPressed: Boolean
        get() = _rightButtonPressed

    private val cursorPosCallback: GLFWCursorPosCallback;

    private val cursorEnterCallback: GLFWCursorEnterCallback;

    private val mouseButtonCallback: GLFWMouseButtonCallback;

    init {
        previousPos = Vector2d(-1.0, -1.0)
        currentPos = Vector2d(0.0, 0.0)
        displayVector = Vector2f()
        cursorPosCallback = object : GLFWCursorPosCallback() {
            override fun invoke(window: Long, xpos: Double, ypos: Double) {
                currentPos.x = xpos
                currentPos.y = ypos
            }
        }
        cursorEnterCallback = object : GLFWCursorEnterCallback() {
            override fun invoke(window: Long, entered: Boolean) {
                inWindow = entered
            }
        }
        mouseButtonCallback = object : GLFWMouseButtonCallback() {
            override fun invoke(window: Long, button: Int, action: Int, mods: Int) {
                _leftButtonPressed = button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS
                _rightButtonPressed = button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS
            }
        }
    }

    fun init(window: Window) {
        glfwSetCursorPosCallback(window.windowHandle, cursorPosCallback)
        glfwSetCursorEnterCallback(window.windowHandle, cursorEnterCallback)
        glfwSetMouseButtonCallback(window.windowHandle, mouseButtonCallback)
    }

    fun input(window: Window) {
        displayVector.x = 0f
        displayVector.y = 0f
        if (previousPos.x > 0 && previousPos.y > 0 && inWindow) {
            val deltax = currentPos.x - previousPos.x
            val deltay = currentPos.y - previousPos.y
            val rotateX = deltax != 0.0
            val rotateY = deltay != 0.0
            if (rotateX) {
                displayVector.y = deltax.toFloat()
            }
            if (rotateY) {
                displayVector.x = deltay.toFloat()
            }
        }
        previousPos.x = currentPos.x
        previousPos.y = currentPos.y
    }
}