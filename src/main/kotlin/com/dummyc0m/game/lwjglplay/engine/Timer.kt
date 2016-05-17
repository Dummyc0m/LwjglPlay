package com.dummyc0m.game.lwjglplay.engine

/**
 * Created by Dummyc0m on 5/16/16.
 */
class Timer {
    private var _lastTick: Double = 0.0;
    val lastTick: Double
        get() = _lastTick;

    fun init() {
        _lastTick = getTime();
    }

    fun getTime(): Double {
        return System.nanoTime() / 1000000000.0;
    }

    fun getElapsedTime(): Float {
        val time = getTime();
        val ret: Float = (time - _lastTick).toFloat();
        _lastTick = time;
        return ret;
    }
}