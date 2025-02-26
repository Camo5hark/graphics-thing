package project1.client;

import org.joml.Vector3d;
import project1.client.display.Monitor;
import project1.client.display.Window;
import project1.client.light.*;
import project1.client.mesh.Material;
import project1.client.mesh.Mesh;
import project1.client.mesh.MeshLoader;
import project1.client.renderer.Framebuffer;
import project1.client.display.GLFWManager;
import project1.client.resource.ResourceLoader;
import project1.client.resource.ResourceManager;
import project1.client.shader.Shader;
import project1.client.shader.ShaderProgram;
import project1.client.texture.FramebufferTexture;
import project1.client.texture.MapTexture;
import project1.client.world.Camera;
import project1.client.world.Transform;

import static org.lwjgl.opengl.GL46.*;
import static org.lwjgl.glfw.GLFW.*;

public class Client {
    public static final Client CLIENT = new Client();

    private Window window;
    private ResourceLoader resourceLoader;
    public boolean running;

    public void run() {
        running = true;

        new Thread(() -> {
            try {
                GLFWManager.init();

                window = new Window(false, new Monitor(0), 1280, 720, "Project1", false);
                resourceLoader = new ResourceLoader("/res/");

                //deferred

                Framebuffer gBuffer = new Framebuffer(window.width, window.height, Framebuffer.CLEAR_MASK,
                        new FramebufferTexture(GL_DEPTH_COMPONENT, window.width, window.height, GL_DEPTH_COMPONENT, GL_FLOAT, GL_DEPTH_ATTACHMENT, false),
                        new FramebufferTexture(GL_RGBA16F, window.width, window.height, GL_RGBA, GL_FLOAT, GL_COLOR_ATTACHMENT0, true),
                        new FramebufferTexture(GL_RGBA16F, window.width, window.height, GL_RGBA, GL_FLOAT, GL_COLOR_ATTACHMENT1, true),
                        new FramebufferTexture(GL_RGBA16F, window.width, window.height, GL_RGBA, GL_FLOAT, GL_COLOR_ATTACHMENT2, true)
                );

                ShaderProgram gBufferGeom = new ShaderProgram(
                        new Shader(GL_VERTEX_SHADER, "g-buffer-geom.vsh"),
                        new Shader(GL_FRAGMENT_SHADER, "g-buffer-geom.fsh")
                );

                ShaderProgram gBufferLight = new ShaderProgram(
                        new Shader(GL_VERTEX_SHADER, "g-buffer-light.vsh"),
                        new Shader(GL_FRAGMENT_SHADER, "g-buffer-light.fsh")
                );

                Mesh quad = new Mesh(
                        new float[] {
                                -1.0F,  1.0F, 0.0F,
                                -1.0F, -1.0F, 0.0F,
                                 1.0F,  1.0F, 0.0F,
                                 1.0F, -1.0F, 0.0F
                        },
                        new float[] {
                                0.0F, 1.0F,
                                0.0F, 0.0F,
                                1.0F, 1.0F,
                                1.0F, 0.0F
                        },
                        new float[] {
                                0.0F, 0.0F, 0.0F,
                                0.0F, 0.0F, 0.0F,
                                0.0F, 0.0F, 0.0F,
                                0.0F, 0.0F, 0.0F
                        },
                        new int[] {
                                0, 1, 2,
                                1, 3, 2
                        }
                );

                //end deferred

                ShaderProgram shader = new ShaderProgram(
                        new Shader(GL_VERTEX_SHADER, "shader.vsh"),
                        new Shader(GL_FRAGMENT_SHADER, "shader.fsh")
                );

                Mesh cubeMesh = MeshLoader.load("cube.dae");
                Material cubeMaterial = new Material();
                //cubeMaterial.albedo.w = 0.5;
                cubeMaterial.texture = new MapTexture("wall.png");
                Mesh planeMesh = new Mesh(
                        new float[] {
                                -10.0F, 0.0F, -10.0F,
                                 10.0F, 0.0F, -10.0F,
                                 10.0F, 0.0F,  10.0F,
                                -10.0F, 0.0F,  10.0F
                        },
                        new float[] {
                                0.0F, 0.0F,
                                1.0F, 0.0F,
                                1.0F, 1.0F,
                                0.0F, 1.0F
                        },
                        new float[] {
                                0.0F, 1.0F, 0.0F,
                                0.0F, 1.0F, 0.0F,
                                0.0F, 1.0F, 0.0F,
                                0.0F, 1.0F, 0.0F
                        },
                        new int[] {
                                0, 1, 2,
                                2, 3, 0
                        }
                );
                Material planeMaterial = new Material();

                AmbientLight ambientLight = new AmbientLight();
                DirectionalLight directionalLight = new DirectionalLight();
                PointLight pointLight = new PointLight();
                SpotLight spotLight = new SpotLight();
                spotLight.direction = new Vector3d(0.0, 0.0, -1.0);
                spotLight.cutOff = Math.toRadians(10.0);

                Camera camera = new Camera();
                Transform cubeTransform = new Transform(new Vector3d(0.0, 0.0, -2.0), new Vector3d());
                Transform planeTransform = new Transform(new Vector3d(0.0, -1.0, 0.0), new Vector3d());

                while (window.isOpen() && isRunning()) {
                    Timer.update();

                    //movement test

                    double speed = Timer.getDeltaTime();

                    if (glfwGetKey(window.getGlfwWindowID(), GLFW_KEY_A) == GLFW_PRESS) {
                        camera.position.x -= speed;
                    }

                    if (glfwGetKey(window.getGlfwWindowID(), GLFW_KEY_D) == GLFW_PRESS) {
                        camera.position.x += speed;
                    }

                    if (glfwGetKey(window.getGlfwWindowID(), GLFW_KEY_W) == GLFW_PRESS) {
                        camera.position.z -= speed;
                    }

                    if (glfwGetKey(window.getGlfwWindowID(), GLFW_KEY_S) == GLFW_PRESS) {
                        camera.position.z += speed;
                    }

                    if (glfwGetKey(window.getGlfwWindowID(), GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS) {
                        camera.position.y -= speed;
                    }

                    if (glfwGetKey(window.getGlfwWindowID(), GLFW_KEY_SPACE) == GLFW_PRESS) {
                        camera.position.y += speed;
                    }

                    if (glfwGetKey(window.getGlfwWindowID(), GLFW_KEY_RIGHT) == GLFW_PRESS) {
                        camera.rotation.y -= speed;
                    }

                    if (glfwGetKey(window.getGlfwWindowID(), GLFW_KEY_LEFT) == GLFW_PRESS) {
                        camera.rotation.y += speed;
                    }

                    if (glfwGetKey(window.getGlfwWindowID(), GLFW_KEY_DOWN) == GLFW_PRESS) {
                        camera.rotation.x -= speed;
                    }

                    if (glfwGetKey(window.getGlfwWindowID(), GLFW_KEY_UP) == GLFW_PRESS) {
                        camera.rotation.x += speed;
                    }

                    //begin frame

                    camera.updateWorldView();

                    gBuffer.bind();

                    cubeMaterial.texture.bind(0);

                    gBufferGeom.bind();
                    gBufferGeom.setUniformMat4("projection", camera.getProjection());
                    gBufferGeom.setUniformMat4("worldView", camera.getWorldView());
                    gBufferGeom.setUniformMat4("modelView", cubeTransform.calcModelView());
                    gBufferGeom.setUniformMaterial("material", cubeMaterial);
                    gBufferGeom.setUniformInt("albedoTexture", 0);

                    cubeMesh.draw();

                    Framebuffer.bindDefault();

                    gBuffer.textures[1].bind(0);
                    gBuffer.textures[2].bind(1);
                    gBuffer.textures[3].bind(2);

                    gBufferLight.bind();
                    gBufferLight.setUniformInt("positionTexture", 0);
                    gBufferLight.setUniformInt("normalAndShininessTexture", 1);
                    gBufferLight.setUniformInt("albedoTexture", 2);
                    gBufferLight.setUniformVec3("cameraPos", camera.position);
                    gBufferLight.setUniformAmbientLight("ambientLight", ambientLight);
                    gBufferLight.setUniformDirectionalLight("directionalLight", directionalLight);
                    //gBufferLight.setUniformPointLight("pointLight", pointLight);
                    //gBufferLight.setUniformSpotLight("spotLight", spotLight);

                    quad.draw();

//                    Framebuffer.unbind();
//
//                    //render cube
//
//                    shader.bind();
//                    shader.setUniformMat4("projection", camera.getProjection());
//                    shader.setUniformMat4("worldView", camera.getWorldView());
//                    shader.setUniformMat4("modelView", cubeTransform.calcModelView());
//                    shader.setUniformMaterial("material", cubeMaterial);
//                    shader.setUniformInt("albedoTexture", 0);
//                    shader.setUniformVec3("cameraPos", camera.position);
//                    shader.setUniformAmbientLight("ambientLight", ambientLight);
//                    //shader.setUniformDirectionalLight("directionalLight", directionalLight);
//                    shader.setUniformPointLight("pointLight", pointLight);
//                    //program.setUniformSpotLight("spotLight", spotLight);
//
//                    cubeMesh.draw();
//
//                    //render plane
//
//                    shader.bind();
//                    shader.setUniformMat4("projection", camera.getProjection());
//                    shader.setUniformMat4("worldView", camera.getWorldView());
//                    shader.setUniformMat4("modelView", planeTransform.calcModelView());
//                    shader.setUniformMaterial("material", planeMaterial);
//                    shader.setUniformInt("albedoTexture", 0);
//                    shader.setUniformVec3("cameraPos", camera.position);
//                    shader.setUniformAmbientLight("ambientLight", ambientLight);
//                    //shader.setUniformDirectionalLight("directionalLight", directionalLight);
//                    shader.setUniformPointLight("pointLight", pointLight);
//                    //program.setUniformSpotLight("spotLight", spotLight);
//
//                    planeMesh.draw();
//
//                    //end frame

                    window.swapBuffers();

                    GLFWManager.pollEvents();
                }
            } catch (Throwable t) {
                t.printStackTrace();
            } finally {
                ResourceManager.cleanup();

                if (window != null) {
                    window.destroy();
                }

                GLFWManager.term();
            }
        }, "Render").start();
    }

    public Window getWindow() {
        return window;
    }

    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

    private synchronized boolean isRunning() {
        return running;
    }
}
