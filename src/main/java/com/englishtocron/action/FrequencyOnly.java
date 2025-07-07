package com.englishtocron.action;

import com.englishtocron.Cron;
import com.englishtocron.Stack;

import java.util.Optional;
import java.util.regex.Pattern;

public class FrequencyOnly {
    private static final Pattern RE_MATCH = Pattern.compile("^[0-9]+$");

    public static boolean tryFromToken(String str) {
        return RE_MATCH.matcher(str).matches();
    }

    public static void process(int frequency, Cron cron) {
        if (!cron.stack.isEmpty()) {
            Stack lastStack = cron.stack.get(cron.stack.size() - 1);
            if (lastStack.owner == Kind.RangeEnd) {
                lastStack.frequencyEnd = Optional.of(frequency);
                return;
            } else if (lastStack.owner == Kind.RangeStart) {
                lastStack.frequencyStart = Optional.of(frequency);
                return;
            }
        }
        cron.stack.add(
                Stack.builder(Kind.FrequencyOnly)
                        .frequency(frequency)
                        .build()
        );
    }
}