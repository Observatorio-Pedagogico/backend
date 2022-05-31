package com.obervatorio_pedagogico.backend.application.services.usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.obervatorio_pedagogico.backend.domain.exceptions.UsuarioNaoEncontradoException;
import com.obervatorio_pedagogico.backend.domain.exceptions.UsuarioNaoEncontradoException.AtributoBuscado;
import com.obervatorio_pedagogico.backend.domain.model.usuario.Aluno;
import com.obervatorio_pedagogico.backend.infrastructure.persistence.repository.usuario.AlunoRepository;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
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

    public Aluno buscarPorMatricula(String matricula) {
        Optional<Aluno> alunoSalvoOp = alunoRepository.findAlunoByMatricula(matricula);
        if(!alunoSalvoOp.isPresent())
            throw new UsuarioNaoEncontradoException(AtributoBuscado.MATRICULA, "Aluno", matricula);
        return alunoSalvoOp.get();
    }
}
