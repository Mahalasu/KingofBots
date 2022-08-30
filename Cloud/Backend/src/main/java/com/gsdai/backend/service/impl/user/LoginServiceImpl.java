package com.gsdai.backend.service.impl.user;

import com.gsdai.backend.domain.User;
import com.gsdai.backend.service.user.LoginService;
import com.gsdai.backend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public Map<String, String> getToken(String username, String password) {
        Map<String, String> map = new HashMap<>();

        if (username == null || username.length() == 0) {
            map.put("errorMessage", "username cannot be null");
            return map;
        }

        if (password == null || password.length() == 0) {
            map.put("errorMessage", "password cannot be null");
            return map;
        }
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        // it will resolve the failure situation automatically
        try {
            Authentication authenticate = authenticationManager.authenticate(authenticationToken);
            UserDetailsImpl loginUser = (UserDetailsImpl) authenticate.getPrincipal();
            User user = loginUser.getUser();
            String jwtToken = JwtUtil.createJwt(user.getId().toString());
            map.put("errorMessage", "success");
            map.put("token", jwtToken);
        } catch (Exception e) {
            map.put("errorMessage", "wrong username or password");
        }

        return map;
    }
}
