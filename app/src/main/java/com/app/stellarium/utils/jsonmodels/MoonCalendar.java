package com.app.stellarium.utils.jsonmodels;

public class MoonCalendar {
    public String date;
    public String phase;
    public String characteristics;
    public String health;
    public String relations;
    public String business;

    public MoonCalendar(String date, String phase, String characteristics, String health, String relations, String business) {
        this.date = date;
        this.phase = phase;
        this.characteristics = characteristics;
        this.health = health;
        this.relations = relations;
        this.business = business;
    }

    @Override
    public String toString() {
        return "MoonCalendar{" +
                "date='" + date + '\'' +
                ", phase='" + phase + '\'' +
                ", characteristics='" + characteristics + '\'' +
                ", health='" + health + '\'' +
                ", relations='" + relations + '\'' +
                ", business='" + business + '\'' +
                '}';
    }
}
