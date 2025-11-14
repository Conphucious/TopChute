package io.github.conphucious.topchute.util;

import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class GenerationUtil {

    public static String uuid() {
        return String.valueOf(UUID.randomUUID());
    }

}
