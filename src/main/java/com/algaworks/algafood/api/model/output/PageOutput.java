package com.algaworks.algafood.api.model.output;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageOutput<T> {

    private List<T> content;

    private long totalElements;
    private int totalPages;
    private int number;
    private int size;
}