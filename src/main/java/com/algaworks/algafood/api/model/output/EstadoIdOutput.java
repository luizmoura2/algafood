package com.algaworks.algafood.api.model.output;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EstadoIdOutput {
	
	@NotNull
	private Long id;

}