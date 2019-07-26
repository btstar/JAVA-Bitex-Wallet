package com.udun_demo.support.utils;

import java.util.Random;

public class GeneratorUtil {
    public GeneratorUtil() {
    }
    public static int getRandomNumber(int from, int to) {
        float a = (float) from + (float) (to - from) * (new Random()).nextFloat();
        int b = (int) a;
        return ((double) (a - (float) b) > 0.5D ? 1 : 0) + b;
    }
}
