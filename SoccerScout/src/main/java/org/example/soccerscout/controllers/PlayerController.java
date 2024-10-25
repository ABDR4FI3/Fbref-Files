package org.example.soccerscout.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.example.soccerscout.services.PlayerService;

@RestController
@RequestMapping("/player")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @GetMapping("/compare")
    public String comparePlayers(@RequestParam(value = "player1") String player1, @RequestParam("player2") String player2) {
        return playerService.generateComparisonLink(player1, player2);
    }
    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }
}
