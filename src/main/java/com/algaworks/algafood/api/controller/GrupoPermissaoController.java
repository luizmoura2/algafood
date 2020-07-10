package com.algaworks.algafood.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.OutputAssembler;
import com.algaworks.algafood.api.model.output.PermissaoOutput;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.exception.ObjectNaoEncontradoException;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.model.Permissao;
import com.algaworks.algafood.domain.service.GrupoService;

@RestController
@RequestMapping("/grupos/{grupoId}/permissoes")
public class GrupoPermissaoController {

	@Autowired
	private GrupoService grupoService;

	@Autowired
	private OutputAssembler<Permissao, PermissaoOutput> outputAssembler;

	@GetMapping
	public List<PermissaoOutput> listar(@PathVariable("grupoId") Grupo grupo, @PathVariable Long grupoId) {
		if (grupo == null) {
			throw new ObjectNaoEncontradoException(grupoId, "Grupo");
		}
		return outputAssembler.toCollectionModel(grupo.getPermissoes(), PermissaoOutput.class);
	}

	@GetMapping("/{permissaoId}")
	public PermissaoOutput buscar(@PathVariable("grupoId") Grupo grupo, @PathVariable Long grupoId,
			@PathVariable("permissaoId") Permissao permissao, @PathVariable Long permissaoId) {

		verificar(grupo,grupoId, permissao, permissaoId);
		if (grupo.contemPermissao(permissao)) {
			return outputAssembler.toModel(permissao, PermissaoOutput.class);
		} else {
			throw new ObjectNaoEncontradoException(permissaoId, "Permissao no Grupo " + grupoId);
		}

	}

	@PutMapping("/{permissaoId}")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public List<PermissaoOutput> adicionar(@PathVariable("grupoId") Grupo grupo, @PathVariable Long grupoId,
			@PathVariable("permissaoId") Permissao permissao, @PathVariable Long permissaoId) {
		try {
			verificar(grupo,grupoId, permissao, permissaoId);
			grupo.adicionarPermissao(permissao);
			
			grupoService.s_flush();
			return outputAssembler.toCollectionModel(grupo.getPermissoes(), PermissaoOutput.class);
		} catch (EntidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
	}

	@DeleteMapping("/{permissaoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable("grupoId") Grupo grupo, @PathVariable Long grupoId,
			@PathVariable("permissaoId") Permissao permissao, @PathVariable Long permissaoId) {

		verificar(grupo,grupoId, permissao, permissaoId);
		
		if (grupo.contemPermissao(permissao)) {
			grupo.removerPermissao(permissao);
			
			grupoService.s_flush();
		} else {
			throw new ObjectNaoEncontradoException(permissaoId, "Permissao no Grupo " + grupoId);
		}
	}
	
	public void verificar(Grupo grupo, Long grupoId, Permissao permissao, Long permissaoId) {
		if (grupo == null) {
			throw new ObjectNaoEncontradoException(grupoId, "Grupo ");
		}
		if (permissao == null) {
			throw new ObjectNaoEncontradoException(permissaoId, "Permissao ");
		}
	}
}