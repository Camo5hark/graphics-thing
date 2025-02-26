package project1.helper;

import java.util.ArrayList;

public class ListHelper {
    public static float[] floatListToArray(ArrayList<Float> list) {
        float[] array = new float[list.size()];

        for (int i = 0; i < array.length; i++) {
            array[i] = list.get(i);
        }

        return array;
    }

    public static int[] intListToArray(ArrayList<Integer> list) {
        int[] array = new int[list.size()];

        for (int i = 0; i < array.length; i++) {
            array[i] = list.get(i);
        }

        return array;
    }
}
