package com.gsdai.backend.controller.bot;

import com.gsdai.backend.domain.Bot;
import com.gsdai.backend.service.bot.GetInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class GetInfoController {

    @Autowired
    private GetInfoService getInfoService;

    @GetMapping("/api/user/bot/info")
    public Bot getInfo(@RequestParam Map<String, String> data) {
        return getInfoService.getInfo(data);
    }
}
