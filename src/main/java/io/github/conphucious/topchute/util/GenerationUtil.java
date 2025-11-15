package io.github.conphucious.topchute.util;

import lombok.experimental.UtilityClass;

import java.util.Random;
import java.util.UUID;

@UtilityClass
public class GenerationUtil {

    public static String uuid() {
        return String.valueOf(UUID.randomUUID());
    }

    public static int generateRandomInt(int bound) {
        Random random = new Random();
        return random.nextInt(bound);
    }

    public static boolean isRngSelected(int numberPool, int fractionalValue) {
        int randomNumber = generateRandomInt(numberPool);
        return randomNumber < fractionalValue;
    }

}
