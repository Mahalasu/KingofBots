package com.gsdai.backend.service.impl.bot;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gsdai.backend.domain.Bot;
import com.gsdai.backend.domain.User;
import com.gsdai.backend.mapper.BotMapper;
import com.gsdai.backend.service.bot.CreateService;
import com.gsdai.backend.service.impl.user.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class CreateServiceImpl implements CreateService {

    @Autowired
    private BotMapper botMapper;

    @Override
    public Map<String, String> create(Map<String, String> data) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        String name = data.get("name");
        String description = data.get("description");
        String code = data.get("code");

        Map<String, String> map = new HashMap<>();
        if (name == null || name.length() == 0) {
            map.put("errorMessage", "name cannot be null");
            return map;
        }

        if (name.length() > 100) {
            map.put("errorMessage", "name cannot be longer than 100 characters");
            return map;
        }

        if (description != null && description.length() > 1000) {
            map.put("errorMessage", "description cannot be longer than 1000 characters");
            return map;
        }

        if (code == null || code.length() == 0) {
            map.put("errorMessage", "code cannot be null");
            return map;
        }

        if (code.length() > 10000) {
            map.put("errorMessage", "code cannot be longer than 1000 characters");
            return map;
        }

        QueryWrapper<Bot> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user.getId());
        if (botMapper.selectCount(queryWrapper) >= 10) {
            map.put("errorMessage", "you can only create no more than 10 bots");
            return map;
        }

        Date now = new Date();
        Bot bot = new Bot(null, user.getId(), name, description, code, now, now);
        botMapper.insert(bot);
        map.put("errorMessage", "success");

        return map;
    }
}
