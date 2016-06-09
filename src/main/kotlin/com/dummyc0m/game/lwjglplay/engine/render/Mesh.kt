package com.dummyc0m.game.lwjglplay.engine.render

import com.dummyc0m.game.lwjglplay.engine.util.Util
import com.dummyc0m.game.lwjglplay.game.DEFAULT_COLOR
import org.joml.Vector2f
import org.joml.Vector3f
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL13.GL_TEXTURE0
import org.lwjgl.opengl.GL13.glActiveTexture
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL20.glDisableVertexAttribArray
import org.lwjgl.opengl.GL20.glVertexAttribPointer
import org.lwjgl.opengl.GL30.*
import java.util.*

/**
 * Created by Dummyc0m on 5/18/16.
 */
class Mesh(positions: FloatArray, textureCoords: FloatArray, normals: FloatArray, indices: IntArray) {
    val vaoId: Int;

    private val posVboId: Int;

    private val idxVboId: Int;

    private val textureVboId: Int;

    private val normalsVboId: Int;

    val vertexCount: Int;

    var texture: Texture? = null;

    var color: Vector3f = DEFAULT_COLOR;

    init {
        vertexCount = indices.size;

        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        posVboId = glGenBuffers();
        val posBuffer = BufferUtils.createFloatBuffer(positions.size);
        posBuffer.put(positions).flip();
        glBindBuffer(GL_ARRAY_BUFFER, posVboId);
        glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        textureVboId = glGenBuffers();
        val textureBuffer = BufferUtils.createFloatBuffer(textureCoords.size);
        textureBuffer.put(textureCoords).flip();
        glBindBuffer(GL_ARRAY_BUFFER, textureVboId);
        glBufferData(GL_ARRAY_BUFFER, textureBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

        normalsVboId = glGenBuffers()
        val vecNormalsBuffer = BufferUtils.createFloatBuffer(normals.size)
        vecNormalsBuffer.put(normals).flip()
        glBindBuffer(GL_ARRAY_BUFFER, normalsVboId)
        glBufferData(GL_ARRAY_BUFFER, vecNormalsBuffer, GL_STATIC_DRAW)
        glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0)

        idxVboId = glGenBuffers();
        val indicesBuffer = BufferUtils.createIntBuffer(indices.size);
        indicesBuffer.put(indices).flip();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxVboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    fun cleanup() {
        glDisableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

        glDeleteBuffers(posVboId);
        glDeleteBuffers(idxVboId);
        glDeleteBuffers(textureVboId);
        glDeleteBuffers(normalsVboId);

        texture?.cleanup();

        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }

    fun render() {
        val texture = this.texture;
        if (texture != null) {
            glActiveTexture(GL_TEXTURE0);
            texture.bind();
        }

        glBindVertexArray(vaoId);
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        GL11.glDrawElements(GL11.GL_TRIANGLES, vertexCount, GL11.GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    companion object {
        fun loadFromObj(fileName: String): Mesh {
            val lines: List<String> = Util.readAllLines(fileName);

            val vertices = ArrayList<Vector3f>()
            val textures = ArrayList<Vector2f>()
            val normals = ArrayList<Vector3f>()
            val faces = ArrayList<Face>()

            for (line in lines) {
                val tokens = line.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                when (if (tokens.size > 0) tokens[0] else "") {
                    "v" -> {
                        // Geometric vertex
                        val vec3f = Vector3f(
                                java.lang.Float.parseFloat(tokens[1]),
                                java.lang.Float.parseFloat(tokens[2]),
                                java.lang.Float.parseFloat(tokens[3]))
                        vertices.add(vec3f)
                    }
                    "vt" -> {
                        // Texture coordinate
                        val vec2f = Vector2f(
                                java.lang.Float.parseFloat(tokens[1]),
                                java.lang.Float.parseFloat(tokens[2]))
                        textures.add(vec2f)
                    }
                    "vn" -> {
                        // Vertex normal
                        val vec3fNorm = Vector3f(
                                java.lang.Float.parseFloat(tokens[1]),
                                java.lang.Float.parseFloat(tokens[2]),
                                java.lang.Float.parseFloat(tokens[3]))
                        normals.add(vec3fNorm)
                    }
                    "f" -> {
                        val face = Face(tokens[1], tokens[2], tokens[3])
                        faces.add(face)
                    }
                }
            }
            return reorderLists(vertices, textures, normals, faces)
        }


        private fun reorderLists(posList: List<Vector3f>, textCoordList: List<Vector2f>,
                                 normList: List<Vector3f>, facesList: List<Face>): Mesh {

            val indices = ArrayList<Int>()
            // Create position array in the order it has been declared
            val posArr = FloatArray(posList.size * 3)
            var i = 0
            for (pos in posList) {
                posArr[i * 3] = pos.x
                posArr[i * 3 + 1] = pos.y
                posArr[i * 3 + 2] = pos.z
                i++
            }
            val textCoordArr = FloatArray(posList.size * 2)
            val normArr = FloatArray(posList.size * 3)

            for (face in facesList) {
                val faceVertexIndices = face.idxGroups
                for (indValue in faceVertexIndices) {
                    processFaceVertex(indValue!!, textCoordList, normList,
                            indices, textCoordArr, normArr)
                }
            }
            val indicesArr = indices.toIntArray()
            val mesh = Mesh(posArr, textCoordArr, normArr, indicesArr)
            return mesh
        }

        private fun processFaceVertex(indices: IdxGroup, textCoordList: List<Vector2f>,
                                      normList: List<Vector3f>, indicesList: MutableList<Int>,
                                      texCoordArr: FloatArray, normArr: FloatArray) {

            // Set index for vertex coordinates
            val posIndex = indices.idxPos
            indicesList.add(posIndex)

            // Reorder texture coordinates
            if (indices.idxTextCoord >= 0) {
                val textCoord = textCoordList[indices.idxTextCoord]
                texCoordArr[posIndex * 2] = textCoord.x
                texCoordArr[posIndex * 2 + 1] = 1 - textCoord.y
            }
            if (indices.idxVecNormal >= 0) {
                // Reorder vectornormals
                val vecNorm = normList[indices.idxVecNormal]
                normArr[posIndex * 3] = vecNorm.x
                normArr[posIndex * 3 + 1] = vecNorm.y
                normArr[posIndex * 3 + 2] = vecNorm.z
            }
        }
    }
}