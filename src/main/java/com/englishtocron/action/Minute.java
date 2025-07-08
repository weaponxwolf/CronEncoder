package com.englishtocron.action;

import com.englishtocron.Cron;
import com.englishtocron.Stack;
import com.englishtocron.StartEnd;

import java.util.Optional;
import java.util.regex.Pattern;

public class Minute {
    private Minute() {
        // Prevent instantiation
    }
    private static final Pattern RE_MATCH = Pattern.compile("(?i)(minutes|minute|mins|min)");
    private static final Pattern RE_MINUTES = Pattern.compile("(?i)^(minutes|minute|mins|min)$");

    public static boolean tryFromToken(String str) {
        return RE_MATCH.matcher(str).matches();
    }

    public static void process(String token, Cron cron) {
        if (RE_MINUTES.matcher(token).matches()) {
            Optional<StartEnd> minutes = Optional.empty();
            if (!cron.stack.isEmpty()) {
                Stack element = cron.stack.get(cron.stack.size() - 1);
                if (element.owner == Kind.FrequencyOnly) {
                    minutes = Optional.of(new StartEnd(element.frequency, Optional.empty()));
                    cron.syntax.min = String.format("0/%s", element.frequencyToString());
                    cron.stack.remove(cron.stack.size() - 1);
                } else if (element.owner == Kind.FrequencyWith) {
                    minutes = Optional.of(new StartEnd(element.frequency, Optional.empty()));
                    cron.syntax.min = element.frequencyToString();
                    cron.stack.remove(cron.stack.size() - 1);
                } else if (element.owner == Kind.RangeStart) {
                    element.min = Optional.of(new StartEnd(element.frequencyStart, Optional.empty()));
                    return;
                } else if (element.owner == Kind.RangeEnd) {
                    element.min = Optional.of(new StartEnd(element.frequencyStart, element.frequencyEnd));
                    element.frequencyEnd = Optional.empty();

                    if (element.frequencyStart.isPresent() && element.frequencyEnd.isPresent()) {
                        cron.syntax.min = String.format("%d-%d", element.frequencyStart.get(), element.frequencyEnd.get());
                    }
                    return;
                }
            }

            minutes.ifPresent(m -> cron.stack.add(Stack.builder(Kind.Minute).min(m).build()));
        }
    }
}