package com.obervatorio_pedagogico.backend.infrastructure.persistence.repository.usuario;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.obervatorio_pedagogico.backend.domain.model.usuario.FuncionarioCoped;

@Repository
public interface FuncionarioCopedRepository extends JpaRepository<FuncionarioCoped, Long> {
    public Optional<FuncionarioCoped> findFuncionarioCopedByEmailAndSenha(String email, String senha);

    public Optional<FuncionarioCoped> findFuncionarioCopedByEmail(String email);
}
