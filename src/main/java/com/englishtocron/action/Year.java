package com.englishtocron.action;

import com.englishtocron.Cron;
import com.englishtocron.Error;
import com.englishtocron.Stack;
import com.englishtocron.StartEnd;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Year {
    // Accepts year keywords and simple lists
    private static final Pattern RE_MATCH = Pattern.compile(
        "(?i)^\\s*(in|on|during)?\\s*(years?|(?:[0-9]{4})(?:\\s*(?:and|,)?\\s*[0-9]{4})*)\\s*$"
    );

    private static final Pattern RE_YEARS = Pattern.compile("(?i)^\\s*(in|on|during)?\\s*years?\\s*$");

    private static final Pattern RE_NUMERIC = Pattern.compile("\\b[0-9]{4}\\b");

    // NEW: Match range expressions like "from 2023 to 2025"
    private static final Pattern RE_RANGE = Pattern.compile(
        "(?i)(?:from|between|starting)\\s*([0-9]{4})\\s*(?:to|and|ending|end)\\s*([0-9]{4})"
    );

    public static boolean tryFromToken(String str) {
        return RE_MATCH.matcher(str).matches()
                || RE_RANGE.matcher(str).matches()
                || RE_NUMERIC.matcher(str).find();
    }

    public static void process(String token, Cron cron) throws Error {
        token = token.trim();

        // Handle keyword-only: "years", "in years", etc.
        if (RE_YEARS.matcher(token).matches()) {
            cron.syntax.year = "*";
            if (!cron.stack.isEmpty()) {
                Stack element = cron.stack.get(cron.stack.size() - 1);
                switch (element.owner) {
                    case FrequencyOnly:
                        cron.syntax.year = "0/" + element.frequencyToString();
                        cron.stack.remove(cron.stack.size() - 1);
                        break;
                    case FrequencyWith:
                        cron.syntax.year = element.frequencyToString();
                        break;
                    default:
                        cron.syntax.year = "*";
                }
            }
        }

        // Handle range: "from 2024 to 2025", etc.
        else if (RE_RANGE.matcher(token).find()) {
            Matcher range = RE_RANGE.matcher(token);
            if (range.find()) {
                int start = Integer.parseInt(range.group(1));
                int end = Integer.parseInt(range.group(2));
                if (start > end) {
                    throw Error.incorrectValue("year", "start year must be <= end year");
                }
                cron.syntax.year = start + "-" + end;
            } else {
                throw Error.incorrectValue("year", "Unrecognized range format: " + token);
            }
        }

        // Handle year list: "2023, 2024 and 2025"
        else {
            Matcher matcher = RE_NUMERIC.matcher(token);
            List<Integer> years = new ArrayList<>();
            while (matcher.find()) {
                try {
                    years.add(Integer.parseInt(matcher.group()));
                } catch (NumberFormatException e) {
                    throw Error.incorrectValue("year", "Invalid year: " + matcher.group());
                }
            }

            if (years.isEmpty()) {
                throw Error.incorrectValue("year", "No valid 4-digit years found in: " + token);
            }

            // Stack-based range handling
            if (!cron.stack.isEmpty()) {
                Stack element = cron.stack.get(cron.stack.size() - 1);
                if (element.owner == Kind.RangeStart) {
                    element.year = Optional.of(new StartEnd(
                        Optional.of(years.get(0)),
                        element.year.flatMap(y -> y.end)
                    ));
                    return;
                } else if (element.owner == Kind.RangeEnd) {
                    Optional<Integer> start = element.year.flatMap(y -> y.start);
                    Optional<Integer> end = Optional.of(years.get(0));

                    if (start.isPresent() && end.isPresent()) {
                        cron.syntax.year = String.format("%d-%d", start.get(), end.get());
                        cron.stack.remove(cron.stack.size() - 1);
                        return;
                    } else {
                        throw Error.incorrectValue("year", "Incomplete range: " + token);
                    }
                }
            }

            // Regular list of years
            cron.syntax.year = years.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        }

        // Final: push Year to stack
        cron.stack.add(Stack.builder(Kind.Year).build());
    }
}
