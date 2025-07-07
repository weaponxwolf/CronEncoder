package com.englishtocron;

import com.englishtocron.action.Kind;

import java.util.Optional;

public class Stack {
    public Kind owner;
    public Optional<Integer> frequency;
    public Optional<Integer> frequencyEnd;
    public Optional<Integer> frequencyStart;
    public Optional<StartEnd> min;
    public Optional<StartEnd> hour;
    public Optional<StartEndString> day;
    public Optional<StartEndString> month;
    public Optional<StartEnd> year;
    public Optional<String> dayOfWeek;
    public boolean isAndConnector;
    public boolean isBetweenRange;

    private Stack(Kind owner) {
        this.owner = owner;
        this.frequency = Optional.empty();
        this.frequencyEnd = Optional.empty();
        this.frequencyStart = Optional.empty();
        this.min = Optional.empty();
        this.hour = Optional.empty();
        this.day = Optional.empty();
        this.month = Optional.empty();
        this.year = Optional.empty();
        this.dayOfWeek = Optional.empty();
        this.isAndConnector = false;
        this.isBetweenRange = false;
    }

    public static Builder builder(Kind owner) {
        return new Builder(owner);
    }

    public String frequencyToString() {
        return frequency.map(Object::toString).orElse("*");
    }

    @Override
    public String toString() {
        return "Stack{" +
                "owner=" + owner +
                ", frequency=" + frequency +
                ", frequencyEnd=" + frequencyEnd +
                ", frequencyStart=" + frequencyStart +
                ", min=" + min +
                ", hour=" + hour +
                ", day=" + day +
                ", month=" + month +
                ", year=" + year +
                ", dayOfWeek=" + dayOfWeek +
                ", isAndConnector=" + isAndConnector +
                ", isBetweenRange=" + isBetweenRange +
                '}';
    }

    public static class Builder {
        private Stack stack;

        public Builder(Kind owner) {
            stack = new Stack(owner);
        }

        public Builder frequency(int frequency) {
            stack.frequency = Optional.of(frequency);
            return this;
        }

        public Builder min(StartEnd min) {
            stack.min = Optional.of(min);
            return this;
        }

        public Builder hour(StartEnd hour) {
            stack.hour = Optional.of(hour);
            return this;
        }

        public Builder month(StartEndString month) {
            stack.month = Optional.of(month);
            return this;
        }

        public Builder dayOfWeek(String dayOfWeek) {
            stack.dayOfWeek = Optional.of(dayOfWeek);
            return this;
        }

        public Stack build() {
            return stack;
        }
    }
}