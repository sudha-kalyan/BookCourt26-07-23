package com.CourtReserve.app.controllers;

import com.CourtReserve.app.models.Game;
import com.CourtReserve.app.repositories.GameRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GameController {
    private final GameRepository gamesRepository;

    public GameController(GameRepository gamesRepository) {
        this.gamesRepository = gamesRepository;
    }

    @RequestMapping("/games")
    public String showGames(Model model){
        Iterable<Game> games = gamesRepository.findAll();
        model.addAttribute("games", games);

        return "masters/game";
    }
    @PostMapping("/games")
    public String addGames(@ModelAttribute Game game, Model model){
        gamesRepository.save(game);

        return "redirect:/games";
    }
}
