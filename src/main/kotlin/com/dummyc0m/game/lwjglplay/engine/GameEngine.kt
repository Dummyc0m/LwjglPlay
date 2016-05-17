package com.dummyc0m.game.lwjglplay.engine

import com.dummyc0m.game.lwjglplay.SharedLibraryLoader

/**
 * Created by Dummyc0m on 5/16/16.
 */
class GameEngine(windowTitle: String, width: Int, height: Int, val vSync: Boolean, val gameLogic: IGameLogic, var fps: Int, var tps: Int): Runnable {
    private val gameLoopThread: Thread;
    private val window: Window;
    private val timer: Timer;

    init {
        gameLoopThread = Thread(this, "GAME_LOOP_THREAD");
        window = Window(vSync, windowTitle, width, height);
        timer = Timer();
    }

    override fun run() {
        try {
            init();
            gameLoop();
        } catch (e: Exception) {
            e.printStackTrace();
        }
    }

    fun start() {
        if(SharedLibraryLoader.isMac) {
            gameLoopThread.run();
        } else {
            gameLoopThread.start();
        }
    }

    protected fun input() {
        gameLogic.input(window);
    }

    protected fun update(interval: Float) {
        gameLogic.update(interval);
    }

    protected fun render() {
        gameLogic.render(window);
        window.update();
    }

    private fun init() {
        window.init();
        timer.init();
        gameLogic.init();
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