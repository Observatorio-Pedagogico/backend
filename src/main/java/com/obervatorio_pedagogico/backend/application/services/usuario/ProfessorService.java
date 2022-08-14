package com.obervatorio_pedagogico.backend.application.services.usuario;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.obervatorio_pedagogico.backend.domain.model.usuario.Professor;
import com.obervatorio_pedagogico.backend.infrastructure.persistence.repository.usuario.ProfessorRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProfessorService {

    private ProfessorRepository ProfessorRepository;

    public Professor salvar(Professor professor) {
        return ProfessorRepository.save(professor);
    }

    public Optional<Professor> buscarPorEmailESenha(String email, String senha) {
        return ProfessorRepository.findProfessorByEmailAndSenha(email, senha);
    }

    public Optional<Professor> buscarPorEmail(String email) {
        return ProfessorRepository.findProfessorByEmail(email);
    }

    public long count() {
        return ProfessorRepository.count();
    }
}
