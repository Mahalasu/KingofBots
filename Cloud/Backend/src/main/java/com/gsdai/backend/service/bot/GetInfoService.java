package com.gsdai.backend.service.bot;

import com.gsdai.backend.domain.Bot;

import java.util.Map;

public interface GetInfoService {
    Bot getInfo(Map<String, String> data);
}
