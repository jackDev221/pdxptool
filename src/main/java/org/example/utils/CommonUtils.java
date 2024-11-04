package org.example.utils;

import java.net.URL;

public class CommonUtils {
    public static boolean isValidUrl(String url) {
        try {
            new URL(url);
            return true;
        } catch (java.net.MalformedURLException e) {
            return false;
        }
    }
}
