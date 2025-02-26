package project1.client.mesh;

import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIVector3D;
import project1.client.Client;
import project1.client.GLBufferHelper;
import project1.client.resource.ResourceLoader;
import project1.helper.ListHelper;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import static org.lwjgl.assimp.Assimp.*;

public class MeshLoader {
    private static final ResourceLoader RESOURCE_LOADER = Client.CLIENT.getResourceLoader();
    private static final int AI_IMPORT_FLAGS = aiProcess_JoinIdenticalVertices | aiProcess_Triangulate;

    public static Mesh load(String resPath) {
        ByteBuffer resource = null;
        AIScene aiScene = null;

        try {
            resource = RESOURCE_LOADER.loadGLByteBuffer("meshes/" + resPath);
            aiScene = aiImportFileFromMemory(resource, AI_IMPORT_FLAGS, (ByteBuffer) null);

            if (aiScene == null) {
                throw new IllegalStateException(resPath + ": Mesh import error: " + aiGetErrorString());
            }

            PointerBuffer aiMeshes = aiScene.mMeshes();

            if (aiMeshes == null) {
                throw new IllegalStateException(resPath + ": No meshes found");
            }

            AIMesh aiMesh = AIMesh.create(aiMeshes.get());
            AIVector3D.Buffer aiVertices = aiMesh.mVertices();
            ArrayList<Float> vertexData = new ArrayList<>();

            while (aiVertices.hasRemaining()) {
                AIVector3D aiVertex = aiVertices.get();

                vertexData.add(aiVertex.x());
                vertexData.add(aiVertex.y());
                vertexData.add(aiVertex.z());
            }

            AIVector3D.Buffer aiTexcoords = aiMesh.mTextureCoords(0);
            ArrayList<Float> texcoordData = new ArrayList<>();

            if (aiTexcoords != null) {
                while (aiTexcoords.hasRemaining()) {
                    AIVector3D aiTexcoord = aiTexcoords.get();

                    texcoordData.add(aiTexcoord.x());
                    texcoordData.add(1.0F - aiTexcoord.y());
                }
            }

            AIVector3D.Buffer aiNormals = aiMesh.mNormals();
            ArrayList<Float> normalData = new ArrayList<>();

            if (aiNormals != null) {
                while (aiNormals.hasRemaining()) {
                    AIVector3D aiNormal = aiNormals.get();

                    normalData.add(aiNormal.x());
                    normalData.add(aiNormal.y());
                    normalData.add(aiNormal.z());
                }
            }

            AIFace.Buffer aiFaces = aiMesh.mFaces();
            ArrayList<Integer> indexData = new ArrayList<>();

            while (aiFaces.hasRemaining()) {
                IntBuffer aiIndices = aiFaces.get().mIndices();

                while (aiIndices.hasRemaining()) {
                    indexData.add(aiIndices.get());
                }
            }

            return new Mesh(
                    ListHelper.floatListToArray(vertexData),
                    ListHelper.floatListToArray(texcoordData),
                    ListHelper.floatListToArray(normalData),
                    ListHelper.intListToArray(indexData)
            );
        } finally {
            if (aiScene != null) {
                aiScene.free();
            }

            GLBufferHelper.free(resource);
        }
    }
}
