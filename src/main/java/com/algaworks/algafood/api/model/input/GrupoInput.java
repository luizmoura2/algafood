package com.algaworks.algafood.api.model.input;


import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GrupoInput {

	@NotBlank
	private String nome;

	@Valid
	@NotNull
	private List<PermissaoIdInput> permissoes;

}