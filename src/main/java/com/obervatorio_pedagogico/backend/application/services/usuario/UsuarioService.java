package com.obervatorio_pedagogico.backend.application.services.usuario;

import org.springframework.stereotype.Service;

import com.obervatorio_pedagogico.backend.application.services.utils.EmailService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UsuarioService {

    private EmailService emailService;

    public Boolean processarOAuthLogin(String email) {
        System.out.println(email);
        if (emailService.isDominioValido(email)) {
            return true;
        }
        return false;
    }
}
