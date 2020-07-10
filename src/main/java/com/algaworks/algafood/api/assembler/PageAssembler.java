package com.algaworks.algafood.api.assembler;

import java.util.Collection;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.output.PageOutput;

@Component
public class PageAssembler<T, S> {

    private ModelMapper modelMapper;

    public PageAssembler(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public PageOutput<S> toCollectionModelPage(Page<T> page, Class<S> type) {
    	PageOutput<S> pageModel = new PageOutput<>();
        Collection<T> domainElementsList = (Collection<T>) page.getContent();
        pageModel.setContent(
                domainElementsList.stream()
                        .map(object -> modelMapper.map(object, type))
                        .collect(Collectors.toList()));
        pageModel.setNumber(page.getNumber());
        pageModel.setSize(page.getSize());
        pageModel.setTotalElements(page.getTotalElements());
        pageModel.setTotalPages(page.getTotalPages());

        return pageModel;
    }
}