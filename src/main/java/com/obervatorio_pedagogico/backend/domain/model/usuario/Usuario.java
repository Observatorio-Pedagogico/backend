package com.obervatorio_pedagogico.backend.domain.model.usuario;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

import com.obervatorio_pedagogico.backend.domain.model.endereco.Endereco;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "matricula", nullable = false)
    protected String matricula;

    @Column(name = "email")
    protected String email;

    @Column(name = "senha")
    protected String senha;

    @Column(name = "nome")
    protected String nome;

    @Column(name = "sexo")
    protected Sexo sexo;

    @OneToOne
    @JoinColumn(name = "id_endereco")
    protected Endereco endereco;

    public enum Sexo {
        MASCULINO, FEMININO
    }
}
