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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.InputAssembler;
import com.algaworks.algafood.api.assembler.OutputAssembler;
import com.algaworks.algafood.api.model.input.CidadeInput;
import com.algaworks.algafood.api.model.output.CidadeOutput;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.service.CidadeService;

@RestController
@RequestMapping("/cidades")
public class CidadeController {

	@Autowired
	private CidadeService cidadeService;
	
	@Autowired
	private OutputAssembler<Cidade, CidadeOutput> outputAssembler;
	
	@Autowired
	private InputAssembler<CidadeInput, Cidade> inputAssembler;
		
	@GetMapping
	public List<CidadeOutput> listar() {
		return outputAssembler.toCollectionModel(cidadeService.find_All(), CidadeOutput.class);
	}
	
	@GetMapping("/{cidadeId}")
	public CidadeOutput buscar(@PathVariable Long cidadeId) {
		return  outputAssembler.toModel(cidadeService.findBy_Id(cidadeId), CidadeOutput.class);
	}
		
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CidadeOutput adicionar(@RequestBody @Valid CidadeInput cidadeInput) {
		try { 
			Cidade cidade = inputAssembler.toDomainObject(cidadeInput, Cidade.class);
			return  outputAssembler.toModel(cidadeService.s_save(cidade), CidadeOutput.class);	
		} catch (EntidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
	}
	
	@PutMapping("/{cidadeId}")
	public CidadeOutput atualizar(@PathVariable Long cidadeId,
							@RequestBody @Valid CidadeInput cidadeInput) {
		
		Cidade cidadeAtual = cidadeService.findBy_Id(cidadeId);
		cidadeAtual.setEstado(new Estado());
		inputAssembler.copyToDomainObject(cidadeInput, cidadeAtual);
				
		try {
			return outputAssembler.toModel(cidadeService.s_save(cidadeAtual), CidadeOutput.class);
		} catch (EntidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}			
	}
	
	@DeleteMapping("/{cidadeId}")
	public void remover(@PathVariable Long cidadeId) {
			cidadeService.s_delete(cidadeId);			
	}
}