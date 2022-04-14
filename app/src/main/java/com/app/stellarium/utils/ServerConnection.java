package com.app.stellarium.utils;

import android.util.Pair;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Scanner;

public class ServerConnection {
    public String serverURL = "http://192.168.0.106:5000/";

    public String getStringResponseByParameters(String params) {
        String result;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(serverURL + params);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(5000);
            urlConnection.setConnectTimeout(5000);
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            Scanner s = new Scanner(in).useDelimiter("\\A");
            result = s.hasNext() ? s.next() : "";
            urlConnection.disconnect();
            return result;
        } catch (Exception e) {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            e.printStackTrace();
            return null;

        }
    }

    public static String getQuery(List<Pair<String, String>> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (Pair<String, String> pair : params) {
            if (first) {
                first = false;
            } else {
                result.append("&");
            }
            result.append(URLEncoder.encode(pair.first, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.second, "UTF-8"));
        }
        return result.toString();
    }
}
