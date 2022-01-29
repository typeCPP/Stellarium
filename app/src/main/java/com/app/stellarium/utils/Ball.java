package com.app.stellarium.utils;

import java.util.Random;

public class Ball extends Object {
    private Random random;
    private String[] responses = {
            "It is certain",
            "It is decidedly so",
            "Without a doubt",
            "Yes definitely",
            "You may rely on it",
            "As I see it yes",
            "Most likely",
            "Outlook good",
            "Yes",
            "Signs point to yes",
            "Reply hazy try again",
            "Ask again later",
            "Better not tell you now",
            "Cannot predict now",
            "Concentrate and ask again",
            "Don't count on it",
            "My reply is no",
            "My sources say no",
            "Outlook not so good",
            "Very doubtful",
    };

    public Ball() {
        super();
        this.random = new Random();
    }
    public String shake() {
        final int responseCode = this.random.nextInt(responses.length);
        return responses[responseCode];
    }
}