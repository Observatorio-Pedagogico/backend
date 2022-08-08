package com.obervatorio_pedagogico.backend.infrastructure.security.oauth;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CustomOAuth2User implements OAuth2User {

    private OAuth2User oAuth2User;

    @Override
    public Map<String, Object> getAttributes() {
        return this.oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.oAuth2User.getAuthorities();
    }

    @Override
    public String getName() {
        return this.oAuth2User.getAttribute("name");
    }

    public String getEmail() {
        return this.oAuth2User.<String>getAttribute("email");
    }
    
}
