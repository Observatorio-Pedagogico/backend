package com.obervatorio_pedagogico.backend.infrastructure.persistence.repository.usuario;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.obervatorio_pedagogico.backend.domain.model.usuario.Professor;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    public Optional<Professor> findProfessorByEmailAndSenha(String email, String senha);

    public Optional<Professor> findProfessorByEmail(String email);
}
