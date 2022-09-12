package com.obervatorio_pedagogico.backend.application.services.usuario;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.obervatorio_pedagogico.backend.domain.exceptions.NaoEncontradoException;
import com.obervatorio_pedagogico.backend.domain.model.usuario.Coped;
import com.obervatorio_pedagogico.backend.infrastructure.persistence.repository.usuario.FuncionarioCopedRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FuncionarioCopedService {

    private FuncionarioCopedRepository funcionarioCopedRepository;

    public Coped salvar(Coped funcionarioCoped) {
        return funcionarioCopedRepository.save(funcionarioCoped);
    }

    public Optional<Coped> buscarPorEmailESenha(String email, String senha) {
        return funcionarioCopedRepository.findFuncionarioCopedByEmailAndSenha(email, senha);
    }

    public Optional<Coped> buscarPorEmail(String email) {
        return funcionarioCopedRepository.findFuncionarioCopedByEmail(email);
    }

    public Optional<Coped> buscarPorId(Long id) {
        return funcionarioCopedRepository.findById(id);
    }

    public long count() {
        return funcionarioCopedRepository.count();
    }

    public Coped ativarFuncionarioCoped(Long id) {
        Optional<Coped> funcionarioCoped = buscarPorId(id);

        if (!funcionarioCoped.isPresent()) {
            throw new NaoEncontradoException();
        }

        funcionarioCoped.get().setAtivo(true);
        funcionarioCoped.get().setEsperaCadastro(false);
        return salvar(funcionarioCoped.get());
    }

    public List<Coped> listarEsperaCadastro() {
        return funcionarioCopedRepository.findFuncionarioCopedWhereEsperaCadastroTrue();
    }
}
