package com.obervatorio_pedagogico.backend.application.services.extracao;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.obervatorio_pedagogico.backend.application.services.uploader.Uploader;
import com.obervatorio_pedagogico.backend.domain.exceptions.NaoEncontradoException;
import com.obervatorio_pedagogico.backend.domain.model.extracao.ExtracaoThread;

@Service
public class ExtracaoThreadService {
    
    Uploader uploader = Uploader.getInstance();

    public ExtracaoThread getById(Long id) {
        Optional<ExtracaoThread> extracaoThreadOp = uploader.findById(id);
        
        if(!extracaoThreadOp.isPresent()) throw new NaoEncontradoException("Extracao não encontrada");
        
        extracaoThreadOp.get().calcularPorcentagem();
        return extracaoThreadOp.get();
    }
}