package com.obervatorio_pedagogico.backend.infrastructure.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.obervatorio_pedagogico.backend.application.services.usuario.UsuarioService;
import com.obervatorio_pedagogico.backend.infrastructure.security.oauth.CustomOAuth2User;
import com.obervatorio_pedagogico.backend.infrastructure.security.oauth.CustomOAuth2UserService;

import lombok.AllArgsConstructor;

@EnableWebSecurity
@Configuration
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    private CustomOAuth2UserService oauthUserService;
    private UsuarioService usuarioService;

    // @Override
    // protected void configure(HttpSecurity http) throws Exception {
    //     http.cors().and().csrf().disable().authorizeRequests()
    //     .and()
    //     .authorizeRequests()
    //     .anyRequest().permitAll()
    //     .and()
    //     .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    // }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
        .and()
        .authorizeRequests()
        .anyRequest()
        .authenticated()
        // .and()
        // .exceptionHandling().accessDeniedHandler(new AccessDeniedHandler() {
        //     @Override
        //     public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        //         response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        //     }
        // })
        // .and()
        // .exceptionHandling().authenticationEntryPoint(new AuthenticationEntryPoint() {
        //     @Override
        //     public void commence(HttpServletRequest request, HttpServletResponse response,
        //                 AuthenticationException authException) throws IOException, ServletException {
        //             response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        //     }
        // })
        .and()
        .oauth2Login()
            .permitAll()
            .userInfoEndpoint()
                .userService(oauthUserService)
        .and()
        .successHandler(new AuthenticationSuccessHandler() {

            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                    Authentication authentication) throws IOException, ServletException {
        
                CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
                
                if (usuarioService.processarOAuthLogin(oauthUser.getEmail())) {
                    System.out.println("deu bom");
                    System.out.println(oauthUser.getAttributes());
                    response.sendRedirect("/observatorio-pedagogico/api/extracao");
                } else {
                    System.out.println("deu ruim");
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                }
            }
        });
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
        .antMatchers("/oauth/**")
        .antMatchers("/observatorio-pedagogico/api/extracao/enviar");
    }
}