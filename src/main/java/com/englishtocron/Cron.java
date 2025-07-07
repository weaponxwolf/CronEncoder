package com.englishtocron;

import com.englishtocron.action.Kind;
import com.englishtocron.action.ActionProcessor;

import java.util.ArrayList;
import java.util.List;

public class Cron {
    public Syntax syntax;
    public List<Stack> stack;

    public Cron() {
        this.syntax = new Syntax();
        this.stack = new ArrayList<>();
    }

    public static Cron fromString(String text) throws Error {
        Tokenizer tokenizer = new Tokenizer();
        List<String> tokens = tokenizer.run(text);

        if (tokens.isEmpty()) {
            throw Error.invalidInput();
        }

        Cron cron = new Cron();
        for (String token : tokens) {
            Kind state = ActionProcessor.tryFromToken(token);
            if (state != null) {
                ActionProcessor.process(state, token, cron);
            }
        }
        return cron;
    }

    public String format() {
        return this.toString();
    }

    @Override
    public String toString() {
        return syntax.toString();
    }
}