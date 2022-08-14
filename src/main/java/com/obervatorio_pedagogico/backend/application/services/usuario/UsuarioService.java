package com.obervatorio_pedagogico.backend.application.services.usuario;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.obervatorio_pedagogico.backend.domain.model.usuario.FuncionarioCoped;
import com.obervatorio_pedagogico.backend.domain.model.usuario.Professor;
import com.obervatorio_pedagogico.backend.domain.model.usuario.Usuario;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UsuarioService {

    private FuncionarioCopedService funcionarioCopedService;

    private ProfessorService professorService;

    public Optional<Usuario> buscarUsuarioByEmail(String email) {
        Optional<FuncionarioCoped> funcionarioCopedOp = funcionarioCopedService.buscarPorEmail(email);
        Optional<Professor> professorOp;

        if (funcionarioCopedOp.isPresent()) {
            return Optional.of(funcionarioCopedOp.get());
        }

        professorOp = professorService.buscarPorEmail(email);

        if (professorOp.isPresent()) {
            return Optional.of(professorOp.get());
        }

        return Optional.empty();
    }

    public boolean existeUsuarioCadastrado() {
        return funcionarioCopedService.count() > 0 || professorService.count() > 0;
    }
}
