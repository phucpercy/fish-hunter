package com.percy.fish_hunter.controller;

import com.percy.fish_hunter.converter.PlayerConverter;
import com.percy.fish_hunter.dto.AuthenticationRequest;
import com.percy.fish_hunter.dto.TokenResponse;
import com.percy.fish_hunter.service.MyUserDetailsService;
import com.percy.fish_hunter.utils.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor(onConstructor_ = @Autowired)
public class AuthenticationController {

    private MyUserDetailsService myUserDetailsService;
    private PlayerConverter playerConverter;
    private JwtUtil jwtUtil;

    @PostMapping("/token")
    public ResponseEntity<Object> generateTokenByUserName(@RequestBody AuthenticationRequest request) {

        var username = request.getUsername();
        var user = myUserDetailsService.loadUserByUsername(username);

        if (user == null) {
            user = myUserDetailsService.createNewPlayer(username);
        }

        var userDto = playerConverter.toDto(user);

        var res = TokenResponse.builder()
                .token(jwtUtil.generateToken(user.getUsername()))
                .user(userDto)
                .build();
        return ResponseEntity.ok(res);
    }
}
