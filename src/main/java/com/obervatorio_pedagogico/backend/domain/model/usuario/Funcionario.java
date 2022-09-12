package com.obervatorio_pedagogico.backend.domain.model.usuario;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class Funcionario extends Usuario {
    @Column(name = "ativo")
    protected Boolean ativo = false;

    @Column(name = "espera_cadastro")
    protected Boolean esperaCadastro = true;

    public boolean isAtivo() {
        return this.getAtivo();
    }

    public boolean isEsperaCadastro() {
        return this.getEsperaCadastro();
    }
}
