package com.obervatorio_pedagogico.backend.application.services.disciplina;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.obervatorio_pedagogico.backend.application.services.usuario.AlunoService;
import com.obervatorio_pedagogico.backend.domain.model.disciplina.Disciplina;
import com.obervatorio_pedagogico.backend.domain.model.usuario.Aluno;
import com.obervatorio_pedagogico.backend.infrastructure.persistence.repository.disciplina.DisciplinaRepository;

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

    public Optional<Disciplina> buscarPorCodigoEPeriodoLetivo(String codigo, String periodoLetivo) {
        Optional<Disciplina> disciplinaSalvaOp = disciplinaRepository.findDisciplinaByCodigoAndPeriodoLetivo(codigo, periodoLetivo);
        return disciplinaSalvaOp;
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
}
