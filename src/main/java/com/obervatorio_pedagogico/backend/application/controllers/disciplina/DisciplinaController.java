package com.obervatorio_pedagogico.backend.application.controllers.disciplina;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.obervatorio_pedagogico.backend.application.services.disciplina.DisciplinaService;
import com.obervatorio_pedagogico.backend.infrastructure.utils.httpResponse.ResponseService;
import com.obervatorio_pedagogico.backend.presentation.shared.Response;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/observatorio-pedagogico/api/disciplina")
@AllArgsConstructor
@CrossOrigin
public class DisciplinaController {

    private DisciplinaService disciplinaService;

    private ResponseService responseService;

    @GetMapping("/periodos")
    public ResponseEntity<Response<List<String>>> buscarPeriodos() {
        List<String> periodos = disciplinaService.buscarListaPeriodos();

        return responseService.ok(periodos);
    }
}
