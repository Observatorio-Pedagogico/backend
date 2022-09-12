package com.obervatorio_pedagogico.backend.application.services.usuario;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.obervatorio_pedagogico.backend.domain.model.usuario.FuncionarioCoped;
import com.obervatorio_pedagogico.backend.domain.model.usuario.Professor;
import com.obervatorio_pedagogico.backend.domain.model.usuario.Funcionario;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FuncionarioService {

    private FuncionarioCopedService funcionarioCopedService;

    private ProfessorService professorService;

    public Optional<Funcionario> buscarFuncionarioByEmail(String email) {
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

    public boolean existeFuncionarioCadastrado() {
        return funcionarioCopedService.count() > 0 || professorService.count() > 0;
    }
}
