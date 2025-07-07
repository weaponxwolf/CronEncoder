package com.englishtocron.action;

import com.englishtocron.Cron;
import com.englishtocron.Stack;

import java.util.regex.Pattern;

public class Seconds {
    private static final Pattern RE_MATCH = Pattern.compile("(?i)(seconds|second|sec|secs)");
    private static final Pattern RE_SECUND = Pattern.compile("^(seconds|second|sec|secs)$");

    public static boolean tryFromToken(String str) {
        return RE_MATCH.matcher(str).matches();
    }

    public static void process(String token, Cron cron) {
        if (RE_SECUND.matcher(token).matches()) {
            if (!cron.stack.isEmpty()) {
                Stack element = cron.stack.get(cron.stack.size() - 1);
                if (element.owner == Kind.FrequencyOnly) {
                    cron.syntax.seconds = String.format("0/%s", element.frequencyToString());
                    cron.stack.remove(cron.stack.size() - 1);
                } else if (element.owner == Kind.FrequencyWith) {
                    cron.syntax.seconds = element.frequencyToString();
                    cron.stack.remove(cron.stack.size() - 1);
                }
            } else {
                cron.syntax.seconds = "*";
            }
            cron.stack.add(Stack.builder(Kind.Second).build());
        }
    }
}