package com.obervatorio_pedagogico.backend.infrastructure.utils.modelMapper;

import java.util.Objects;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ModelMapperService {

    private ModelMapper modelMapper;

    public <T> T convert(Object origin, Class<T> target) {
        modelMapper = init();
        return modelMapper.map(origin, target);
    }

    private ModelMapper init() {
        if(Objects.isNull(modelMapper)) {
            this.modelMapper = new ModelMapper();
        }
        return modelMapper;
    }
}
