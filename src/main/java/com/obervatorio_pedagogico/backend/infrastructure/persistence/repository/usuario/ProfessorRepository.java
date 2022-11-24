package com.obervatorio_pedagogico.backend.infrastructure.persistence.repository.usuario;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.obervatorio_pedagogico.backend.domain.model.usuario.Professor;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    public Optional<Professor> findProfessorByEmailAndSenha(String email, String senha);

    public Optional<Professor> findProfessorByEmail(String email);

    @Query(value = "SELECT * FROM t_professor coped WHERE coped.espera_cadastro = true", nativeQuery = true)
    public Page<Professor> findProfessorWhereEsperaCadastroTrue(Pageable pageable);

    @Query(value = "SELECT * FROM t_professor coped WHERE coped.espera_cadastro = false", nativeQuery = true)
    public Page<Professor> findProfessorWhereEsperaCadastroFalse(Pageable pageable);
}
