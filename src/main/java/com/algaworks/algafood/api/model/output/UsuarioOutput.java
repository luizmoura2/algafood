package com.algaworks.algafood.api.model.output;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UsuarioOutput {

	private Long id;
	private String nome;
	private String email;
	//private List<GrupoOutput> grupos;

}