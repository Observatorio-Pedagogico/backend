package com.obervatorio_pedagogico.backend.application.services.usuario;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.obervatorio_pedagogico.backend.domain.model.usuario.FuncionarioCoped;
import com.obervatorio_pedagogico.backend.infrastructure.persistence.repository.usuario.FuncionarioCopedRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FuncionarioCopedService {

    private FuncionarioCopedRepository funcionarioCopedRepository;

    public FuncionarioCoped salvar(FuncionarioCoped funcionarioCoped) {
        return funcionarioCopedRepository.save(funcionarioCoped);
    }

    public Optional<FuncionarioCoped> buscarPorEmailESenha(String email, String senha) {
        return funcionarioCopedRepository.findFuncionarioCopedByEmailAndSenha(email, senha);
    }

    public Optional<FuncionarioCoped> buscarPorEmail(String email) {
        return funcionarioCopedRepository.findFuncionarioCopedByEmail(email);
    }

    public long count() {
        return funcionarioCopedRepository.count();
    }
}
