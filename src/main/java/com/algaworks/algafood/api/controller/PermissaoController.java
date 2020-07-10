package com.algaworks.algafood.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.InputAssembler;
import com.algaworks.algafood.api.assembler.OutputAssembler;
import com.algaworks.algafood.api.model.input.PermissaoInput;
import com.algaworks.algafood.api.model.output.PermissaoOutput;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Permissao;
import com.algaworks.algafood.domain.service.PermissaoService;

@RestController
@RequestMapping("/permissoes")
public class PermissaoController {

	@Autowired
	private PermissaoService permissaoService;
	
	@Autowired
	private OutputAssembler<Permissao, PermissaoOutput> outputAssembler;
	
	@Autowired
	private InputAssembler<PermissaoInput, Permissao> inputAssembler;
	
	@GetMapping
	public List<PermissaoOutput> listar() {
		return outputAssembler.toCollectionModel(permissaoService.find_All(), PermissaoOutput.class);
	}
	
	@GetMapping("/{permissaoId}")
	public PermissaoOutput buscar(@PathVariable Long permissaoId) {
		return outputAssembler.toModel(permissaoService.findBy_Id(permissaoId), PermissaoOutput.class);
		
	}
		
	@PostMapping
	public PermissaoOutput adicionar(@RequestBody PermissaoInput permissaoIn) {
		///permissao.setId(null);
		Permissao permissao= inputAssembler.toDomainObject(permissaoIn, Permissao.class);
					
		return outputAssembler.toModel(permissaoService.s_save(permissao), PermissaoOutput.class);
		
	}
	
	@PutMapping("/{permissaoId}")
	public PermissaoOutput atualizar(@PathVariable Long permissaoId,
									   @RequestBody PermissaoInput permissaoIn) {
		var permissaoAtual = permissaoService.findBy_Id(permissaoId);
			
			inputAssembler.copyToDomainObject(permissaoIn, permissaoAtual);
			try{
				return outputAssembler.toModel(permissaoService.s_save(permissaoAtual), PermissaoOutput.class);				
			} catch (EntidadeNaoEncontradaException e) {
				throw new NegocioException(e.getMessage());
			}			
	}
	
	@DeleteMapping("/{permissaoId}")
	public void remover(@PathVariable Long permissaoId) {
		permissaoService.s_delete(permissaoId);		
	}
}