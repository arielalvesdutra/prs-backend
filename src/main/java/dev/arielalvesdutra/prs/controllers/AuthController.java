package dev.arielalvesdutra.prs.controllers;

import dev.arielalvesdutra.prs.controllers.dto.LoginFormDTO;
import dev.arielalvesdutra.prs.controllers.dto.TokenDTO;
import dev.arielalvesdutra.prs.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequestMapping("/auth")
@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity<TokenDTO> auth(@RequestBody @Valid LoginFormDTO formDto)
            throws AuthenticationException {

        UsernamePasswordAuthenticationToken loginData = formDto.toUsernamePasswordAuthenticationToken();
        Authentication authentication = authManager.authenticate(loginData);
        String token = tokenService.generateToken(authentication);

        return ResponseEntity.ok(new TokenDTO(token));
    }
}
