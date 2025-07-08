package com.englishtocron;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {
    // Enhanced regex pattern
    private static final Pattern RE_TOKENS = Pattern.compile(
        "(?i)" +
        // Time units
        "(?:seconds|second|secs|sec)|" +
        "(?:hours?|hrs?)|" +
        "(?:minutes?|mins?|min)|" +

        // Months
        "(?:months?|(?:january|february|march|april|may|june|july|august|september|october|november|december|" +
        "jan|feb|mar|apr|may|jun|jul|aug|sept|oct|nov|dec)(?: ?and)?,? ?)+|" +

        // Ordinals
        "[0-9]+(?:th|nd|rd|st)|" +

        // Clock times
        "(?:[0-9]+:)?[0-9]+ ?(?:am|pm)|" +
        "[0-9]+:[0-9]+|" +

        // Special times
        "(?:noon|midnight)|" +

        // Days
        "(?:days?|(?:monday|tuesday|wednesday|thursday|friday|saturday|sunday|weekend|" +
        "mon|tue|wed|thu|fri|sat|sun)(?: ?and)?,? ?)+|" +

        // Range expressions: from <year> to <year>
        "(?:from\\s*[0-9]{4}\\s*to\\s*[0-9]{4})|" +
        "(?:between\\s*[0-9]{4}\\s*and\\s*[0-9]{4})|" +
        "(?:starting\\s*[0-9]{4}\\s*and\\s*(?:ending|end)\\s*[0-9]{4})|" +

        // in year / on years etc.
        "(?:in|on|during)\\s*(?:year|years)?\\s*[0-9]{4}(?:\\s*(?:and|,)?\\s*[0-9]{4})*|" +

        // Single 4-digit year
        "[0-9]{4}|" +

        // General numbers
        "[0-9]+|" +

        // Keywords
        "(?:only on)|" +
        "(?:to|through|ending|end|and)|" +
        "(?:between|starting|start)"
    );

    private final Pattern regex;

    public Tokenizer() {
        this.regex = RE_TOKENS;
    }

    public List<String> run(String inputString) {
        String processedInput = inputString.replace(", ", " and ");

        // Normalize "only on"
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
