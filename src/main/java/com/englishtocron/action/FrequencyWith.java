package com.englishtocron.action;

import com.englishtocron.Cron;
import com.englishtocron.Error;
import com.englishtocron.Stack;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FrequencyWith {
    private FrequencyWith() {
        // Prevent instantiation
    }
    private static final Pattern RE_MATCH = Pattern.compile("^[0-9]+(th|nd|rd|st)$");
    private static final Pattern RE_NUMERIC_PREFIX = Pattern.compile("^[0-9]+");

    public static boolean tryFromToken(String str) {
        return RE_MATCH.matcher(str).matches();
    }

    public static void process(String token, Cron cron) throws Error {
        Matcher numericPrefixMatcher = RE_NUMERIC_PREFIX.matcher(token);
        if (!numericPrefixMatcher.find()) {
            throw Error.capture("frequency_with", token);
        }

        int frequency;
        try {
            frequency = Integer.parseInt(numericPrefixMatcher.group());
        } catch (NumberFormatException e) {
            throw Error.parseToNumber("frequency_with", numericPrefixMatcher.group());
        }

        if (!cron.stack.isEmpty()) {
            Stack element = cron.stack.get(cron.stack.size() - 1);
            if (element.owner == Kind.RangeEnd) {
                element.frequencyEnd = Optional.of(frequency);
                return;
            } else if (element.owner == Kind.RangeStart) {
                element.frequencyStart = Optional.of(frequency);
                return;
            }
        }

        cron.stack.add(
                Stack.builder(Kind.FrequencyWith)
                        .frequency(frequency)
                        .dayOfWeek(cron.syntax.dayOfWeek)
                        .build()
        );
    }
}