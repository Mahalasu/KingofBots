package com.gsdai.backend.controller.bot;

import com.gsdai.backend.service.bot.EditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class EditController {

    @Autowired
    private EditService editService;

    @PostMapping("/api/user/bot/edit")
    public Map<String, String> update(@RequestParam Map<String, String> data) {
        return editService.edit(data);
    }
}
