package com.gsdai.backend.service.impl.bot;

import com.gsdai.backend.domain.Bot;
import com.gsdai.backend.domain.User;
import com.gsdai.backend.mapper.BotMapper;
import com.gsdai.backend.service.impl.user.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DeleteServiceImpl implements com.gsdai.backend.service.bot.DeleteService {

    @Autowired
    private BotMapper botMapper;

    @Override
    public Map<String, String> delete(Map<String, String> data) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        Map<String, String> map = new HashMap<>();
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

        botMapper.deleteById(id);
        map.put("errorMessage", "success");
        return map;
    }
}
