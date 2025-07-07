
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
    private static final Pattern RE_MATCH = Pattern.compile("(?i)((years|year)|([0-9]{4}[0-9]*(( ?and)?,? ?))+)$");
    private static final Pattern RE_YEARS = Pattern.compile("(?i)^(years|year)$");
    private static final Pattern RE_NUMERIC = Pattern.compile("[0-9]+");
    private static final Pattern RE_YEAR_FORMAT = Pattern.compile("^[0-9]{4}$");

    public static boolean tryFromToken(String str) {
        return RE_MATCH.matcher(str).matches();
    }

    public static void process(String token, Cron cron) throws Error {
        if (RE_YEARS.matcher(token).matches()) {
            cron.syntax.year = "?";
            if (!cron.stack.isEmpty()) {
                Stack element = cron.stack.get(cron.stack.size() - 1);
                if (element.owner == Kind.FrequencyOnly) {
                    cron.syntax.year = String.format("0/%s", element.frequencyToString());
                    cron.stack.remove(cron.stack.size() - 1);
                } else if (element.owner == Kind.FrequencyWith) {
                    cron.syntax.year = element.frequencyToString();
                } else {
                    cron.syntax.year = "*";
                }
            }
        } else {
            Matcher matches = RE_NUMERIC.matcher(token);
            List<Integer> years = new ArrayList<>();
            while (matches.find()) {
                String yearStr = matches.group();
                if (RE_YEAR_FORMAT.matcher(yearStr).matches()) {
                    try {
                        years.add(Integer.parseInt(yearStr));
                    } catch (NumberFormatException e) {
                        // Should not happen if RE_YEAR_FORMAT matches
                    }
                }
            }

            if (!cron.stack.isEmpty()) {
                Stack element = cron.stack.get(cron.stack.size() - 1);
                if (element.owner == Kind.RangeStart) {
                    element.year = Optional.of(new StartEnd(
                            years.stream().findFirst().map(Optional::of).orElse(Optional.empty()),
                            element.year.flatMap(y -> y.end)
                    ));
                    return;
                } else if (element.owner == Kind.RangeEnd) {
                    StartEnd year = new StartEnd(
                            element.year.flatMap(y -> y.start),
                            years.stream().findFirst().map(Optional::of).orElse(Optional.empty())
                    );

                    cron.syntax.year = String.format(
                            "%d-%d",
                            year.start.orElse(0),
                            year.end.orElse(0)
                    );
                    cron.stack.remove(cron.stack.size() - 1);
                    return;
                }
            }
            if (years.isEmpty()) {
                throw Error.incorrectValue("year", String.format("value %s is not a year format", token));
            }
            cron.syntax.year = years.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
        }

        cron.stack.add(Stack.builder(Kind.Year).build());
    }
}