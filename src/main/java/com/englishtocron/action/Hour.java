package com.englishtocron.action;

import com.englishtocron.Cron;
import com.englishtocron.Stack;
import com.englishtocron.StartEnd;

import java.util.Optional;
import java.util.regex.Pattern;

public class Hour {
    private  Hour() {
        // Prevent instantiation
    }
    private static final Pattern RE_MATCH = Pattern.compile("(?i)(hour|hrs|hours)");
    private static final Pattern RE_HOUR = Pattern.compile("^(hour|hrs|hours)$");

    public static boolean tryFromToken(String str) {
        return RE_MATCH.matcher(str).matches();
    }

    public static void process(String token, Cron cron) {
        if (RE_HOUR.matcher(token).matches()) {
            Optional<StartEnd> hour = Optional.empty();
            if (!cron.stack.isEmpty()) {
                Stack element = cron.stack.get(cron.stack.size() - 1);
                if (element.owner == Kind.FrequencyOnly) {
                    hour = Optional.of(new StartEnd(element.frequency, Optional.empty()));
                    cron.syntax.hour = String.format("0/%s", element.frequencyToString());
                    cron.syntax.min = "0";
                    cron.stack.remove(cron.stack.size() - 1);
                } else if (element.owner == Kind.FrequencyWith) {
                    hour = Optional.of(new StartEnd(element.frequency, Optional.empty()));
                    cron.syntax.hour = element.frequencyToString();
                    cron.syntax.min = "0";
                    cron.stack.remove(cron.stack.size() - 1);
                } else if (element.owner == Kind.RangeStart) {
                    element.min = Optional.of(new StartEnd(element.frequencyStart, Optional.empty()));
                    return;
                } else if (element.owner == Kind.RangeEnd) {
                    element.min = Optional.of(new StartEnd(element.frequencyStart, element.frequencyEnd));
                    element.frequencyEnd = Optional.empty();

                    if (element.frequencyStart.isPresent() && element.frequencyEnd.isPresent()) {
                        cron.syntax.hour = String.format("%d-%d", element.frequencyStart.get(), element.frequencyEnd.get());
                        cron.syntax.min = "0";
                    }
                    return;
                }
            }
            cron.syntax.min = "0";

            hour.ifPresent(h -> cron.stack.add(Stack.builder(Kind.Minute).hour(h).build()));
        }
    }
}