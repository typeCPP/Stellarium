package com.app.stellarium.utils.jsonmodels;

public class Affirmation {
    public Integer id;
    public String text;
    public String picture;

    public Affirmation(Integer id, String text, String picture) {
        this.id = id;
        this.text = text;
        this.picture = picture;
    }

    @Override
    public String toString() {
        return "Affirmation{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", picture='" + picture + '\'' +
                '}';
    }
}
