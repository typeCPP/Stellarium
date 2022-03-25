package com.app.stellarium.utils;

public class ZodiacSignUtils {
    public static int getUserSignID(String dateOfBirth) {
        String[] dob = dateOfBirth.split("/", 3);
        int day = Integer.parseInt(dob[0]);
        int month = Integer.parseInt(dob[1]);
        int signId = 1;
        if ((month == 3 && day >= 21) || (month == 4 && day <= 20)) {
            signId = 1;
        } else if ((month == 4 && day >= 21) || (month == 5 && day <= 21)) {
            signId = 2;
        } else if ((month == 5 && day >= 22) || (month == 6 && day <= 21)) {
            signId = 3;
        } else if ((month == 6 && day >= 22) || (month == 7 && day <= 22)) {
            signId = 4;
        } else if ((month == 7 && day >= 23) || (month == 8 && day <= 23)) {
            signId = 5;
        } else if ((month == 8 && day >= 24) || (month == 9 && day <= 23)) {
            signId = 6;
        } else if ((month == 9 && day >= 24) || (month == 10 && day <= 23)) {
            signId = 7;
        } else if ((month == 10 && day >= 24) || (month == 11 && day <= 22)) {
            signId = 8;
        } else if ((month == 11 && day >= 23) || (month == 12 && day <= 22)) {
            signId = 9;
        } else if ((month == 12 && day >= 23) || (month == 1 && day <= 20)) {
            signId = 10;
        } else if ((month == 1 && day >= 21) || (month == 2 && day <= 19)) {
            signId = 11;
        } else if ((month == 2 && day >= 20) || (month == 3 && day <= 20)) {
            signId = 12;
        }
        return signId;
    }
}
