package com.app.stellarium.utils.jsonmodels;

public class Horoscope {
    public class Info {
        public Integer id;
        public String name;

        @Override
        public String toString() {
            return "Info{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    public class Prediction {
        public String love;
        public String common;
        public String health;
        public String business;

        @Override
        public String toString() {
            return "Prediction{" +
                    "love='" + love + '\'' +
                    ", common='" + common + '\'' +
                    ", health='" + health + '\'' +
                    ", business='" + business + '\'' +
                    '}';
        }
    }

    public class Character {
        public String description;
        public String charact;
        public String love;
        public String career;

        @Override
        public String toString() {
            return "Character{" +
                    "description='" + description + '\'' +
                    ", charact='" + charact + '\'' +
                    ", love='" + love + '\'' +
                    ", career='" + career + '\'' +
                    '}';
        }
    }

    public Info info;
    public Prediction today;
    public Prediction tomorrow;
    public Prediction week;
    public Prediction month;
    public Prediction year;
    public Character character;

    public Horoscope(Info info, Prediction today, Prediction tomorrow, Prediction week, Prediction month, Prediction year, Character character) {
        this.info = info;
        this.today = today;
        this.tomorrow = tomorrow;
        this.week = week;
        this.month = month;
        this.year = year;
        this.character = character;
    }

    @Override
    public String toString() {
        return "Horoscope{" +
                "info=" + info +
                ", today=" + today +
                ", tomorrow=" + tomorrow +
                ", week=" + week +
                ", month=" + month +
                ", year=" + year +
                ", character=" + character +
                '}';
    }
}
