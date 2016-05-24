package com.dummyc0m.game.lwjglplay

import com.dummyc0m.game.lwjglplay.engine.GameEngine
import com.dummyc0m.game.lwjglplay.game.TestGame

/**
 * Created by Dummy on 5/15/16.
 */
fun main(args: Array<String>) {
    try {
        SharedLibraryLoader.load();
        val vSync = true;
        val fps = 80;
        val tps = 20;
        val gameLogic = TestGame();
        val gameEngine = GameEngine("GAME", 600, 480, vSync, gameLogic, fps, tps);
        gameEngine.start();
    } catch (e: Exception) {
        e.printStackTrace();
        System.exit(-1);
    }
}
