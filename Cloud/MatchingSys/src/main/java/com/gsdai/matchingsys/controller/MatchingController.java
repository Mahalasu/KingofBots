package com.gsdai.matchingsys.controller;

import com.gsdai.matchingsys.service.MatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class MatchingController {

    @Autowired
    private MatchingService matchingService;

    @PostMapping("/player/add")
    public String addPlayer(@RequestParam MultiValueMap<String,String> data) {
        Integer userId = Integer.parseInt(Objects.requireNonNull(data.getFirst("userId")));
        Integer marks = Integer.parseInt(Objects.requireNonNull(data.getFirst("marks")));
        Integer botId = Integer.parseInt(Objects.requireNonNull(data.getFirst("botId")));
        return matchingService.addPlayer(userId, marks, botId);
    }

    @PostMapping("/player/delete")
    public String deletePlayer(@RequestParam MultiValueMap<String,String> data) {
        Integer userId = Integer.parseInt(Objects.requireNonNull(data.getFirst("userId")));
        return matchingService.deletePlayer(userId);
    }
}
