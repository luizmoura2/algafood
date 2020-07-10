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
import com.algaworks.algafood.api.model.input.GrupoInput;
import com.algaworks.algafood.api.model.output.GrupoOutput;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.service.GrupoService;

@RestController
@RequestMapping("/grupos")
public class GrupoController {

	@Autowired
	private GrupoService grupoService;
	
	@Autowired
	private OutputAssembler<Grupo, GrupoOutput> outputAssembler;
	
	@Autowired
	private InputAssembler<GrupoInput, Grupo> inputAssembler;
		
	@GetMapping
	public List<GrupoOutput> listar() {
		return outputAssembler.toCollectionModel(grupoService.find_All(), GrupoOutput.class);
	}
	
	@GetMapping("/{grupoId}")
	public GrupoOutput buscar(@PathVariable Long grupoId) {
		return  outputAssembler.toModel(grupoService.findBy_Id(grupoId), GrupoOutput.class);
	}
		
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public GrupoOutput adicionar(@RequestBody @Valid GrupoInput grupoIn) {
		try { 
			Grupo grupo = inputAssembler.toDomainObject(grupoIn, Grupo.class);
			return  outputAssembler.toModel(grupoService.s_save(grupo), GrupoOutput.class);	
		} catch (EntidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
	}
	
	@PutMapping("/{grupoId}")
	public GrupoOutput atualizar(@PathVariable Long grupoId,
							@RequestBody @Valid GrupoInput grupoIn) {
		
		Grupo grupoAtual = grupoService.findBy_Id(grupoId);		
		inputAssembler.copyToDomainObject(grupoIn, grupoAtual);
				
		try {
			return outputAssembler.toModel(grupoService.s_save(grupoAtual), GrupoOutput.class);
		} catch (EntidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}			
	}
	
	@DeleteMapping("/{grupoId}")
	public void remover(@PathVariable Long grupoId) {
			grupoService.s_delete(grupoId);			
	}
}