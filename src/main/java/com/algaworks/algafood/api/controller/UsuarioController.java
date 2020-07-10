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
import com.algaworks.algafood.api.model.input.SenhaInput;
import com.algaworks.algafood.api.model.input.UsuarioInput;
import com.algaworks.algafood.api.model.output.UsuarioOutput;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.service.UsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private OutputAssembler<Usuario, UsuarioOutput> outputAssembler;
	
	@Autowired
	private InputAssembler<UsuarioInput, Usuario> inputAssembler;
		
	@GetMapping
	public List<UsuarioOutput> listar() {
		return outputAssembler.toCollectionModel(usuarioService.find_All(), UsuarioOutput.class);
	}
	
	@GetMapping("/{usuarioId}")
	public UsuarioOutput buscar(@PathVariable Long usuarioId) {
		return  outputAssembler.toModel(usuarioService.findBy_Id(usuarioId), UsuarioOutput.class);
	}
		
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UsuarioOutput adicionar(@RequestBody @Valid UsuarioInput usuarioIn) {
		try { 
			Usuario usuario = inputAssembler.toDomainObject(usuarioIn, Usuario.class);
			return  outputAssembler.toModel(usuarioService.s_save(usuario), UsuarioOutput.class);	
		} catch (EntidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
	}
	
	@PutMapping("/{usuarioId}")
	public UsuarioOutput atualizar(@PathVariable Long usuarioId,
							@RequestBody @Valid UsuarioInput usuarioIn) {
		
		Usuario usuarioAtual = usuarioService.findBy_Id(usuarioId);		
		inputAssembler.copyToDomainObject(usuarioIn, usuarioAtual);
			
		try {
			return outputAssembler.toModel(usuarioService.s_save(usuarioAtual), UsuarioOutput.class);
		} catch (EntidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}			
	}
	
	@DeleteMapping("/{usuarioId}")
	public void remover(@PathVariable Long usuarioId) {
			usuarioService.s_delete(usuarioId);			
	}
	
	@PutMapping("/{usuarioId}/senha")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void alterarSenha(@PathVariable Long usuarioId, @RequestBody @Valid SenhaInput senha) {
        usuarioService.alterarSenha(usuarioId, senha.getSenhaAtual(), senha.getNovaSenha());
    }   
}