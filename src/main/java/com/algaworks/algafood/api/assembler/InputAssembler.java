package com.algaworks.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InputAssembler<T, S> {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public S  toDomainObject(T obj, Class<S> clz) {
		return modelMapper.map(obj, clz);
	}

	public void copyToDomainObject(T obj_src, S obj_des) {
			modelMapper.map(obj_src, obj_des);
	}
	
}