package com.example.api.web.rest;

import com.example.api.domain.User;
import com.example.api.dto.UserDto;
import com.example.api.security.DataTokenJWT;
import com.example.api.service.TokenService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/login")
public class AuthController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity efetuarLogin(@RequestBody @Valid UserDto userDto) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(userDto.getLogin(), userDto.getPassword());
        var authentication = manager.authenticate(authenticationToken);

        var tokenJWT = tokenService.getToken(authentication);

        return ResponseEntity.ok(new DataTokenJWT(tokenJWT));
    }

}
