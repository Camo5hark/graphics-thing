package project1.client.resource;

import java.util.ArrayList;

public class ResourceManager {
    private static final ArrayList<Resource> RESOURCES = new ArrayList<>();

    public static void add(Resource resource) {
        RESOURCES.add(resource);
    }

    public static void cleanup() {
        for (Resource resource : RESOURCES) {
            resource.delete();
        }
    }
}
