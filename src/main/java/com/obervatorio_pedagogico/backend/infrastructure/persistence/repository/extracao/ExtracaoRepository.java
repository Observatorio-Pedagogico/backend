package com.obervatorio_pedagogico.backend.infrastructure.persistence.repository.extracao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.obervatorio_pedagogico.backend.domain.model.extracao.Extracao;

@Repository
public interface ExtracaoRepository extends JpaRepository<Extracao, Long>, QuerydslPredicateExecutor<Extracao>, JpaSpecificationExecutor<Extracao> {

}
