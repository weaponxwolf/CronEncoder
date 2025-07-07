package com.englishtocron.action;

import com.englishtocron.Cron;
import com.englishtocron.Stack;

import java.util.regex.Pattern;

public class RangeStart {
    private static final Pattern RE_MATCH = Pattern.compile("(?i)(between|starting|start)");
    private static final Pattern RE_MATCH_BETWEEN = Pattern.compile("(?i)(between)");

    public static boolean tryFromToken(String str) {
        return RE_MATCH.matcher(str).matches();
    }

    public static void process(String token, Cron cron) {
        Stack stack = Stack.builder(Kind.RangeStart).build();
        stack.isBetweenRange = RE_MATCH_BETWEEN.matcher(token).matches();
        cron.stack.add(stack);
    }
}