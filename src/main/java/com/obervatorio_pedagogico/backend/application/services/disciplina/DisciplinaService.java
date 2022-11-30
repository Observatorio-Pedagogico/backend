package com.obervatorio_pedagogico.backend.application.services.disciplina;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.obervatorio_pedagogico.backend.application.services.usuario.AlunoService;
import com.obervatorio_pedagogico.backend.domain.exceptions.NaoEncontradoException;
import com.obervatorio_pedagogico.backend.domain.model.disciplina.Disciplina;
import com.obervatorio_pedagogico.backend.domain.model.usuario.Aluno;
import com.obervatorio_pedagogico.backend.infrastructure.persistence.repository.disciplina.DisciplinaRepository;
import com.querydsl.core.types.Predicate;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class DisciplinaService {

    private final DisciplinaRepository disciplinaRepository;
    private final AlunoService alunoService;

    public Disciplina salvar(Disciplina disciplina) {
        return disciplinaRepository.save(disciplina);
    }

    public List<Disciplina> salvar(List<Disciplina> listaDisciplinas) {
        List<Disciplina> listaDisciplinaSalvas = new ArrayList<>();
        for(Disciplina disciplina : listaDisciplinas) {
            listaDisciplinaSalvas.add(this.salvar(disciplina));
        }
        return listaDisciplinaSalvas;
    }

    public List<Disciplina> buscarPorCodigo(String codigo) {
        List<Disciplina> disciplinas = disciplinaRepository.findDisciplinaByCodigo(codigo);
        return disciplinas;
    }

    public List<Disciplina> buscarPorCodigo(List<String> codigos) {
        List<Disciplina> disciplinas = disciplinaRepository.findDisciplinaByCodigo(codigos);
        return disciplinas;
    }

    public Optional<Disciplina> buscarPorCodigoEPeriodoLetivo(String codigo, String periodoLetivo) {
        Optional<Disciplina> disciplinaSalvaOp = disciplinaRepository.findDisciplinaByCodigoAndPeriodoLetivo(codigo, periodoLetivo);
        return disciplinaSalvaOp;
    }

    public Disciplina buscarPorId(Long id) {
        Disciplina disciplina = disciplinaRepository.findById(id).orElseThrow(() -> new NaoEncontradoException());
        return disciplina;
    }

    public void deleteDisciplina(Disciplina disciplina) {
        List<Aluno> disciplinaAlunosClone = new ArrayList<>(disciplina.getAlunos());
        disciplinaAlunosClone.stream().forEach(aluno -> {
            disciplina.removeAluno(aluno);
            aluno.removeDisciplina(disciplina);
            if (aluno.isPassivoDeletar()) {
                alunoService.delete(aluno);
            }
        });

        disciplinaRepository.deleteById(disciplina.getId());
    }

    public Page<Disciplina> buscar(Pageable pageable, Predicate predicate) {
        Page<Disciplina> disciplinas = disciplinaRepository.findAll(predicate, pageable);

        if (disciplinas.isEmpty())
            throw new NaoEncontradoException();

        return disciplinas;
    }

    public List<Disciplina> buscarIgnorandoPeriodos() {
        return disciplinaRepository.findDisciplinaIgnorePeriodo();
    }

    public List<String> buscarListaPeriodos(Predicate predicate) {
        Set<String> legendaPeriodoLetivos = new LinkedHashSet<>();
        List<Disciplina> disciplinas = StreamSupport.stream(this.disciplinaRepository.findAll(predicate, Sort.by(Sort.Direction.ASC, "periodoLetivo")).spliterator(), false)
                                            .collect(Collectors.toList());
        if (disciplinas.isEmpty())
            throw new NaoEncontradoException();

        disciplinas.stream().forEach(disciplina -> {
            legendaPeriodoLetivos.add(disciplina.getPeriodoLetivo());
        });

        return new ArrayList<>(legendaPeriodoLetivos);
    }
}
