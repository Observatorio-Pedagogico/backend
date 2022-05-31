package com.obervatorio_pedagogico.backend.infrastructure.persistence.repository.usuario;

import org.springframework.stereotype.Repository;

import java.util.Optional;

import com.obervatorio_pedagogico.backend.domain.model.usuario.Aluno;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    public Optional<Aluno> findAlunoByMatricula(String matricula);
}
