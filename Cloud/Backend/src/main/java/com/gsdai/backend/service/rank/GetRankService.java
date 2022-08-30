package com.gsdai.backend.service.rank;

import com.alibaba.fastjson.JSONObject;

public interface GetRankService {
    JSONObject getList(Integer page);
}
