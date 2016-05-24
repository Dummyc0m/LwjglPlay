package com.dummyc0m.game.lwjglplay.engine

/**
 * Created by Dummyc0m on 5/16/16.
 */
interface IGameLogic {
    fun init(window: Window);

    fun input(window: Window);

    fun update(interval: Float);

    fun render(window: Window);

    fun cleanup();
}