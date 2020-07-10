package com.algaworks.algafood.api.model.input;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UsuarioInput {

	@NotBlank
	private String nome;

	@NotNull
	@Email
	private String email;
	
	//private List<GrupoIdInput> grupos;

}