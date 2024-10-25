package org.example.soccerscout.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

@Service
public class FbrefService {

    // Method to fetch player ID from FBref
    public String getPlayerId(String playerName) {
        try {
            String searchUrl = "https://fbref.com/en/search/search.fcgi?search=" + playerName.replace(" ", "+");
            Document doc = Jsoup.connect(searchUrl).get();
            Element playerLink = doc.select("a:contains(" + playerName + ")").first();

            if (playerLink != null) {
                String playerUrl = playerLink.attr("href");
                return playerUrl.split("/")[3]; // Extract player_id from URL
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Method to generate the FBref comparison link for two players
    public String generateComparisonLink(String player1Name, String player2Name) {
        String player1Id = getPlayerId(player1Name);
        String player2Id = getPlayerId(player2Name);

        if (player1Id != null && player2Id != null) {
            return "https://fbref.com/en/stathead/player_comparison.cgi?request=1&player_id1=" + player1Id + "&player_id2=" + player2Id;
        }
        return "Player not found or mismatch in player name.";
    }
}
