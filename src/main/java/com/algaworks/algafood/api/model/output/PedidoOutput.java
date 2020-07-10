package com.algaworks.algafood.api.model.output;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import com.algaworks.algafood.domain.enums.StatusPedido;
import com.algaworks.algafood.domain.model.Endereco;
import com.algaworks.algafood.domain.model.FormaPagamento;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PedidoOutput {
	
	private String codigo;	
    private BigDecimal subtotal;
    private BigDecimal taxaFrete;
    private BigDecimal valorTotal;
    private Endereco enderecoEntrega;
    private StatusPedido status;
    private OffsetDateTime dataCriacao;
    private OffsetDateTime dataConfirmacao;
    private FormaPagamento formaPagamento;
    //@JsonProperty("Restaurante")
	private RestauranteResumoOutput restaurante;
    //@JsonProperty("Cliente")
    private ClienteOutput cliente;
    private List<ItemPedidoOutput> itens;

}