package io.github.conphucious.topchute.util;

import lombok.experimental.UtilityClass;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@UtilityClass
public class TimeUtil {

    public static Instant getNextMoveTime() {
        return Instant.now().plus(1, ChronoUnit.DAYS).truncatedTo(ChronoUnit.DAYS);
    }

}
