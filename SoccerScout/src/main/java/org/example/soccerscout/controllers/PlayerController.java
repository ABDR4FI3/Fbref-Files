package org.example.soccerscout.controllers;

import org.example.soccerscout.services.DataFetchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.example.soccerscout.services.PlayerService;
import tech.tablesaw.api.Table;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/player")
public class PlayerController {

    @Autowired
    private PlayerService playerService;
    @Autowired
    private DataFetchService dataFetchService;

    @GetMapping("/Link")
    public String generateComparisonLink(@RequestParam(value = "player1") String player1, @RequestParam("player2") String player2) {
        return playerService.generateComparisonLink(player1, player2);
    }
    @GetMapping("/compare")
    public List<Map<String, String>> comparePlayers(@RequestParam(value = "player1") String player1, @RequestParam("player2") String player2) {
        return  dataFetchService.fetchComparisonData(player1, player2);
    }
    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }
}
