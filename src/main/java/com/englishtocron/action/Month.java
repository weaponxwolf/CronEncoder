package com.englishtocron.action;

import com.englishtocron.Cron;
import com.englishtocron.Error;
import com.englishtocron.Stack;
import com.englishtocron.StartEndString;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Month {
    private Month() {
        // Prevent instantiation
    }
    private static final Pattern RE_MATCH = Pattern.compile("(?i)^((months|month)|(((january|february|march|april|may|june|july|august|september|october|november|december|JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEPT|OCT|NOV|DEC)( ?and)?,? ?)+))$");
    private static final Pattern RE_MONTH = Pattern.compile("(?i)^(month|months)$");
    private static final Pattern RE_MONTHS_ABBREVIATION = Pattern.compile("(?i)(JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)");

    private static final String[] MONTHS = {
            "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"
    };

    public static boolean tryFromToken(String str) {
        return RE_MATCH.matcher(str).matches();
    }

    public static void process(String token, Cron cron) throws Error {
        if (RE_MONTH.matcher(token).matches()) {
            if (!cron.stack.isEmpty()) {
                Stack element = cron.stack.get(cron.stack.size() - 1);
                if (element.owner == Kind.FrequencyOnly || element.owner == Kind.FrequencyWith) {
                    cron.syntax.month = element.frequencyToString();
                    cron.stack.remove(cron.stack.size() - 1);
                } else if (element.owner == Kind.RangeEnd) {
                    if (element.frequencyEnd.isPresent()) {
                        cron.syntax.dayOfMonth = String.format(
                                "%d,%d",
                                element.frequencyStart.orElse(0),
                                element.frequencyEnd.get()
                        );
                    }
                } else {
                    cron.syntax.month = "*";
                }
            } else {
                cron.syntax.month = "*";
            }
        } else {
            Matcher matches = RE_MONTHS_ABBREVIATION.matcher(token);
            List<String> months = new ArrayList<>();
            while (matches.find()) {
                months.add(matches.group().toUpperCase());
            }

            if (months.isEmpty()) {
                throw Error.incorrectValue("month", String.format("value %s is not a month format", token));
            }

            cron.syntax.month = "";

            if (!cron.stack.isEmpty()) {
                Stack element = cron.stack.get(cron.stack.size() - 1);
                if (element.owner == Kind.FrequencyOnly || element.owner == Kind.FrequencyWith) {
                    cron.syntax.dayOfMonth = element.frequencyToString();
                    cron.stack.remove(cron.stack.size() - 1);
                } else if (element.owner == Kind.RangeStart) {
                    element.month = Optional.of(new StartEndString(
                            months.stream().findFirst(),
                            element.month.flatMap(m -> m.end)
                    ));
                    cron.stack.remove(cron.stack.size() - 1);
                    return;
                } else if (element.owner == Kind.RangeEnd) {
                    if (element.frequencyEnd.isPresent()) {
                        cron.syntax.dayOfWeek = "?";
                        if (element.frequencyStart.isPresent()) {
                            cron.syntax.dayOfMonth = String.format("%d-%d", element.frequencyStart.get(), element.frequencyEnd.get());
                        }
                    }

                    StartEndString data = new StartEndString(
                            element.month.flatMap(m -> m.start),
                            months.stream().findFirst()
                    );
                    element.month = Optional.of(data);

                    if (data.start.isPresent() && data.end.isPresent()) {
                        cron.syntax.month = String.format("%s-%s", data.start.get(), data.end.get());
                    }
                    cron.stack.remove(cron.stack.size() - 1);
                    return;
                } else {
                    cron.stack.remove(cron.stack.size() - 1);
                }
            }

            for (String month : MONTHS) {
                if (months.contains(month) && !cron.syntax.month.contains(month)) {
                    cron.syntax.month += month + ",";
                }
            }
            cron.syntax.month = cron.syntax.month.replaceAll(",$", "");
        }

        cron.stack.add(
                Stack.builder(Kind.Month)
                        .month(new StartEndString(Optional.of(cron.syntax.month), Optional.empty()))
                        .build()
        );
    }
}