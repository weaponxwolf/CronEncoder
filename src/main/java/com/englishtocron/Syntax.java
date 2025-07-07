package com.englishtocron;

public class Syntax {
    public String seconds;
    public String min;
    public String hour;
    public String dayOfMonth;
    public String dayOfWeek;
    public String month;
    public String year;

    public Syntax() {
        this.seconds = "0";
        this.min = "*";
        this.hour = "*";
        this.dayOfMonth = "*";
        this.dayOfWeek = "?";
        this.month = "*";
        this.year = "*";
    }

    @Override
    public String toString() {
        return String.format("%s %s %s %s %s %s %s",
                seconds.trim(),
                min.trim(),
                hour.trim(),
                dayOfMonth.trim(),
                month.trim(),
                dayOfWeek.trim(),
                year.trim());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Syntax syntax = (Syntax) o;
        return seconds.equals(syntax.seconds) &&
                min.equals(syntax.min) &&
                hour.equals(syntax.hour) &&
                dayOfMonth.equals(syntax.dayOfMonth) &&
                dayOfWeek.equals(syntax.dayOfWeek) &&
                month.equals(syntax.month) &&
                year.equals(syntax.year);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(seconds, min, hour, dayOfMonth, dayOfWeek, month, year);
    }
}