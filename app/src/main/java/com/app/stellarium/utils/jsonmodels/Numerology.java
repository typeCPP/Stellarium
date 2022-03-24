package com.app.stellarium.utils.jsonmodels;

public class Numerology {
    public String number;
    public String general;
    public String dignities;
    public String disadvantages;
    public String fate;

    public Numerology(String number, String general, String dignities, String disadvantages, String fate) {
        this.number = number;
        this.general = general;
        this.dignities = dignities;
        this.disadvantages = disadvantages;
        this.fate = fate;
    }
}
