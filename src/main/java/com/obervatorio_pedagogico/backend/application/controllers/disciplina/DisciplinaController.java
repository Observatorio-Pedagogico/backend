package com.obervatorio_pedagogico.backend.application.controllers.disciplina;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.obervatorio_pedagogico.backend.application.services.disciplina.DisciplinaService;
import com.obervatorio_pedagogico.backend.domain.model.disciplina.Disciplina;
import com.obervatorio_pedagogico.backend.infrastructure.utils.buscaConstrutor.PredicatesGenerator;
import com.obervatorio_pedagogico.backend.infrastructure.utils.httpResponse.ResponseService;
import com.obervatorio_pedagogico.backend.infrastructure.utils.modelMapper.ModelMapperService;
import com.obervatorio_pedagogico.backend.presentation.dto.disciplina.request.DisciplinaBuscaRequest;
import com.obervatorio_pedagogico.backend.presentation.dto.disciplina.response.DisciplinaResumidoResponse;
import com.obervatorio_pedagogico.backend.presentation.shared.Response;
import com.querydsl.core.types.dsl.BooleanExpression;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/observatorio-pedagogico/api/disciplina")
@AllArgsConstructor
@CrossOrigin
public class DisciplinaController {

    private DisciplinaService disciplinaService;

    private ResponseService responseService;

    private ModelMapperService modelMapperService;

    private PredicatesGenerator predicatesGenerator;

    @GetMapping("/resumido")
    public ResponseEntity<Response<List<DisciplinaResumidoResponse>>> buscarDisciplinasResumido(Pageable pageable, DisciplinaBuscaRequest disciplinaBuscaRequest) {
        BooleanExpression predicate = predicatesGenerator.add(disciplinaBuscaRequest).build();
        List<Disciplina> disciplinas = disciplinaService.buscar(pageable, predicate);

        return responseService.ok(modelMapperService.convert(disciplinas, DisciplinaResumidoResponse.class));
    }

    @GetMapping("/periodos")
    public ResponseEntity<Response<List<String>>> buscarPeriodos() {
        List<String> periodos = disciplinaService.buscarListaPeriodos();

        return responseService.ok(periodos);
    }
}
