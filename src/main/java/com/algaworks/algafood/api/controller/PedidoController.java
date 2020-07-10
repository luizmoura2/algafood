package com.algaworks.algafood.api.controller;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.InputAssembler;
import com.algaworks.algafood.api.assembler.OutputAssembler;
import com.algaworks.algafood.api.model.input.PedidoInput;
import com.algaworks.algafood.api.model.output.PedidoOutput;
import com.algaworks.algafood.api.model.output.PedidoResumoOutput;
import com.algaworks.algafood.core.data.PageableTranslator;
import com.algaworks.algafood.domain.enums.StatusPedido;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.exception.ObjectNaoEncontradoException;
import com.algaworks.algafood.domain.filter.PedidoFilter;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.service.PedidoService;
import com.algaworks.algafood.infrastructure.repository.spec.PedidoSpecs;
import com.google.common.collect.ImmutableMap;

@RestController
@RequestMapping(value = "/pedidos")
public class PedidoController {

	@Autowired
	private PedidoService pedidoService;
	
	@Autowired
	private OutputAssembler<Pedido, PedidoOutput> outputAssembler;
	
	@Autowired
	private OutputAssembler<Pedido, PedidoResumoOutput> output_Assembler;

	@Autowired
	private InputAssembler<PedidoInput, Pedido> inputAssembler;
		
	//private final PageAssembler<Pedido, PedidoResumoOutput> pageAssembler = null;
	
//	@GetMapping
//	public PageOutput<PedidoResumoOutput> search(PedidoFilter filter, @PageableDefault(size = 8) Pageable pageable) {
//	    Page<Pedido> pedidoPage = pedidoService.find_Allx(PedidoSpecs.usandoFiltro(filter), pageable);
//
//	    return pageAssembler.toCollectionModelPage(pedidoPage, PedidoResumoOutput.class);
//	}
	
	@GetMapping
	public Page<PedidoResumoOutput> pesquisar(PedidoFilter filtro, Pageable pageable) {
		
		pageable = traduzirPageable(pageable);
		Page<Pedido> pedidosPage = pedidoService.find_Allx(PedidoSpecs.usandoFiltro(filtro), pageable);
		
		
		List<PedidoResumoOutput> pedidotOutput =  output_Assembler.toCollectionModel(pedidosPage.getContent(), PedidoResumoOutput.class);
		Page<PedidoResumoOutput> pedidoOutputPage = new PageImpl<>(pedidotOutput, pageable, 
				pedidosPage.getTotalElements());
		return pedidoOutputPage;
	}
	
	@GetMapping("/{codigo}")
	@ResponseStatus(HttpStatus.OK)
	public PedidoOutput buscar(//@PathVariable("codigo") Pedido pedido,
								@PathVariable String codigo) { 
		
		Pedido pedido = pedidoService.findBy_codigo(codigo);
		
		Optional.ofNullable(pedido).orElseThrow(
				() -> new ObjectNaoEncontradoException(codigo, "Pedido"));
		
		return outputAssembler.toModel(pedido, PedidoOutput.class);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public PedidoOutput adicionar(@RequestBody @Valid PedidoInput pedidoInput) {
		Pedido pedido = inputAssembler.toDomainObject(pedidoInput, Pedido.class);
		       pedido.setCliente(new Usuario());
		       pedido.getCliente().setId(1L);
		try {
			return outputAssembler.toModel(pedidoService.s_save(pedido), PedidoOutput.class);
		} catch (EntidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
	}

	@PutMapping("/{codigo}")
	public PedidoOutput atualizar(//@PathVariable("codigo") Pedido pedido, 
									@PathVariable String codigo,
			@RequestBody @Valid PedidoInput pedidoInput) {
		
		Pedido pedido = pedidoService.findBy_codigo(codigo);
		
		Optional.ofNullable(pedido).orElseThrow(
				() -> new ObjectNaoEncontradoException(codigo, "Pedido"));
		
		inputAssembler.copyToDomainObject(pedidoInput, pedido);
		try {

			return outputAssembler.toModel(pedidoService.s_save(pedido), PedidoOutput.class);
		} catch (EntidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
	}

	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(//@PathVariable("codigo") Pedido pedido, 
						@PathVariable String codigo) {
		
		Pedido pedido = pedidoService.findBy_codigo(codigo);
		Optional.ofNullable(pedido).orElseThrow(
				() -> new ObjectNaoEncontradoException(codigo, "Pedido"));
		pedidoService.s_delete(pedido);
	}

	@PatchMapping("/{codigo}")
	public PedidoOutput atualizarParcial(//@PathVariable("codigo") Pedido pedido, 
										 @PathVariable String codigo,
			@RequestBody PedidoInput pedidoInput) {
		
		Pedido pedido = pedidoService.findBy_codigo(codigo);
		Optional.ofNullable(pedido).orElseThrow(
				() -> new ObjectNaoEncontradoException(codigo, "Pedido"));
		
		inputAssembler.copyToDomainObject(pedidoInput, pedido);

		Pedido pedidoIn = inputAssembler.toDomainObject(pedidoInput, Pedido.class);
		pedidoIn = pedidoService.s_mergeParc(pedidoIn, pedido);
		
		return outputAssembler.toModel(pedidoService.s_save(pedidoIn), PedidoOutput.class);
	}
	
	@PutMapping("/{codigo}/{status}")
	public void changeStatus(//@PathVariable("codigo") Pedido pedido, 
							 @PathVariable String codigo, 
							 @PathVariable("status") String status) {
		
		Pedido pedido =pedidoService.findBy_codigo(codigo);
		Optional.ofNullable(pedido).orElseThrow(
				() -> new ObjectNaoEncontradoException(codigo, "Pedido"));
		
		switch (status) {
			case "confirmar":				
				pedido.chgStatus(StatusPedido.CONFIRMADO);
				pedido.setDataConfirmacao(OffsetDateTime.now());
				break;
			case "cancelar":				
				pedido.chgStatus(StatusPedido.CANCELADO);
				pedido.setDataCancelamento(OffsetDateTime.now());
				break;
			case "entregar":				
				pedido.chgStatus(StatusPedido.ENTREGUE);
				pedido.setDataEntrega(OffsetDateTime.now());
				break;
			default:
				throw new NegocioException(
						String.format("O Recurso %s no pedido %d não existe!", status, pedido.getId()));
		}
		pedidoService.s_commit(pedido);
	}
	
	private Pageable traduzirPageable(Pageable apiPageable) {
		var mapeamento = ImmutableMap.of(
				"codigo", "codigo",
				"restaurante.nome", "restaurante.nome",
				"nomeCliente", "cliente.nome",
				"valorTotal", "valorTotal"
			);
		
		return PageableTranslator.translate(apiPageable, mapeamento);
	}
}
