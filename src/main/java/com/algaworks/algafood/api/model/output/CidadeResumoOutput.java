package com.algaworks.algafood.api.model.output;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CidadeResumoOutput {

	private Long id;
	private String nome;
	@JsonProperty("Estado")
	private String nomeEstado;	
}