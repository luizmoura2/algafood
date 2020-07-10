package com.algaworks.algafood.api.assembler;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OutputAssembler<T, S> {

	@Autowired
	private ModelMapper modelMapper;
	
	/**
	 * Converte um Objeto de Entidade do tipo T 
	 * em um Objeto do tipo DTO S modelo da classe 
	 * @param obj - O objeto da classe de entidade
	 * @param clz - A classe modelo DTO
	 * @return S 
	 */
	public S  toModel(T obj, Class<S> clz) {
		return modelMapper.map(obj, clz);
	}
	
	/**
	 * Converte um lista de objetos de classe de entidade obj em uma
	 * lista de objetos da classe modelo
	 * @param obj - O objeto da classe de entidade listado.
	 * @param clz - A classe modelo DTO, que compõe a lista convertida.
	 * @return List<'clz'>  Uma lista de Objetos da classe modelo DTO
	 */
	public List<S> toCollectionModel(Collection<T> obj, Class<S> clz) {
		return obj.stream()
				.map(result -> toModel(result, clz))
				.collect(Collectors.toList());
	}

}