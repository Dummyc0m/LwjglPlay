package com.dummyc0m.game.lwjglplay.engine.util

import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

/**
 * Created by Dummy on 5/28/16.
 */
object Util {
    fun readAllLines(fileName: String): List<String> {
        val list = ArrayList<String>()
        BufferedReader(InputStreamReader(Util::class.java.getResourceAsStream(fileName)))
                .use { br ->
                    var line: String? = br.readLine()
                    while (line != null) {
                        list.add(line)
                        line = br.readLine()
                    }
                }
        return list
    }

    fun loadResource(name: String): String {
        var result = "";
        try {
            val input = Util::class.java.getResourceAsStream(name);
            result = Scanner(input, "UTF-8").useDelimiter("\\A").next();
        } finally {
            return result;
        }
    }
}