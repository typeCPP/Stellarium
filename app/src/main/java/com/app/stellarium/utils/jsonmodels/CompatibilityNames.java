package com.app.stellarium.utils.jsonmodels;

public class CompatibilityNames {
    public int hashedId;
    public String love_text;
    public String friend_text;
    public String job_text;

    public int love_val;
    public int friend_val;
    public int job_val;

    public CompatibilityNames(int hashedId, String love_text, String friend_text, String job_text, int love_val, int friend_val, int job_val) {
        this.hashedId = hashedId;
        this.love_text = love_text;
        this.friend_text = friend_text;
        this.job_text = job_text;
        this.love_val = love_val;
        this.friend_val = friend_val;
        this.job_val = job_val;
    }

    @Override
    public String toString() {
        return "CompatibilityNames{" +
                "love_text='" + love_text + '\'' +
                ", friend_text='" + friend_text + '\'' +
                ", job_text='" + job_text + '\'' +
                ", love_val=" + love_val +
                ", friend_val=" + friend_val +
                ", job_val=" + job_val +
                '}';
    }
}
