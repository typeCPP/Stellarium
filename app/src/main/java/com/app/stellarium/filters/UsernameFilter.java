package com.app.stellarium.filters;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Pattern;

public class UsernameFilter implements InputFilter {
    @Override
    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {
        StringBuilder builder = new StringBuilder();
        for (int i = start; i < end; i++) {
            char c = source.charAt(i);
            if (Pattern.matches("[\\w\\Q-._ \\E]", new StringBuilder(1).append(c))) {
                builder.append(c);
            }
        }
        boolean allCharactersValid = (builder.length() == end - start);
        return allCharactersValid ? null : builder.toString();
    }
}
