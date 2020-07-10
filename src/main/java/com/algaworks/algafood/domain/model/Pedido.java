package com.algaworks.algafood.domain.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;

import org.hibernate.annotations.CreationTimestamp;

import com.algaworks.algafood.domain.enums.StatusPedido;
import com.algaworks.algafood.domain.exception.NegocioException;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Pedido {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String codigo;
    private BigDecimal subtotal;
    private BigDecimal taxaFrete;
    private BigDecimal valorTotal;

    @Embedded
    private Endereco enderecoEntrega;
    
    @Enumerated(EnumType.STRING)
    private StatusPedido status = StatusPedido.CRIADO;;
    
    @CreationTimestamp
    private OffsetDateTime dataCriacao;

    private OffsetDateTime dataConfirmacao;
    private OffsetDateTime dataCancelamento;
    private OffsetDateTime dataEntrega;
    
    @ManyToOne
    @JoinColumn(nullable = false)
    private FormaPagamento formaPagamento;
    
    @ManyToOne
    @JoinColumn(nullable = false)
    private Restaurante restaurante;
    
    @ManyToOne
    @JoinColumn(name = "usuario_cliente_id", nullable = false)
    private Usuario cliente;
    
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<ItemPedido> itens = new ArrayList<>();

    
    public void calcularValorTotal() {
        this.subtotal = getItens().stream()
            .map(item -> item.getPrecoTotal())
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println("frete" + this.taxaFrete + "subtotal "+ this.subtotal);
        this.valorTotal = this.subtotal.add(this.taxaFrete);
    }

    public void definirFrete() {
        setTaxaFrete(getRestaurante().getTaxaFrete());
    }

    public void atribuirPedidoAosItens() {
        getItens().forEach(item -> item.setPedido(this));
    }

    /**
     * Checks whether the order status can be changed 
     * if positive, changes the status
     * @param newStatus The new status to be entered
     */
    public void chgStatus(StatusPedido newStatus) {
		if (getStatus().setChecks(newStatus)) {
			throw new NegocioException(
					String.format("Status do pedido %d não pode ser alterado de %s para %s",
							getId(), getStatus().getDescricao(), 
							newStatus.getDescricao()));
		}
		
		this.status = newStatus;
	}
    
    /**
     * Executado antes de persistir no banco de dados. 
     */
    @PrePersist
	private void gerarCodigo() {
		setCodigo(UUID.randomUUID().toString());
	}
    
	public String toXString() {
		return "Pedido [id=" + id + ", subtotal=" + subtotal + ", taxaFrete=" + taxaFrete + ", valorTotal=" + valorTotal
				+ ", enderecoEntrega=" + enderecoEntrega + ", status=" + status + ", dataCriacao=" + dataCriacao
				+ ", dataConfirmacao=" + dataConfirmacao + ", dataCancelamento=" + dataCancelamento + ", dataEntrega="
				+ dataEntrega +  "]";
	}
    
}