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

public class Day {
    private static final Pattern RE_MATCH = Pattern.compile("(?i)^((days|day)|(((monday|tuesday|wednesday|thursday|friday|saturday|sunday|WEEKEND|MON|TUE|WED|THU|FRI|SAT|SUN)( ?and)?,? ?)+))$");
    private static final Pattern RE_DAY = Pattern.compile("(?i)^(day|days)$");
    private static final Pattern RE_WEEKDAYS = Pattern.compile("(?i)(MON|TUE|WED|THU|FRI|SAT|SUN|WEEKEND)");

    private static final String[] WEEK_DAYS = {"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};

    public static boolean tryFromToken(String str) {
        return RE_MATCH.matcher(str).matches();
    }

    public static void process(String token, Cron cron) throws Error {
        if (RE_DAY.matcher(token).matches()) {
            cron.syntax.dayOfWeek = "?";
            if (cron.syntax.min.equals("*")) {
                cron.syntax.min = "0";
            }
            if (cron.syntax.hour.equals("*")) {
                cron.syntax.hour = "0";
            }

            if (!cron.stack.isEmpty()) {
                Stack element = cron.stack.get(cron.stack.size() - 1);
                if (element.owner == Kind.FrequencyOnly) {
                    cron.syntax.dayOfMonth = String.format("*/%s", element.frequencyToString());
                    cron.stack.remove(cron.stack.size() - 1);
                } else if (element.owner == Kind.FrequencyWith) {
                    cron.syntax.dayOfMonth = element.frequencyToString();
                    cron.stack.remove(cron.stack.size() - 1);
                } else {
                    cron.syntax.dayOfMonth = "*";
                }
            } else {
                cron.syntax.dayOfMonth = "*/1";
            }
        } else {
            Matcher matches = RE_WEEKDAYS.matcher(token);
            List<String> days = new ArrayList<>();
            while (matches.find()) {
                days.add(matches.group().toUpperCase());
            }

            if (days.isEmpty()) {
                throw Error.incorrectValue("day", String.format("value %s is not a weekend format", token));
            }

            cron.syntax.dayOfWeek = "";

            if (!cron.stack.isEmpty()) {
                Stack element = cron.stack.get(cron.stack.size() - 1);
                if (element.owner == Kind.RangeStart) {
                    element.day = Optional.of(new StartEndString(
                            days.stream().findFirst(),
                            element.day.flatMap(d -> d.end)
                    ));
                    return;
                } else if (element.owner == Kind.RangeEnd) {
                    StartEndString data = new StartEndString(
                            element.day.flatMap(d -> d.start),
                            days.stream().findFirst()
                    );
                    element.day = Optional.of(data);

                    if (data.start.isPresent() && data.end.isPresent()) {
                        cron.syntax.dayOfWeek = String.format("%s-%s", data.start.get(), data.end.get());
                    } else {
                        throw Error.incorrectValue("day", "Failed to format day of week range");
                    }

                    cron.syntax.dayOfMonth = "?";
                    cron.stack.remove(cron.stack.size() - 1);
                    return;
                } else if (element.owner == Kind.OnlyOn) {
                    String day = days.stream().findFirst().orElseThrow(() ->
                            Error.incorrectValue("day", "Expected at least one day in 'only on' syntax but found none"));
                    cron.syntax.dayOfWeek = day;
                    cron.syntax.dayOfMonth = "?";
                    cron.stack.remove(cron.stack.size() - 1);
                    return;
                }
                cron.stack.clear();
            }

            for (String day : WEEK_DAYS) {
                if (days.contains(day) && !cron.syntax.dayOfWeek.contains(day)) {
                    cron.syntax.dayOfWeek += day + ",";
                }
            }

            if (days.contains("WEEKEND")) {
                for (String day : new String[]{"SAT", "SUN"}) {
                    if (!cron.syntax.dayOfWeek.contains(day)) {
                        cron.syntax.dayOfWeek += day + ",";
                    }
                }
            }

            cron.syntax.dayOfWeek = cron.syntax.dayOfWeek.replaceAll(",$", "");
            cron.syntax.dayOfMonth = "?";
        }

        cron.stack.add(
                Stack.builder(Kind.Day)
                        .dayOfWeek(cron.syntax.dayOfWeek)
                        .build()
        );
    }
}