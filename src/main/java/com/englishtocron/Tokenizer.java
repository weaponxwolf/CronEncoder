package com.englishtocron;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {
    private static final Pattern RE_TOKENS = Pattern.compile(
            "(?i)(?:seconds|second|secs|sec)|(?:hours?|hrs?)|(?:minutes?|mins?|min)|(?:months?|(?:january|february|march|april|may|june|july|august|september|october|november|december|jan|feb|mar|apr|may|jun|jul|aug|sept|oct|nov|dec)(?: ?and)?,? ?)+|[0-9]+(?:th|nd|rd|st)|(?:[0-9]+:)?[0-9]+ ?(?:am|pm)|[0-9]+:[0-9]+|(?:noon|midnight)|(?:days?|(?:monday|tuesday|wednesday|thursday|friday|saturday|sunday|weekend|mon|tue|wed|thu|fri|sat|sun)(?: ?and)?,? ?)+|(?:[0-9]{4}[0-9]*(?: ?and)?,? ?)+|[0-9]+|(?:only on)|(?:to|through|ending|end|and)|(?:between|starting|start)"
    );

    private final Pattern regex;

    public Tokenizer() {
        this.regex = RE_TOKENS;
    }

    public List<String> run(String inputString) {
        String processedInput = inputString.replace(", ", " and ");

        // Handle "only on" followed by day names as a special pattern
        if (processedInput.contains("only on")) {
            processedInput = processedInput.replace(" and only on", " only on");
        }

        List<String> matches = new ArrayList<>();
        Matcher matcher = regex.matcher(processedInput);
        while (matcher.find()) {
            matches.add(matcher.group().trim());
        }
        return matches;
    }
}