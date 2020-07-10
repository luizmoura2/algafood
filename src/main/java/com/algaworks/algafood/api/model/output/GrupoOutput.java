package com.algaworks.algafood.api.model.output;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GrupoOutput {

	private Long id;
	private String nome;
	private List<PermissaoOutput> permissoes;

}