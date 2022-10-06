package com.obervatorio_pedagogico.backend.application.services.usuario;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.obervatorio_pedagogico.backend.domain.model.usuario.FuncionarioCoped;
import com.obervatorio_pedagogico.backend.domain.model.usuario.Professor;
import com.obervatorio_pedagogico.backend.infrastructure.security.auth.JwtUtils;
import com.obervatorio_pedagogico.backend.presentation.model.usuario.EnvelopeFuncionario;
import com.obervatorio_pedagogico.backend.presentation.model.usuario.EnvelopeFuncionario.TipoFuncionario;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FuncionarioService {

    private FuncionarioCopedService funcionarioCopedService;

    private ProfessorService professorService;

    private JwtUtils jwtUtils;

    public Optional<EnvelopeFuncionario> buscarFuncionarioPorEmail(String email) {
        Optional<FuncionarioCoped> funcionarioCopedOp = funcionarioCopedService.buscarPorEmail(email);
        Optional<Professor> professorOp;
        EnvelopeFuncionario envelopeFuncionario;

        if (funcionarioCopedOp.isPresent()) {
            envelopeFuncionario = new EnvelopeFuncionario(
                funcionarioCopedOp.get(),
                TipoFuncionario.FUNCIONARIO_COPED
            );
            return Optional.of(envelopeFuncionario);
        }

        professorOp = professorService.buscarPorEmail(email);

        if (professorOp.isPresent()) {
            envelopeFuncionario = new EnvelopeFuncionario(
                professorOp.get(),
                TipoFuncionario.PROFESSOR
            );
            return Optional.of(envelopeFuncionario);
        }

        return Optional.empty();
    }

    public Optional<EnvelopeFuncionario> buscarFuncionarioPorToken(String token) {
        String email = jwtUtils.extractEmail(token);

        return buscarFuncionarioPorEmail(email);
    }

    public boolean existeFuncionarioCadastrado() {
        return funcionarioCopedService.count() > 0 || professorService.count() > 0;
    }
}
