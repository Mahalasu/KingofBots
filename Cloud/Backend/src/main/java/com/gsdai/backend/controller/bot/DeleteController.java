package com.gsdai.backend.controller.bot;

import com.gsdai.backend.service.bot.DeleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class DeleteController {

    @Autowired
    private DeleteService deleteService;

    @PostMapping("/api/user/bot/delete")
    public Map<String, String> delete(@RequestParam Map<String, String> data) {
        return deleteService.delete(data);
    }
}
