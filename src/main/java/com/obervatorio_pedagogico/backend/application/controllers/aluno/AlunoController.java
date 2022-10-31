package com.obervatorio_pedagogico.backend.application.controllers.aluno;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.obervatorio_pedagogico.backend.application.services.usuario.AlunoService;
import com.obervatorio_pedagogico.backend.domain.model.usuario.Aluno;
import com.obervatorio_pedagogico.backend.infrastructure.utils.buscaConstrutor.PredicatesGenerator;
import com.obervatorio_pedagogico.backend.infrastructure.utils.httpResponse.ResponseService;
import com.obervatorio_pedagogico.backend.infrastructure.utils.modelMapper.ModelMapperService;
import com.obervatorio_pedagogico.backend.presentation.dto.usuario.request.AlunoBuscaRequest;
import com.obervatorio_pedagogico.backend.presentation.dto.usuario.response.AlunoResumidoResponse;
import com.obervatorio_pedagogico.backend.presentation.shared.Response;
import com.querydsl.core.types.dsl.BooleanExpression;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/observatorio-pedagogico/api/aluno")
@AllArgsConstructor
@CrossOrigin
public class AlunoController {
    
    private AlunoService alunoService;

    private ResponseService responseService;

    private ModelMapperService modelMapperService;

    private PredicatesGenerator predicatesGenerator;

    @GetMapping("/resumido")
    public ResponseEntity<Response<Page<AlunoResumidoResponse>>> buscarDisciplinas(Pageable pageable, AlunoBuscaRequest alunoBuscaRequest) {
        BooleanExpression predicate = predicatesGenerator.add(alunoBuscaRequest).build();
        Page<Aluno> alunos = alunoService.buscar(pageable, predicate);

        return responseService.ok(modelMapperService.convert(alunos, AlunoResumidoResponse.class));
    }
}
