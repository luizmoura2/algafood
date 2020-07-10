package com.algaworks.algafood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import com.algaworks.algafood.api.model.CozinhasXmlWrapper;
import com.algaworks.algafood.api.model.input.CozinhaInput;
import com.algaworks.algafood.api.model.output.CozinhaOutput;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.service.CozinhaService;

@RestController
@RequestMapping(value = "/cozinhas") //, produces = MediaType.APPLICATION_JSON_VALUE)
public class CozinhaController {

	@Autowired
	private CozinhaService cozinhaService;
	
	@Autowired
	private OutputAssembler<Cozinha, CozinhaOutput> outputAssembler;
	
	@Autowired
	private InputAssembler<CozinhaInput, Cozinha> inputAssembler;
	
	@GetMapping
	public Page<CozinhaOutput> listar(@PageableDefault(size = 2) Pageable pageable) {
		Page<Cozinha> cozinhasPage = cozinhaService.find_All(pageable);
		
		List<CozinhaOutput> cozinhasOutput = outputAssembler.toCollectionModel(cozinhasPage.getContent(), CozinhaOutput.class);
		
		Page<CozinhaOutput> cozinhasModelPage = new PageImpl<>(cozinhasOutput, pageable, 
				cozinhasPage.getTotalElements());
		
		return cozinhasModelPage;
	}
	
	//@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)//Retorna formato json default
	public List<CozinhaOutput> listar1() {
		//System.out.println("LISTAR 1");
		return outputAssembler.toCollectionModel(cozinhaService.find_All(), CozinhaOutput.class);
	}
	
	//@GetMapping//(produces = MediaType.APPLICATION_XML_VALUE) //retorna formato xml
	public List<CozinhaOutput> listar2() {
		//System.out.println("LISTAR 2");
		return outputAssembler.toCollectionModel(cozinhaService.find_All(), CozinhaOutput.class);
	}
	
	@GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
	public CozinhasXmlWrapper listarXml() {
		return new CozinhasXmlWrapper(cozinhaService.find_All());
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/{cozinhaId}")
	public CozinhaOutput Busca(@PathVariable Long cozinhaId) {
		return outputAssembler.toModel(cozinhaService.findBy_Id(cozinhaId), CozinhaOutput.class);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CozinhaOutput adicionar(@RequestBody @Valid CozinhaInput cozinhaIn) {
		Cozinha cozinha = inputAssembler.toDomainObject(cozinhaIn, Cozinha.class);
		return outputAssembler.toModel(cozinhaService.s_save(cozinha), CozinhaOutput.class);
	}
	
	@PutMapping("/{cozinhaId}")
	public CozinhaOutput atualizar(@PathVariable Long cozinhaId,
								   @RequestBody @Valid CozinhaInput cozinhaIn) {
		
		Cozinha cozinhaAtual = cozinhaService.findBy_Id(cozinhaId);
		inputAssembler.copyToDomainObject(cozinhaIn, cozinhaAtual);
				
		return outputAssembler.toModel(cozinhaService.s_save(cozinhaAtual), CozinhaOutput.class);
	}
	
	@DeleteMapping("/{cozinhaId}")
	public void remover(@PathVariable Long cozinhaId) {		
		cozinhaService.s_delete(cozinhaId);
		
	}
}