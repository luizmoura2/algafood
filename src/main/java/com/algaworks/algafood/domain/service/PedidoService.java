package com.algaworks.algafood.domain.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.api.model.output.PedidoIIOutput;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.exception.ObjectNaoEncontradoException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.model.ItemPedido;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.PedidoRepository;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private RestauranteService restauranteService;
	
	@Autowired
	private CidadeService cidadeService;
	
	@Autowired
	private FormaPagamentoService formaPagamentoService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	public List<Pedido> find_All() {
		return pedidoRepository.findAll();
	}
	
	public List<PedidoIIOutput> find_AllP() {
		var obj = pedidoRepository.findAllP();
		
		return obj;
	}

	public Pedido findBy_Id(Long pedidoId) {
		return pedidoRepository.findById(pedidoId)
				.orElseThrow(() -> new ObjectNaoEncontradoException(pedidoId, "Pedido"));
	}
	
	public Pedido findBy_codigo(String codigo) {
		return pedidoRepository.findByCodigo(codigo)
				.orElseThrow(() -> new ObjectNaoEncontradoException(codigo, "Pedido"));
	}
	
	@Transactional
	public Pedido s_save(Pedido pedido) {	
		
		/**
		 * Selecionar o restaurante se não existe emite Exceção */
		Restaurante restaurante = restauranteService.findBy_Id(pedido.getRestaurante().getId());
		pedido.setRestaurante(restaurante);
		pedido.setTaxaFrete(restaurante.getTaxaFrete());
		
		vfyFormaPagamento(pedido);	
		vfyItensPedido(pedido);		
		
		pedido.calcularValorTotal();
		return pedidoRepository.save(pedido);
	}

	/**
	 * Verificar se a forma de pagamento consta no Restaurante, se não, emite Exceção. */
	public void vfyFormaPagamento(Pedido pedido) {
		/**
		 * Cauptura a formaPagamento a ser cadastrado no pedido*/
		FormaPagamento formaPagameno = pedido.getFormaPagamento();
		/**
		 * Captura a listra de formas de pagamento cadastradas no Restaurante */
		Set<FormaPagamento> formPagamento_s = pedido.getRestaurante().getFormasPagamento();
		
		/**
		 * Verifica se a forma de pagamento do pedido esta contida
		 * na lista de Formas de pagamentos cadastrados no Restaurante */
		if (formPagamento_s.contains(formaPagameno)) {
			/**
			 * Percorre a lista de formas de pagamentos cadastradso no Restaurante */
			formPagamento_s.forEach( formaPg ->{
				
						if (formaPg.getId() == formaPagameno.getId()) {
							pedido.setFormaPagamento(formaPg);
						}				
			});	
		}else {
		  throw new ObjectNaoEncontradoException(formaPagameno.getId(), "Forma de Pagamento");		
		}
	}
	
	/**
	 * Verificar os itens se o produto pertence ao Restaurante,
	 * se pertence calcula o valor do item do pedido, se não
	 * emite Exceção */
	public void vfyItensPedido(Pedido pedido) {
		
		/**
		 * Pega os produtos cadastrado no restaurante */
		List<Produto> produtos = pedido.getRestaurante().getProdutos(); 
		
		/**
		 * Pega o Itens do pedido a serem salvos */
		List<ItemPedido> itensPedido = pedido.getItens();
		
		/**
		 * Percorre a lista de pedido a serem salvos */
		itensPedido.forEach( item ->{	
			/**
			 *Captura o quantidade de itens do pedido 
			 *a serem salvos */
			int j = (int) produtos.stream().count();
			
			/**
			 * Verifica so o produto no item a ser salvo 
			 * pertence aos produtos cadastrados no restaurante */
			if (produtos.contains(item.getProduto())){			  
			   
			  for(int i=0; i<j; i++) {
				/**
				 * Pega um produto na lista de produtos
				 * cadastrado no restaurante */
				Produto produto = produtos.get(i);
				
				/**
				 * Verifica se o produto que foi pego na lista de
				 * produtos cadastrado no restaurante é igual ao produto
				 * do item a ser salvo */
				if (item.getProduto().getId() == produto.getId()) {
									
					item.setId(null);
					
					BigDecimal preco = produto.getPreco();
					item.setPrecoUnitario(preco);
					
					BigDecimal quantidade = BigDecimal.valueOf(item.getQuantidade().floatValue());
					preco = preco.multiply(quantidade);
					item.setPrecoTotal(preco);			
					item.setProduto(produto);		
					item.setPedido(pedido);
					pedido.setSubtotal(preco);		
					preco = BigDecimal.ZERO;				
				}
			  }
			}else {
				throw new ObjectNaoEncontradoException(item.getProduto().getId(), 
									"Produto no Restaurante "+pedido.getRestaurante().getId());
			}			
		});	
	}
	
	@Transactional
	public Pedido emitir(Pedido pedido) {
	    pedido = validarPedido(pedido);
	    pedido = validarItens(pedido);
	    
	    pedido.setTaxaFrete(pedido.getRestaurante().getTaxaFrete());
	    
	    pedido.calcularValorTotal();

	    return pedidoRepository.save(pedido);
	}

	private Pedido validarPedido(Pedido pedido) {
		Restaurante restaurante = restauranteService.findBy_Id(pedido.getRestaurante().getId());
		Cidade cidade = cidadeService.findBy_Id(pedido.getEnderecoEntrega().getCidade().getId());
	    Usuario cliente = usuarioService.findBy_Id(pedido.getCliente().getId());
	    
	    FormaPagamento formaPagamento = formaPagamentoService.findBy_Id(pedido.getFormaPagamento().getId());

	    pedido.getEnderecoEntrega().setCidade(cidade);
	    pedido.setCliente(cliente);
	    pedido.setRestaurante(restaurante);
	    pedido.setFormaPagamento(formaPagamento);
	    
	    if (restaurante.naoAceitaFormaPagamento(formaPagamento)) {
	        throw new NegocioException(String.format("Forma de pagamento '%s' não é aceita por esse restaurante.",
	                formaPagamento.getDescricao()));
	    }
	    return pedido;
	}

	private Pedido validarItens(Pedido pedido) {
		
		List<Produto> produtos= pedido.getRestaurante().getProdutos();
		 
	    pedido.getItens().forEach(item-> {
	    	Produto produto = produtos.get(produtos.indexOf(item.getProduto()));
	    	Optional.ofNullable(produto).orElseThrow(
					() -> new ObjectNaoEncontradoException(item.getId(), "Item"));
	        item.setPedido(pedido);
	        item.setProduto(produto);
	        item.setPrecoUnitario(produto.getPreco());
	        pedido.setSubtotal(produto.getPreco());
	    });
	    
	    return pedido;
	}
	
	public void s_delete(Pedido pedido) {
		
		try {
			pedidoRepository.delete(pedido);
			pedidoRepository.flush();
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(pedido.getId(), "Pedido ");
		}
	}
	
	@Transactional
	public Pedido s_mergeParc(Pedido source, Pedido target) {
		return pedidoRepository.mergeParc(source, target);
	}
	
	@Transactional
	public void s_commit(Pedido pedido) {
		pedidoRepository.flush();
	}

	public Page<Pedido> find_Allx(Specification<Pedido> usandoFiltro, Pageable pageable) {
		return pedidoRepository.findAll(usandoFiltro, pageable);
	};

}
