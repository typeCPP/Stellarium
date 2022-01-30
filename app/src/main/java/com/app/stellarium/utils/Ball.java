package com.app.stellarium.utils;

import java.util.Random;

public class Ball extends Object {
    private Random random;
    private String[] responses = {
            "Да",
            "Нет",
            "Возможно",
            "Маловероятно",
            "Скорее всего",
            "Невозможно предсказать",
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