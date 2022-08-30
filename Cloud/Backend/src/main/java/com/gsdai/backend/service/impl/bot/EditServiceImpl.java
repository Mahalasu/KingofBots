package com.gsdai.backend.service.impl.bot;

import com.gsdai.backend.domain.Bot;
import com.gsdai.backend.domain.User;
import com.gsdai.backend.mapper.BotMapper;
import com.gsdai.backend.service.bot.EditService;
import com.gsdai.backend.service.impl.user.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class EditServiceImpl implements EditService {

    @Autowired
    private BotMapper botMapper;

    @Override
    public Map<String, String> edit(Map<String, String> data) {
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

        int id = Integer.parseInt(data.get("id"));
        Bot bot = botMapper.selectById(id);
        if (bot == null) {
            map.put("errorMessage", "bot dose not exist");
            return map;
        }

        if (!bot.getUserId().equals(user.getId())) {
            map.put("errorMessage", "you do not have rights to alter this bot");
            return map;
        }

        Bot newBot = new Bot(
          bot.getId(),
          user.getId(),
          name,
          description,
          code,
          bot.getCreateTime(),
          new Date()
        );
        botMapper.updateById(newBot);
        map.put("errorMessage", "success");
        return map;
    }
}
