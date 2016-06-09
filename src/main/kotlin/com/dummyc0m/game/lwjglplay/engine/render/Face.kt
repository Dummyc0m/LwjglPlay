package com.dummyc0m.game.lwjglplay.engine.render

/**
 * Created by Dummy on 5/28/16.
 */
class Face(v1: String, v2: String, v3: String) {
    private var _idxGroups = arrayOfNulls<IdxGroup>(3)
    /**
     * List of idxGroup groups for a face triangle (3 vertices per face).
     */
    val idxGroups: Array<IdxGroup?>
        get() = _idxGroups

    init {
        _idxGroups = arrayOfNulls<IdxGroup>(3)
        // Parse the lines
        _idxGroups[0] = parseLine(v1)
        _idxGroups[1] = parseLine(v2)
        _idxGroups[2] = parseLine(v3)
    }

    private fun parseLine(line: String): IdxGroup {
        val idxGroup = IdxGroup()

        val lineTokens = line.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val length = lineTokens.size
        idxGroup.idxPos = Integer.parseInt(lineTokens[0]) - 1
        if (length > 1) {
            // It can be empty if the obj does not define text coords
            val textCoord = lineTokens[1]
            idxGroup.idxTextCoord = if (textCoord.length > 0) Integer.parseInt(textCoord) - 1 else IdxGroup.NO_VALUE
            if (length > 2) {
                idxGroup.idxVecNormal = Integer.parseInt(lineTokens[2]) - 1
            }
        }

        return idxGroup
    }
}

class IdxGroup {

    var idxPos: Int

    var idxTextCoord: Int

    var idxVecNormal: Int

    init {
        idxPos = NO_VALUE
        idxTextCoord = NO_VALUE
        idxVecNormal = NO_VALUE
    }

    companion object {
        val NO_VALUE = -1
    }
}