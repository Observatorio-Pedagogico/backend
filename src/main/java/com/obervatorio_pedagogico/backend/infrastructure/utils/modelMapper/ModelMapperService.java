package com.obervatorio_pedagogico.backend.infrastructure.utils.modelMapper;

import org.modelmapper.ModelMapper;

public class ModelMapperService {

    private ModelMapper modelMapper;

    public ModelMapperService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public <T> T convert(Object origin, Class<T> target) {
        return modelMapper.map(origin, target);
    }
}
