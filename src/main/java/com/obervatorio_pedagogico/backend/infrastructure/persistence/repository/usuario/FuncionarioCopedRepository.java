package com.obervatorio_pedagogico.backend.infrastructure.persistence.repository.usuario;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.obervatorio_pedagogico.backend.domain.model.usuario.Coped;

@Repository
public interface FuncionarioCopedRepository extends JpaRepository<Coped, Long> {
    public Optional<Coped> findFuncionarioCopedByEmailAndSenha(String email, String senha);

    public Optional<Coped> findFuncionarioCopedByEmail(String email);

    @Query(value = "SELECT * FROM t_funcionario_coped coped WHERE coped.espera_cadastro = true", nativeQuery = true)
    public List<Coped> findFuncionarioCopedWhereEsperaCadastroTrue();
}
