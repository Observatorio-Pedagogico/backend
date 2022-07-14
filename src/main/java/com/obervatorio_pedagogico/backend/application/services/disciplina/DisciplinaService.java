package com.obervatorio_pedagogico.backend.application.services.disciplina;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.obervatorio_pedagogico.backend.application.services.usuario.AlunoService;
import com.obervatorio_pedagogico.backend.domain.exceptions.NaoEncontradoException;
import com.obervatorio_pedagogico.backend.domain.model.disciplina.Disciplina;
import com.obervatorio_pedagogico.backend.infrastructure.persistence.repository.disciplina.DisciplinaRepository;
import org.springframework.stereotype.Service;

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

    public void deleteByIdDisciplina(Long id) {
        Disciplina disciplina = disciplinaRepository.findById(id).orElseThrow(() -> new NaoEncontradoException("Não foi possivel encontrar o ".concat(id.toString())));

        disciplina.getAlunos().stream().forEach(aluno -> {
            aluno.removeDisciplina(disciplina);
            
            if (disciplina.isPassivoDeletar()) {
                alunoService.deleteById(aluno.getId());
            } else {
                alunoService.salvar(aluno);
            }
        });

        disciplinaRepository.deleteById(id);
    }
}
