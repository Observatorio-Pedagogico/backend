package com.obervatorio_pedagogico.backend.application.services.usuario;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.obervatorio_pedagogico.backend.domain.exceptions.NaoEncontradoException;
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

    public Optional<FuncionarioCoped> buscarPorId(Long id) {
        return funcionarioCopedRepository.findById(id);
    }

    public long count() {
        return funcionarioCopedRepository.count();
    }

    public FuncionarioCoped ativarFuncionarioCoped(Long id) {
        Optional<FuncionarioCoped> funcionarioCoped = buscarPorId(id);

        if (!funcionarioCoped.isPresent()) {
            throw new NaoEncontradoException();
        }

        funcionarioCoped.get().setAtivo(true);
        funcionarioCoped.get().setEsperaCadastro(false);
        return salvar(funcionarioCoped.get());
    }

    public Page<FuncionarioCoped> listarEsperaCadastro(Pageable pageable) {
        return funcionarioCopedRepository.findFuncionarioCopedWhereEsperaCadastroTrue(pageable);
    }
}
