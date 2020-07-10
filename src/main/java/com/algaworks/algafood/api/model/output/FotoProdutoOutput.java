package com.algaworks.algafood.api.model.output;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FotoProdutoOutput {

	private String nomeArquivo;
	private String descricao;
	private String contentType;
	private Long tamanho;
	
}