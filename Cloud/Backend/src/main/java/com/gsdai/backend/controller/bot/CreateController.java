package com.gsdai.backend.controller.bot;

import com.gsdai.backend.service.bot.CreateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class CreateController {

    @Autowired
    private CreateService createService;

    @PostMapping("/api/user/bot/create")
    public Map<String, String> create(@RequestParam Map<String, String> data) {
        return createService.create(data);
    }

}
