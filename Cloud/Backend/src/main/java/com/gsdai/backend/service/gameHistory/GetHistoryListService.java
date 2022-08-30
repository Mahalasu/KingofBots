package com.gsdai.backend.service.gameHistory;

import com.alibaba.fastjson.JSONObject;

public interface GetHistoryListService {

    JSONObject getList(Integer page);
}
