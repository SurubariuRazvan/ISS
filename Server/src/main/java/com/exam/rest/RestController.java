package com.exam.rest;

import com.exam.domain.*;
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
    private final GameRepository gameRepo;

    @Autowired
    public RestController(GameRepository gameRepository) {
        this.gameRepo = gameRepository;
    }

    @GetMapping("/game/{id}")
    public Iterable<StudentDTO> getGameAndBombs(@PathVariable Integer id) {
        List<StudentDTO> result = new ArrayList<>();
        if (gameRepo.findByID(id) != null)
            for (Round round : gameRepo.findByID(id).getRounds())
                for (var a : round.getWords()) {
                    result.add(new StudentDTO(a.getStudent(), a.getBomb1(), a.getBomb2()));
                    break;
                }
        return result;
    }

    @GetMapping("")
    public Iterable<Word> getWordsForGameWithCategory(@RequestParam("gameID") Integer gameID, @RequestParam("studentID") Integer studentID) {
        List<Word> result = new ArrayList<>();
        if (gameRepo.findByID(gameID) != null)
            for (Round round : gameRepo.findByID(gameID).getRounds())
                for (var a : round.getWords())
                    if (a.getStudent().getId().equals(studentID))
                        result.add(a);
        return result;
    }

}
