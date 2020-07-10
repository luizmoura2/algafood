package com.algaworks.algafood.api.model.input;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CidadeResumoInput {

	private Long id;
	private String nome;
	private EstadoInput estado;	
}