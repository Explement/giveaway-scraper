package org.explement.getposts;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import org.explement.config;
import org.json.JSONArray;
import org.json.JSONObject;


public class CollectPosts {
    
    private static int limit = config.limit;
    private static String subreddit = config.subreddit;

    public static HashMap<String, String> getPosts() { 
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36"; // https://www.whatsmyua.info/
        
        try {
            // Make URL and Connection
            URL url = new URL("https://www.reddit.com/r/" + subreddit + "/new.json?limit=" + limit);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            
            // Set connection properties
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", userAgent); 
            
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) { // Checks response code is OK
                // Make inputstream and go through it
                BufferedReader inputStream = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = inputStream.readLine()) != null) {
                    response.append(inputLine);
                }

                inputStream.close();
                
                // Get json response and children
                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONObject data = jsonResponse.getJSONObject("data");
                JSONArray posts = data.getJSONArray("children");
                
                // Make a list for all the responses
                HashMap<String, String> allFilteredResponses = new HashMap<>();

                for (int i = 0; i < posts.length(); i++) {
                    // Set all the properties
                    JSONObject post = posts.getJSONObject(i).getJSONObject("data");
                    String title = post.optString("title", "Lacking Title");
                    String description = post.optString("selftext", "Lacking Description");
                    String flair = post.optString("link_flair_text", "CLOSED");
                    String link = post.optString("url", "Missing Link");
                    String author = post.optString("author", "Missing Author");

                    // If post is open then collect the post's properties
                    if (flair.equals("OPEN")) {
                        // Print out all the properties
                        System.out.println("-".repeat(100));
                        System.out.println("Title: " + title);
                        System.out.println("Description: " + description + "\n");
                        System.out.println("Status: " + flair);
                        System.out.println("Link: " + link);
                        System.out.println("Author: " + author);

                        // Collect the filtered response
                        String filterResponse = FilterPosts.FilterPost(title, description, link, author);
                        allFilteredResponses.put(filterResponse, link);
                    }

                }
                return allFilteredResponses;
            } else { // Response code is not OK
                System.out.println("Error: " + responseCode);
            }
        } catch (Exception e) { 
            e.printStackTrace();
        }

        return null;
    }
}