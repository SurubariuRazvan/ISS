package com.exam.rest;

import com.exam.domain.Category;
import com.exam.domain.Round;
import com.exam.domain.Word;
import com.exam.repository.GameRepository;
import com.exam.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/exam")
public class RestController {
    private final UserRepository userRepo;
    private final GameRepository gameRepo;

    @Autowired
    public RestController(UserRepository userRepository, GameRepository gameRepository) {
        this.userRepo = userRepository;
        this.gameRepo = gameRepository;
    }

    @GetMapping("/game/{id}")
    public Iterable<String> getCategoriesForGame(@PathVariable Integer id) {
        List<String> categories = new ArrayList<>();
        if (gameRepo.findByID(id) != null)
            for (Round round : gameRepo.findByID(id).getRounds())
                categories.add(round.getCategory().toString());
        return categories;
    }

    @GetMapping("/words")
    public Iterable<String> getWordsForGameWithCategory(@RequestParam("gameID") Integer id, @RequestParam("category") Category category) {
        List<String> words = new ArrayList<>();
        if (gameRepo.findByID(id) != null)
            for (Round round : gameRepo.findByID(id).getRounds())
                if (round.getCategory().equals(category))
                    for (Word word : round.getWords())
                        words.add(word.getWord());
        return words;
    }

}
