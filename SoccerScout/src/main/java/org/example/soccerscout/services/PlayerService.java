package org.example.soccerscout.services;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PlayerService {

    private static final String FBREF_SEARCH_URL = "https://fbref.com/en/search/search.fcgi?search=";

    public String getPlayerId(String playerName) {
        try {
            // Replace spaces in the player name for URL
            String searchUrl = FBREF_SEARCH_URL + playerName.replace(" ", "+");
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(searchUrl, String.class);

            // Parse the HTML using Jsoup
            Document document = Jsoup.parse(response);
            // Use a more general selector to find the player link
            Element playerLink = document.selectFirst("a[href*='/en/players/']");
            System.out.println("playerLink " + playerLink);

            if (playerLink != null) {
                String playerUrl = playerLink.attr("href");
                String[] urlParts = playerUrl.split("/");
                return urlParts[3];  // Extract player_id from URL
            } else {
                System.out.println("Player link not found for: " + playerName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String generateComparisonLink(String player1Name, String player2Name) {
        String player1Id = getPlayerId(player1Name);
        String player2Id = getPlayerId(player2Name);

        if (player1Id != null && player2Id != null) {
            return String.format(
                    "https://fbref.com/en/stathead/player_comparison.cgi?request=1&player_id1=%s&player_id2=%s",
                    player1Id, player2Id
            );
        }
        return "Player not found or mismatch in player name.";
    }
}
