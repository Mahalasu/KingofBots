package com.gsdai.botrunningsys.service.impl;

import com.gsdai.botrunningsys.service.BotRunningService;
import com.gsdai.botrunningsys.service.utils.BotPool;
import org.springframework.stereotype.Service;

@Service
public class BotRunningServiceImpl implements BotRunningService {

    public final static BotPool botPool = new BotPool();

    @Override
    public String addBot(Integer userId, String code, String input) {
        botPool.addBot(userId, code, input);
        return "add bot success";
    }
}
