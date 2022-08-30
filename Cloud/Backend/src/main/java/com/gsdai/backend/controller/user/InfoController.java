package com.gsdai.backend.controller.user;

import com.gsdai.backend.service.user.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class InfoController {

    @Autowired
    private InfoService infoService;

    @GetMapping("/api/user/info")
    public Map<String, String> getInfo() {
        return infoService.getInfo();
    }
}
