package org.explement.getposts;

import java.util.Arrays;

import org.explement.config;

public class FilterPosts {
    private static String[] filter = config.filter;
    private static String[] usernameFilter = config.usernameFilter;

    public static String FilterPost(String title, String description, String link, String author) {

        // Make a basic stream and check if the author is in the usernameFilter, then return true if it is
        if (Arrays.stream(usernameFilter).anyMatch(s -> s.equals(author))) return "";

        // Check if the title or description contains any of the filter words and then return them
        return Arrays.stream(filter)
                .filter(s -> title.toLowerCase().contains(s.toLowerCase()) || description.toLowerCase().contains(s.toLowerCase()))
                .map(s -> "'" + s + "'" + " was found in the following post: " + link)
                .findFirst()
                .orElse("");
                
    }
}
