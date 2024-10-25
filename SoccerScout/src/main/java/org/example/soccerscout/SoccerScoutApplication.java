package org.example.soccerscout;

import org.example.soccerscout.services.DataFetchService;
import org.example.soccerscout.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tech.tablesaw.api.Table;

import java.util.List;
import java.util.Map;

@SpringBootApplication
public class SoccerScoutApplication implements CommandLineRunner {
    @Autowired
    private PlayerService playerService;
    @Autowired
    private DataFetchService dataFetchService;

    private static String player1 = "Kylian Mbappe";
    private static String player2 = "Lionel Messi";


    public static void main(String[] args)  {
        SpringApplication.run(SoccerScoutApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        //String link = playerService.generateComparisonLink(player1, player2);
        //System.out.println(link);
        //List<Map<String, String>> table = dataFetchService.fetchComparisonData(player1, player2);
        //System.out.println(table);
    }
}
