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
import com.algaworks.algafood.api.model.input.EstadoInput;
import com.algaworks.algafood.api.model.output.EstadoOutput;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.service.EstadoService;

@RestController
@RequestMapping("/estados")
public class EstadoController {

	@Autowired
	private EstadoService estadoService;
	
	@Autowired
	private OutputAssembler<Estado, EstadoOutput> outputAssembler;
	
	@Autowired
	private InputAssembler<EstadoInput, Estado> inputAssembler;
	
	
	@GetMapping
	public List<EstadoOutput> listar() {
		return outputAssembler.toCollectionModel(estadoService.find_All(), EstadoOutput.class);
	}
	
	@GetMapping("/{estadoId}")
	public EstadoOutput buscar(@PathVariable Long estadoId) {
		return outputAssembler.toModel(estadoService.findBy_Id(estadoId), EstadoOutput.class);
	}
		
	@PostMapping
	public EstadoOutput adicionar(@RequestBody @Valid EstadoInput estadoIn) {
		Estado estado = inputAssembler.toDomainObject(estadoIn, Estado.class);
		return outputAssembler.toModel(estadoService.s_save(estado), EstadoOutput.class);			
				
	}
	
	@PutMapping("/{estadoId}")
	public EstadoOutput atualizar(@PathVariable Long estadoId,
									   @RequestBody @Valid EstadoInput estadoIn) {
		Estado estadoAtual = estadoService.findBy_Id(estadoId);		
		inputAssembler.copyToDomainObject(estadoIn, estadoAtual);
		
		return outputAssembler.toModel(estadoService.s_save(estadoAtual), EstadoOutput.class);		
		
	}
	
	@DeleteMapping("/{estadoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long estadoId) {
			estadoService.s_delete(estadoId);					
	}
}