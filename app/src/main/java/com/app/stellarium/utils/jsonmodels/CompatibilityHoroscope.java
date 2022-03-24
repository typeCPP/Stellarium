package com.app.stellarium.utils.jsonmodels;

public class CompatibilityHoroscope {
    public String love_text;
    public String sex_text;
    public String marriage_text;
    public String friend_text;

    public int love_val;
    public int sex_val;
    public int marriage_val;
    public int friend_val;

    public CompatibilityHoroscope(String love_text, String sex_text, String marriage_text, String friend_text, int love_val, int sex_val, int marriage_val, int friend_val) {
        this.love_text = love_text;
        this.sex_text = sex_text;
        this.marriage_text = marriage_text;
        this.friend_text = friend_text;
        this.love_val = love_val;
        this.sex_val = sex_val;
        this.marriage_val = marriage_val;
        this.friend_val = friend_val;
    }

    @Override
    public String toString() {
        return "CompatibilityHoroscope{" +
                "love_text='" + love_text + '\'' +
                ", sex_text='" + sex_text + '\'' +
                ", marriage_text='" + marriage_text + '\'' +
                ", friend_text='" + friend_text + '\'' +
                ", love_val=" + love_val +
                ", sex_val=" + sex_val +
                ", marriage_val=" + marriage_val +
                ", friend_val=" + friend_val +
                '}';
    }
}
