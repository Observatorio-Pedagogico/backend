package com.obervatorio_pedagogico.backend.application.services.extracao;

import com.obervatorio_pedagogico.backend.domain.exceptions.FormatoArquivoNaoSuportadoException;
import com.obervatorio_pedagogico.backend.domain.model.extracao.ExtracaoModel;
import com.obervatorio_pedagogico.backend.infrastructure.persistence.repository.extracao.ExtracaoRepository;
import com.obervatorio_pedagogico.backend.infrastructure.utils.modelMapper.ModelMapperService;
import com.obervatorio_pedagogico.backend.presentation.dto.extracao.Arquivo;
import com.obervatorio_pedagogico.backend.presentation.dto.extracao.ExtracaoRequest;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ExtracaoService {

    private ExtracaoRepository extracaoRepository;

    private ModelMapperService modelMapperService;
    
    public ExtracaoModel enviar(ExtracaoRequest extracaoRequest) {
        validarFormatoArquivo(extracaoRequest.getArquivo());

        ExtracaoModel extracao = modelMapperService.convert(extracaoRequest, ExtracaoModel.class);
        validarArquivo(extracaoRequest.getArquivo());
        lerArquivo(extracaoRequest.getArquivo());
        return extracaoRepository.save(extracao);
    }

    public void processar() {
        
    }

    public void validarFormatoArquivo(Arquivo arquivo) {
        if (!arquivo.isXls())
            throw new FormatoArquivoNaoSuportadoException(arquivo.getConteudo().getContentType());
    }

    public void validarArquivo(Arquivo arquivo) {

    }

    public void lerArquivo(Arquivo arquivo) {

    }
}
