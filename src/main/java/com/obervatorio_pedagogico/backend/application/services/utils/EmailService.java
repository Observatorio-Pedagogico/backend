package com.obervatorio_pedagogico.backend.application.services.utils;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private List<String> dominiosPermitidos = Arrays.asList("academico.ifpb.edu.br");

    public boolean isDominioValido(String email) {
        String dominioEmail = email.split("@")[1];
        return this.dominiosPermitidos.contains(dominioEmail);
    }
}
