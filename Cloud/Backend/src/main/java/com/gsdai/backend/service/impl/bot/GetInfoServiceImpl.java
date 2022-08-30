package com.gsdai.backend.service.impl.bot;

import com.gsdai.backend.domain.Bot;
import com.gsdai.backend.mapper.BotMapper;
import com.gsdai.backend.service.bot.GetInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GetInfoServiceImpl implements GetInfoService {

    @Autowired
    private BotMapper botMapper;

    @Override
    public Bot getInfo(Map<String, String> data) {
        int id = Integer.parseInt(data.get("id"));
        return botMapper.selectById(id);
    }
}
