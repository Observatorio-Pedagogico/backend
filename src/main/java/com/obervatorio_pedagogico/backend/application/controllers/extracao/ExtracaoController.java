package com.obervatorio_pedagogico.backend.application.controllers.extracao;

import com.obervatorio_pedagogico.backend.application.services.extracao.ExtracaoService;
import com.obervatorio_pedagogico.backend.domain.model.extracao.ExtracaoModel;
import com.obervatorio_pedagogico.backend.infrastructure.utils.modelMapper.ModelMapperService;
import com.obervatorio_pedagogico.backend.presentation.dto.extracao.ExtracaoRequest;
import com.obervatorio_pedagogico.backend.presentation.dto.extracao.ExtracaoResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/observatorio-pedagogico/api/extracao")
public class ExtracaoController {

    private ExtracaoService extracaoService;

    private ModelMapperService modelMapperService;

    public ExtracaoController(
        ExtracaoService extracaoService, 
        ModelMapperService modelMapperService
    ) {
        this.extracaoService = extracaoService;
        this.modelMapperService = modelMapperService;
    }
    
    public ResponseEntity<ExtracaoResponse> enviar(ExtracaoRequest extracaoRequest) {
        ExtracaoModel extracaoModel = extracaoService.enviar(extracaoRequest);
        ExtracaoResponse extracaoResponse = modelMapperService.convert(extracaoModel, ExtracaoResponse.class);
        return ResponseEntity.status(HttpStatus.OK).body(extracaoResponse);
    }
}
