package com.gsdai.backend.controller.bot;

import com.gsdai.backend.domain.Bot;
import com.gsdai.backend.service.bot.GetListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GetListController {

    @Autowired
    private GetListService getListService;

    @GetMapping("/api/user/bot/list")
    public List<Bot> getList() {
        return getListService.getList();
    }
}
