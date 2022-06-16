package com.obervatorio_pedagogico.backend.application.controllers.extracao;

import com.obervatorio_pedagogico.backend.application.services.extracao.ExtracaoService;
import com.obervatorio_pedagogico.backend.domain.model.extracao.Extracao;
import com.obervatorio_pedagogico.backend.infrastructure.utils.httpResponse.ResponseService;
import com.obervatorio_pedagogico.backend.infrastructure.utils.modelMapper.ModelMapperService;
import com.obervatorio_pedagogico.backend.presentation.dto.extracao.ExtracaoRequest;
import com.obervatorio_pedagogico.backend.presentation.dto.extracao.ExtracaoResponse;
import com.obervatorio_pedagogico.backend.presentation.shared.Response;

import java.util.List;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/observatorio-pedagogico/api/extracao")
@AllArgsConstructor
public class ExtracaoController {

    private ExtracaoService extracaoService;

    private ModelMapperService modelMapperService;

    private ResponseService responseService;
    
    @PostMapping("/enviar")
    public ResponseEntity<Response<ExtracaoResponse>> enviar(ExtracaoRequest extracaoRequest) {
        extracaoService.cadastrar(extracaoRequest);
        return responseService.create(null);
    }

    @GetMapping("/get-todos")
    public ResponseEntity<Response<List<Extracao>>> getTodos(){
        try {
            return responseService.ok(extracaoService.getTodos());
        } catch (NotFoundException e) {
            return responseService.notFound();
        }
    }
}
