package com.percy.fish_hunter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.percy.fish_hunter.dto.AuthenticationRequest;
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
    private JwtUtil jwtUtil;

    @PostMapping("/token")
    public ResponseEntity<ObjectNode> generateTokenByUserName(@RequestBody AuthenticationRequest request) {

        var username = request.getUsername();
        var user = myUserDetailsService.loadUserByUsername(username);

        if (user == null) {
            user = myUserDetailsService.createNewPlayer(username);
        }

        var res = new ObjectMapper().createObjectNode();
        res.put("token", jwtUtil.generateToken(user.getUsername()));
        res.put("user", new ObjectMapper().valueToTree(user).toString());
        return ResponseEntity.ok(res);
    }
}
