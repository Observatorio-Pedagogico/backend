package com.obervatorio_pedagogico.backend.infrastructure.utils.modelMapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class ModelMapperService {

    private ModelMapper modelMapper;

    public ModelMapperService() {
        this.modelMapper = new ModelMapper();
    }

    public <T> T convert(Object origin, Class<T> target) {
        return modelMapper.map(origin, target);
    }

    public <T, S> List<S> convert(List<T> listOrigin, Class<S> target) {
        return listOrigin.stream().map(origin -> convert(origin, target)).collect(Collectors.toList());
    }

    public <T> Page<T> convert(Page<?> pagina, Class<T> type) {
        return pagina.map(elemento -> convert(elemento, type));
    }
}
