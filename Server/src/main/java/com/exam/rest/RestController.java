package com.exam.rest;

import com.exam.domain.*;
import com.exam.repository.PaperRepository;
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
    private final PaperRepository paperRepo;

    @Autowired
    public RestController(UserRepository userRepository, PaperRepository paperRepository) {
        this.userRepo = userRepository;
        this.paperRepo = paperRepository;
    }

    @GetMapping("/employee/{id}")
    public Iterable<Paper> getReevaluatingPapers(@PathVariable Integer id) {
        List<Paper> papers = new ArrayList<>();
        for (Employee_Paper ep : ((Employee) userRepo.findByID(id)).getPapers()) {
            var employees = ep.getPaper().getEmployees();
            Double grade1 = employees.get(0).getGrade();
            Double grade2 = employees.get(1).getGrade();
            if (!grade1.equals(0.0) && !grade2.equals(0.0) && abs(grade1 - grade2) > 1)
                papers.add(ep.getPaper());
        }
        return papers;
    }

    @GetMapping("/paper/{id}")
    public Paper getEmployees(@PathVariable Integer id) {
        return paperRepo.findByID(id);
    }
}
