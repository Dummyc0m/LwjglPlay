package com.dummyc0m.game.lwjglplay.engine

import com.dummyc0m.game.lwjglplay.SharedLibraryLoader
import com.dummyc0m.game.lwjglplay.engine.util.Timer

/**
 * Created by Dummyc0m on 5/16/16.
 */
class GameEngine(windowTitle: String, width: Int, height: Int, val vSync: Boolean, val gameLogic: IGameLogic, var fps: Int, var tps: Int) : Runnable {
    private val gameLoopThread: Thread;
    private val window: Window;
    private val mouseInput: MouseInput;
    private val timer: Timer;

    init {
        gameLoopThread = Thread(this, "GAME_LOOP_THREAD");
        window = Window(vSync, windowTitle, width, height);
        mouseInput = MouseInput();
        timer = Timer();
    }

    override fun run() {
        try {
            init();
            gameLoop();
        } catch (e: Exception) {
            e.printStackTrace();
        } finally {
            cleanup();
        }
    }

    fun start() {
        if (SharedLibraryLoader.isMac) {
            gameLoopThread.run();
        } else {
            gameLoopThread.start();
        }
    }

    protected fun input() {
        mouseInput.input(window);
        gameLogic.input(window, mouseInput);
    }

    protected fun cleanup() {
        gameLogic.cleanup();
    }

    protected fun update(interval: Float) {
        gameLogic.update(interval, mouseInput);
    }

    protected fun render() {
        gameLogic.render(window);
        window.update();
    }

    private fun init() {
        window.init();
        mouseInput.init(window);
        timer.init();
        gameLogic.init(window);
    }

    private fun gameLoop() {
        var ellapsedTime: Float;
        var accumulator = 0f;
        val interval = 1f / tps;

        val running = true;
        while (running && !window.windowShouldClose()) {
            ellapsedTime = timer.getElapsedTime();
            accumulator += ellapsedTime;

            input();

            while (accumulator >= interval) {
                update(interval);
                accumulator -= interval;
            }

            render();

            if (!window.vsync) {
                sync();
            }
        }
    }

    private fun sync() {
        val loopSlot = 1f / fps;
        val endTime = timer.lastTick + loopSlot;
        while (timer.getTime() < endTime) {
            Thread.sleep(1);
        }
    }
}