package com.gsdai.backend.controller.rank;

import com.alibaba.fastjson.JSONObject;
import com.gsdai.backend.service.rank.GetRankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class GetRankController {

    @Autowired
    private GetRankService getRankService;

    @GetMapping("/api/rank/getlist")
    public JSONObject getList(@RequestParam Map<String, String> data) {
        Integer page = Integer.parseInt(data.get("page"));
        return getRankService.getList(page);
    }
}
