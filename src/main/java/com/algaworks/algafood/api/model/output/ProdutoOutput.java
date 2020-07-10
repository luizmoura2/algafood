package com.algaworks.algafood.api.model.output;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProdutoOutput {
	
	private Long id;
	private String nome;
	private String descricao;
	private BigDecimal preco;
	private boolean ativo;

}