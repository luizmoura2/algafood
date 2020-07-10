package com.algaworks.algafood.api.model.output;

import java.math.BigDecimal;
import java.util.Set;

import com.algaworks.algafood.api.model.view.RestauranteView;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RestauranteOutput {

	@JsonView({ RestauranteView.Resumo.class, RestauranteView.ApenasNome.class })
	private Long id;
	
	@JsonView({ RestauranteView.Resumo.class, RestauranteView.ApenasNome.class })
	private String nome;
	
	@JsonView(RestauranteView.Resumo.class)
	private BigDecimal taxaFrete;
	
	@JsonView(RestauranteView.Resumo.class)
	
	private Boolean ativo;
	private Boolean aberto;
	private EnderecoOutput endereco;
	private Set<FormaPagamentoOutput> formasPagamento;
	
}