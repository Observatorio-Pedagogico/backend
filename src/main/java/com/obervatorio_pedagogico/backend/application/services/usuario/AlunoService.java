package com.obervatorio_pedagogico.backend.application.services.usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import com.obervatorio_pedagogico.backend.domain.exceptions.NaoEncontradoException;
import com.obervatorio_pedagogico.backend.domain.model.usuario.Aluno;
import com.obervatorio_pedagogico.backend.infrastructure.persistence.repository.usuario.AlunoRepository;
import com.querydsl.core.types.Predicate;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AlunoService {
    private final AlunoRepository alunoRepository;

    public Aluno salvar(Aluno aluno) {
        return alunoRepository.save(aluno);
    }

    public List<Aluno> salvar(List<Aluno> listaAlunos) {
        List<Aluno> listaAlunosSalvos = new ArrayList<Aluno>();
        for(Aluno aluno : listaAlunos) {
            listaAlunosSalvos.add(this.salvar(aluno));
        }
        return listaAlunosSalvos;
    }

    public Optional<Aluno> buscarPorMatricula(String matricula) {
        Optional<Aluno> alunoSalvoOp = alunoRepository.findAlunoByMatricula(matricula);
        return alunoSalvoOp;
    }

    public Page<Aluno> buscar(Pageable pageable, Predicate predicate) {
        Page<Aluno> alunos = alunoRepository.findAll(predicate, pageable);

        if (alunos.isEmpty())
            throw new NaoEncontradoException();

        return alunos;
    }

    public void delete(Aluno aluno) {
        alunoRepository.deleteById(aluno.getId());
    }
}
