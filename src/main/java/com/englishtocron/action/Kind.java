package com.englishtocron.action;

public enum Kind {
    FrequencyWith,
    FrequencyOnly,
    ClockTime,
    Day,
    Second,
    Minute,
    Hour,
    Month,
    Year,
    RangeStart,
    RangeEnd,
    OnlyOn;

    public static Kind[] iterator() {
        return new Kind[]{
                FrequencyWith,
                FrequencyOnly,
                ClockTime,
                Day,
                Second,
                Minute,
                Hour,
                Month,
                Year,
                RangeStart,
                RangeEnd,
                OnlyOn
        };
    }
}