package com.app.stellarium.utils.jsonmodels;

public class User {
    public Integer id;
    public String name;
    public Integer mail_confirm;
    public String date;
    public Integer sex;
    public Integer sign;

    public User(Integer id, String name, Integer mail_confirm, String date, Integer sex, Integer sign) {
        this.id = id;
        this.name = name;
        this.mail_confirm = mail_confirm;
        this.date = date;
        this.sex = sex;
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", mail_confirm=" + mail_confirm +
                ", date='" + date + '\'' +
                ", sex=" + sex +
                ", sign=" + sign +
                '}';
    }
}
