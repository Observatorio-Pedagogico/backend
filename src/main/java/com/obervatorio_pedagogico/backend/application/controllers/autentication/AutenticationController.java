package com.obervatorio_pedagogico.backend.application.controllers.autentication;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.obervatorio_pedagogico.backend.infrastructure.security.oauth.CustomOAuth2User;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/observatorio-pedagogico/api/login")
@AllArgsConstructor
@CrossOrigin
public class AutenticationController {
    
    // @GetMapping
    // public String login(@AuthenticationPrincipal OAuth2User principal) {
    //     System.out.println(principal.getName());
    //     return "aaaaa";
    // }

    @GetMapping
    public String login(@AuthenticationPrincipal CustomOAuth2User principal) {
        return "login efetuado com sucesso. Email: " + principal.getEmail();
    }
}
