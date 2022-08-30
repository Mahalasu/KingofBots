package com.gsdai.backend.controller.gamehistory;

import com.alibaba.fastjson.JSONObject;
import com.gsdai.backend.service.gameHistory.GetHistoryListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class GetHistoryListController {

    @Autowired
    private GetHistoryListService getHistoryListService;

    @GetMapping("/api/gamehistory/getlist")
    public JSONObject getList(@RequestParam Map<String, String> data) {
        Integer page = Integer.parseInt(data.get("page"));
        return getHistoryListService.getList(page);
    }


}
