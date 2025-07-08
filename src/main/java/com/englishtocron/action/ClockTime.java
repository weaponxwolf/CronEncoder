package com.englishtocron.action;

import com.englishtocron.Cron;
import com.englishtocron.Error;
import com.englishtocron.Stack;
import com.englishtocron.StartEnd;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClockTime {
    private ClockTime() {
        // Prevent instantiation
    }
    private static final Pattern RE_MATCH = Pattern.compile("(?i)^([0-9]+:)?[0-9]+ *(AM|PM)$|^([0-9]+:[0-9]+)$|(noon|midnight)");
    private static final Pattern RE_HOUR = Pattern.compile("^[0-9]+");
    private static final Pattern RE_MINUTE = Pattern.compile(":[0-9]+");
    private static final Pattern RE_NOON_MIDNIGHT = Pattern.compile("(noon|midnight)");

    public static boolean tryFromToken(String str) {
        return RE_MATCH.matcher(str).matches();
    }

    public static void process(String token, Cron cron) throws Error {
        int hour = 0;
        int minute = 0;

        Matcher hourMatcher = RE_HOUR.matcher(token);
        if (hourMatcher.find()) {
            try {
                hour = Integer.parseInt(hourMatcher.group());
            } catch (NumberFormatException e) {
                throw Error.parseToNumber("clock_time", hourMatcher.group());
            }
        }

        Matcher minuteMatcher = RE_MINUTE.matcher(token);
        if (minuteMatcher.find()) {
            String minuteStr = minuteMatcher.group();
            if (minuteStr.contains(":")) {
                String[] parts = minuteStr.split(":");
                if (parts.length > 1) {
                    try {
                        minute = Integer.parseInt(parts[1]);
                        if (minute >= 60) {
                            throw Error.incorrectValue("clock_time", String.format("minute %d should be lower or equal to 60", minute));
                        }
                    } catch (NumberFormatException e) {
                        throw Error.parseToNumber("clock_time", parts[1]);
                    }
                }
            }
        }

        String lowerCaseToken = token.toLowerCase();
        if (lowerCaseToken.contains("pm")) {
            if (hour < 12) {
                hour += 12;
            } else if (hour > 12) {
                throw Error.incorrectValue("clock_time", String.format("please correct the time before PM. value: %d", hour));
            }
        } else if (lowerCaseToken.contains("am")) {
            if (hour == 12) {
                hour = 0;
            } else if (hour > 12) {
                throw Error.incorrectValue("clock_time", String.format("please correct the time before AM. value: %d", hour));
            }
        }

        if (RE_NOON_MIDNIGHT.matcher(token).matches()) {
            if (token.equals("noon")) {
                hour = 12;
            } else { // midnight
                hour = 0;
            }
            minute = 0;
        }

        if (!cron.stack.isEmpty()) {
            Stack element = cron.stack.get(cron.stack.size() - 1);
            if (element.owner == Kind.RangeStart) {
                element.hour = Optional.of(new StartEnd(Optional.of(hour), Optional.empty()));
                return;
            } else if (element.owner == Kind.RangeEnd) {
                if (element.hour.isPresent()) {
                    StartEnd elementHour = element.hour.get();
                    if (elementHour.start.isPresent() && elementHour.start.get() == hour) {
                        element.min = Optional.of(new StartEnd(Optional.of(hour), Optional.of(hour)));
                        cron.syntax.hour = String.format("%d-%d", hour, hour);
                    } else {
                        elementHour.end = Optional.of(hour);
                        if (element.isAndConnector && !element.isBetweenRange) {
                            if (cron.syntax.hour.contains(",")) {
                                cron.syntax.hour = String.format("%s,%d", cron.syntax.hour, hour);
                            } else {
                                cron.syntax.hour = String.format("%d,%d", elementHour.start.orElse(0), hour);
                            }
                        } else {
                            cron.syntax.hour = String.format("%d-%d", elementHour.start.orElse(0), hour);
                        }
                    }
                }
                return;
            }
        }

        cron.syntax.min = String.valueOf(minute);
        cron.syntax.hour = String.valueOf(hour);

        cron.stack.add(
                Stack.builder(Kind.ClockTime)
                        .hour(new StartEnd(Optional.of(hour), Optional.empty()))
                        .min(new StartEnd(Optional.of(minute), Optional.empty()))
                        .build()
        );
    }
}