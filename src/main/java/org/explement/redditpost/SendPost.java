package org.explement.redditpost;

import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkAdapter;
import net.dean.jraw.http.OkHttpNetworkAdapter;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.oauth.Credentials;
import net.dean.jraw.oauth.OAuthHelper;
import net.dean.jraw.references.SubmissionReference;

public class SendPost {
    public static void postOnReddit(String link, String message) {
        UserAgent userAgent = new UserAgent( // Agent
             "script", // Type
             "org.example.demo", // Organazation
             "1.0", // Version
             "author" // Author
        );

        Credentials credentials = Credentials.script( // Credentials
            "AUTHOR", // Author
            "PASSWORD", // Password
            "CLIENT ID", // ClientID
            "SECRET" // Secret
        );

        NetworkAdapter adapter = new OkHttpNetworkAdapter(userAgent);
        RedditClient redditClient = OAuthHelper.automatic(adapter, credentials);

        SubmissionReference ref = redditClient.submission(parseId(link));

        ref.reply(message);
    }

    private static String parseId(String link) { // Parses ID
        String[] parts = link.split("/");

        String newLink = parts[6];
        return newLink;
    }
}
