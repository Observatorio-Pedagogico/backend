package com.obervatorio_pedagogico.backend.infrastructure.security.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.obervatorio_pedagogico.backend.application.services.usuario.FuncionarioCopedService;
import com.obervatorio_pedagogico.backend.application.services.usuario.ProfessorService;
import com.obervatorio_pedagogico.backend.domain.model.usuario.Coped;
import com.obervatorio_pedagogico.backend.domain.model.usuario.Professor;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SecurityService implements UserDetailsService {

    private FuncionarioCopedService funcionarioCopedService;
    private ProfessorService ProfessorService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Coped> funcionarioCopedRecuperadoOp;
        Optional<Professor> professorRecuperadoOp;

        List<SimpleGrantedAuthority> roles = new ArrayList<>();
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        funcionarioCopedRecuperadoOp = funcionarioCopedService.buscarPorEmail(email);
        if (funcionarioCopedRecuperadoOp.isPresent()) {
            return new User(
                funcionarioCopedRecuperadoOp.get().getEmail(), 
                encoder.encode(funcionarioCopedRecuperadoOp.get().getSenha()),
                roles
            );
        }

        professorRecuperadoOp = ProfessorService.buscarPorEmail(email);
        if (professorRecuperadoOp.isPresent()) {
            return new User(
                professorRecuperadoOp.get().getEmail(), 
                encoder.encode(professorRecuperadoOp.get().getSenha()),
                roles
            );
        }

        throw new UsernameNotFoundException(email);
    }
}