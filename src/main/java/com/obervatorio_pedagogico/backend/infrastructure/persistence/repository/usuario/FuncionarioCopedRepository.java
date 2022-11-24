package com.obervatorio_pedagogico.backend.infrastructure.persistence.repository.usuario;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.obervatorio_pedagogico.backend.domain.model.usuario.FuncionarioCoped;

@Repository
public interface FuncionarioCopedRepository extends JpaRepository<FuncionarioCoped, Long> {
    public Optional<FuncionarioCoped> findFuncionarioCopedByEmailAndSenha(String email, String senha);

    public Optional<FuncionarioCoped> findFuncionarioCopedByEmail(String email);

    @Query(value = "SELECT * FROM t_funcionario_coped coped WHERE coped.espera_cadastro = true", nativeQuery = true)
    public Page<FuncionarioCoped> findFuncionarioCopedWhereEsperaCadastroTrue(Pageable pageable);

    @Query(value = "SELECT * FROM t_funcionario_coped coped WHERE coped.espera_cadastro = false", nativeQuery = true)
    public Page<FuncionarioCoped> findFuncionarioCopedWhereEsperaCadastroFalse(Pageable pageable);
}
