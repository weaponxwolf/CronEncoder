package com.englishtocron.action;

import com.englishtocron.Cron;
import com.englishtocron.Stack;
import com.englishtocron.StartEndString;

import java.util.Optional;
import java.util.regex.Pattern;

public class RangeEnd {
    private RangeEnd() {
        // Prevent instantiation
    }
    private static final Pattern RE_MATCH = Pattern.compile("(?i)(to|through|ending|end|and)");
    private static final Pattern RE_MATCH_AND = Pattern.compile("(?i)(and)");

    public static boolean tryFromToken(String str) {
        return RE_MATCH.matcher(str).matches();
    }

    public static void process(String token, Cron cron) {
        boolean isAnd = RE_MATCH_AND.matcher(token).matches();

        if (!cron.stack.isEmpty()) {
            Stack element = cron.stack.get(cron.stack.size() - 1);
            element.isAndConnector = isAnd;

            switch (element.owner) {
                case FrequencyWith:
                case FrequencyOnly:
                    element.frequencyStart = element.frequency;
                    break;
                case Day:
                    element.day = Optional.of(new StartEndString(
                            element.dayOfWeek,
                            element.day.flatMap(d -> d.end)
                    ));
                    break;
                case Month:
                    element.owner = Kind.RangeEnd;
                    break;
                case RangeStart:
                    element.owner = Kind.RangeEnd;
                    break;
                case Year:
                case ClockTime:
                case Minute:
                case Hour:
                case RangeEnd:
                case Second:
                case OnlyOn:
                    break;
            }
            element.owner = Kind.RangeEnd;
        }
    }
}