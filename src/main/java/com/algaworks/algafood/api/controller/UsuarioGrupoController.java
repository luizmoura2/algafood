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
import com.algaworks.algafood.api.model.output.GrupoOutput;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.exception.ObjectNaoEncontradoException;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.service.UsuarioService;

@RestController
@RequestMapping("/usuarios/{usuarioId}/grupos")
public class UsuarioGrupoController {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private OutputAssembler<Grupo, GrupoOutput> outputAssembler;

	@GetMapping
	public List<GrupoOutput> listar(@PathVariable("usuarioId") Usuario usuario, @PathVariable Long usuarioId) {
		if (usuario == null) {
			throw new ObjectNaoEncontradoException(usuarioId, "Usuario");
		}
		return outputAssembler.toCollectionModel(usuario.getGrupos(), GrupoOutput.class);
	}

	@GetMapping("/{grupoId}")
	public GrupoOutput buscar(@PathVariable("usuarioId") Usuario usuario, @PathVariable Long usuarioId,
			@PathVariable("grupoId") Grupo grupo, @PathVariable Long grupoId) {

		verificar(usuario, usuarioId, grupo, grupoId);
		if (usuario.contemGrupo(grupo)) {
			return outputAssembler.toModel(grupo, GrupoOutput.class);
		} else {
			throw new ObjectNaoEncontradoException(grupoId, "Grupo no Usuario " + usuarioId);
		}

	}

	@PutMapping("/{grupoId}")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public List<GrupoOutput> adicionar(@PathVariable("usuarioId") Usuario usuario, @PathVariable Long usuarioId,
			@PathVariable("grupoId") Grupo grupo, @PathVariable Long grupoId) {
		try {
			verificar(usuario, usuarioId, grupo, grupoId);
			usuario.adicionarGrupo(grupo);

			usuarioService.s_flush();
			return outputAssembler.toCollectionModel(usuario.getGrupos(), GrupoOutput.class);
		} catch (EntidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
	}

	@DeleteMapping("/{grupoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable("usuarioId") Usuario usuario, @PathVariable Long usuarioId,
			@PathVariable("grupoId") Grupo grupo, @PathVariable Long grupoId) {

		verificar(usuario, usuarioId, grupo, grupoId);

		if (usuario.contemGrupo(grupo)) {
			usuario.removerGrupo(grupo);

			usuarioService.s_flush();
		} else {
			throw new ObjectNaoEncontradoException(grupoId, "Grupo no Usuario " + usuarioId);
		}
	}

	public void verificar(Usuario usuario, Long usuarioId, Grupo grupo, Long grupoId) {
		if (usuario == null) {
			throw new ObjectNaoEncontradoException(usuarioId, "Usuario");
		}
		if (grupo == null) {
			throw new ObjectNaoEncontradoException(grupoId, "Grupo ");
		}
	}
}