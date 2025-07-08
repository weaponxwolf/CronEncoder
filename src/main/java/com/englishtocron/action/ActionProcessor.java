package com.englishtocron.action;

import com.englishtocron.Cron;
import com.englishtocron.Error;

public class ActionProcessor {

    private ActionProcessor() {
        // Prevent instantiation
    }

    public static Kind tryFromToken(String token) {
        for (Kind stateKind : Kind.iterator()) {
            boolean isMatch = false;
            switch (stateKind) {
                case FrequencyWith:
                    isMatch = FrequencyWith.tryFromToken(token);
                    break;
                case FrequencyOnly:
                    isMatch = FrequencyOnly.tryFromToken(token);
                    break;
                case ClockTime:
                    isMatch = ClockTime.tryFromToken(token);
                    break;
                case Day:
                    isMatch = Day.tryFromToken(token);
                    break;
                case Second:
                    isMatch = Seconds.tryFromToken(token);
                    break;
                case Minute:
                    isMatch = Minute.tryFromToken(token);
                    break;
                case Hour:
                    isMatch = Hour.tryFromToken(token);
                    break;
                case Month:
                    isMatch = Month.tryFromToken(token);
                    break;
                case Year:
                    isMatch = Year.tryFromToken(token);
                    break;
                case RangeStart:
                    isMatch = RangeStart.tryFromToken(token);
                    break;
                case RangeEnd:
                    isMatch = RangeEnd.tryFromToken(token);
                    break;
                case OnlyOn:
                    isMatch = token.equalsIgnoreCase("only on");
                    break;
            }
            if (isMatch) {
                return stateKind;
            }
        }
        return null;
    }

    public static void process(Kind kind, String token, Cron cron) throws Error {
        switch (kind) {
            case FrequencyWith:
                FrequencyWith.process(token, cron);
                break;
            case FrequencyOnly:
                try {
                    int frequency = Integer.parseInt(token);
                    FrequencyOnly.process(frequency, cron);
                } catch (NumberFormatException e) {
                    throw Error.parseToNumber("frequency_only", token);
                }
                break;
            case ClockTime:
                ClockTime.process(token, cron);
                break;
            case Day:
                Day.process(token, cron);
                break;
            case Second:
                Seconds.process(token, cron);
                break;
            case Minute:
                Minute.process(token, cron);
                break;
            case Hour:
                Hour.process(token, cron);
                break;
            case Month:
                Month.process(token, cron);
                break;
            case Year:
                Year.process(token, cron);
                break;
            case RangeStart:
                RangeStart.process(token, cron);
                break;
            case RangeEnd:
                RangeEnd.process(token, cron);
                break;
            case OnlyOn:
                // When "only on" is encountered, we don't need to do anything special
                // The next token should be a day, which will be handled correctly
                break;
        }
    }
}