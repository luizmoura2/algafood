package com.algaworks.algafood.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.InputAssembler;
import com.algaworks.algafood.api.assembler.OutputAssembler;
import com.algaworks.algafood.api.model.input.FormaPagamentoInput;
import com.algaworks.algafood.api.model.output.FormaPagamentoOutput;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.service.FormaPagamentoService;

@RestController
@RequestMapping("/forma_pagamento")
public class FormaPagamentoController {

	@Autowired
	private FormaPagamentoService formaPagamentoService;
	
	@Autowired
	private OutputAssembler<FormaPagamento, FormaPagamentoOutput> outputAssembler;
	
	@Autowired
	private InputAssembler<FormaPagamentoInput, FormaPagamento> inputAssembler;
	
	@GetMapping
	public List<FormaPagamentoOutput> listar() {
		return outputAssembler.toCollectionModel(formaPagamentoService.find_All(), FormaPagamentoOutput.class) ;
	}
	
	@GetMapping("/{formaPagamentoId}")
	public FormaPagamentoOutput buscar(@PathVariable Long formaPagamentoId) {
		return outputAssembler.toModel(formaPagamentoService.findBy_Id(formaPagamentoId), FormaPagamentoOutput.class);
	}
		
	@PostMapping
	public FormaPagamentoOutput adicionar(@RequestBody FormaPagamentoInput formaPagamentoIn) {
		FormaPagamento formaPagamento = inputAssembler.toDomainObject(formaPagamentoIn, FormaPagamento.class);
		return outputAssembler.toModel(formaPagamentoService.s_save(formaPagamento), FormaPagamentoOutput.class);	
	}
	
	@PutMapping("/{formaPagamentoId}")
	public FormaPagamentoOutput atualizar(@PathVariable Long formaPagamentoId,
									   @RequestBody FormaPagamentoInput formaPagamentoIn) {
		var formaPagamentoAtual = formaPagamentoService.findBy_Id(formaPagamentoId);
		inputAssembler.copyToDomainObject(formaPagamentoIn, formaPagamentoAtual);
		
		try{
			return outputAssembler.toModel(formaPagamentoService.s_save(formaPagamentoAtual), FormaPagamentoOutput.class);		
		} catch (EntidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}			
	}
	
	@DeleteMapping("/{formaPagamentoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long formaPagamentoId) {
		formaPagamentoService.s_delete(formaPagamentoId);		
	}
}