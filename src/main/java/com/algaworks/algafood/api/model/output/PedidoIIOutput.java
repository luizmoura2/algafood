package com.algaworks.algafood.api.model.output;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name="pedido_view")
@NamedNativeQuery(name = "Pedido.findAllP",
	query = "select p.* from pedido_view p",
	resultClass = PedidoIIOutput.class
)
public class PedidoIIOutput { 
	@Id
	private Long id;
    private Long pedidoid;
    private String restaurante; 
    private BigDecimal taxa_frete;  
    private BigDecimal subtotal; 
    private BigDecimal total; 
    private String forma_agamento;
    private String cliente; 
    private String email; 
    private String cep; 
    private String rua; 
    private String nr; 
    private String complemento; 
    private String bairro; 
    private String status; 
    private OffsetDateTime data_pedido; 
    private OffsetDateTime data_confirmacao; 
    private OffsetDateTime cancelamento;
    private OffsetDateTime entrega; 
    private Integer quantidade; 
    private BigDecimal preco_unitario; 
    private BigDecimal preco_total;
    private String observacao; 
    private Long pedido_id; 
    private Long produto_id;
}