package com.obervatorio_pedagogico.backend.application.services.disciplina;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.obervatorio_pedagogico.backend.domain.model.disciplina.Disciplina;
import com.obervatorio_pedagogico.backend.infrastructure.persistence.repository.disciplina.DisciplinaRepository;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class DisciplinaService {
    private final DisciplinaRepository disciplinaRepository;

    public Disciplina salvar(Disciplina disciplina) {
        return disciplinaRepository.save(disciplina);
    }

    public List<Disciplina> salvar(List<Disciplina> listaDisciplinas) {
        List<Disciplina> listaDisciplinaSalvas = new ArrayList<Disciplina>();
        for(Disciplina disciplina : listaDisciplinas) {
            listaDisciplinaSalvas.add(this.salvar(disciplina));
        }
        return listaDisciplinaSalvas;
    }

    public Optional<Disciplina> buscarPorCodigoEPeriodoLetivo(String codigo, String periodoLetivo) {
        Optional<Disciplina> disciplinaSalvaOp = disciplinaRepository.findDisciplinaByCodigoAndPeriodoLetivo(codigo, periodoLetivo);
        return disciplinaSalvaOp;
    }
}
