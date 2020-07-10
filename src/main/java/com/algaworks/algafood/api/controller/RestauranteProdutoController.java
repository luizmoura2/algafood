package com.algaworks.algafood.api.controller;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.InputAssembler;
import com.algaworks.algafood.api.assembler.OutputAssembler;
import com.algaworks.algafood.api.model.input.ProdutoInput;
import com.algaworks.algafood.api.model.output.ProdutoOutput;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.exception.ObjectNaoEncontradoException;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.service.ProdutoService;
import com.algaworks.algafood.domain.service.RestauranteService;

@RestController
@RequestMapping(value = "/restaurantes/{restauranteId}/produto")
public class RestauranteProdutoController {

	@Autowired
	private RestauranteService restauranteService;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private OutputAssembler<Produto, ProdutoOutput> outputAssembler;
	
	@Autowired
	private InputAssembler<ProdutoInput, Produto> inputAssembler;
	
	@GetMapping
	public List<ProdutoOutput> listar(@PathVariable Long restauranteId, @RequestParam(required = false) boolean todos) {
		
		/*tem que verificar se o restaurante existe*/
		Restaurante restaurante = restauranteService.findBy_Id(restauranteId);
		
		List<Produto> todosProdutos = restaurante.getProdutos();;
		
		if (!todos) {
			todosProdutos = produtoService.findByAtivos(todosProdutos);
		}
		
		return outputAssembler.toCollectionModel(todosProdutos, ProdutoOutput.class);
	}
	
	@GetMapping("/{id}")
	public ProdutoOutput buscar(@PathVariable Long id, @PathVariable("id") Produto produto, @PathVariable Long restauranteId) {
		
		if (produto == null) {
			throw  new ObjectNaoEncontradoException(id, "Produto ");
		}
		
		Long ids = produto.getRestaurante().getId();
		if (ids != restauranteId) {
			throw  new ObjectNaoEncontradoException(produto.getId(), "Produto no Restaurante "+restauranteId);
		}	
		return  outputAssembler.toModel(produto, ProdutoOutput.class);		
	}
	
	@PutMapping("/{produtoId}")
	public ProdutoOutput atualizar(@PathVariable Long produtoId, @PathVariable Long restauranteId, 
								   @RequestBody @Valid ProdutoInput produtoIn) {
		try {
			Produto produtoAtual = produtoService.findBy_Id(produtoId);
			Long id = produtoAtual.getRestaurante().getId();
			if (id != restauranteId) {
				throw  new ObjectNaoEncontradoException(produtoId, "Produto no Restaurante "+restauranteId);
			}
			
			inputAssembler.copyToDomainObject(produtoIn, produtoAtual);
		
			return outputAssembler.toModel(produtoService.s_save(produtoAtual), ProdutoOutput.class);
		} catch (EntidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}			
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ProdutoOutput adicionar(@RequestBody @Valid ProdutoInput produtoIn, @PathVariable Long restauranteId) {
		try { 
			Produto produto = inputAssembler.toDomainObject(produtoIn, Produto.class);
			Restaurante restaurante = restauranteService.findBy_Id(restauranteId);
			produto.setRestaurante(restaurante);
			
			return  outputAssembler.toModel(produtoService.s_save(produto), ProdutoOutput.class);	
		} catch (EntidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
	}

	@DeleteMapping("/{produtoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long produtoId,  @PathVariable Long restauranteId) {
			produtoService.s_delete(produtoId, restauranteId);	
			
	}
}