package com.englishtocron;

import java.util.Objects;
import java.util.Optional;

public class StartEndString {
    public Optional<String> start;
    public Optional<String> end;

    public StartEndString(Optional<String> start, Optional<String> end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return "StartEndString{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StartEndString that = (StartEndString) o;
        return Objects.equals(start, that.start) &&
                Objects.equals(end, that.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }
}