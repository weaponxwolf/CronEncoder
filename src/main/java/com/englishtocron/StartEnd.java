package com.englishtocron;

import java.util.Objects;
import java.util.Optional;

public class StartEnd {
    public Optional<Integer> start;
    public Optional<Integer> end;

    public StartEnd(Optional<Integer> start, Optional<Integer> end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return "StartEnd{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StartEnd startEnd = (StartEnd) o;
        return Objects.equals(start, startEnd.start) &&
                Objects.equals(end, startEnd.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }
}