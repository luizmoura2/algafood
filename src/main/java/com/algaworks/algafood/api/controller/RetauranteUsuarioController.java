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
import com.algaworks.algafood.api.model.output.UsuarioOutput;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.exception.ObjectNaoEncontradoException;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.service.RestauranteService;

@RestController
@RequestMapping(value = "/restaurantes/{restauranteId}/usuario")
public class RetauranteUsuarioController {

	@Autowired
	private RestauranteService restauranteService;

	@Autowired
	private OutputAssembler<Usuario, UsuarioOutput> outputAssembler;

	@GetMapping
	public List<UsuarioOutput> listar(@PathVariable("restauranteId") Restaurante restaurante,
			@PathVariable Long restauranteId) {
		if (restaurante == null) {
			throw new ObjectNaoEncontradoException(restauranteId, "Restaurante");
		}
		return outputAssembler.toCollectionModel(restaurante.getUsuarios(), UsuarioOutput.class);
	}

	@GetMapping("/{usuarioId}")
	public UsuarioOutput buscar(@PathVariable("restauranteId") Restaurante restaurante,
			@PathVariable Long restauranteId, @PathVariable("usuarioId") Usuario usuario,
			@PathVariable Long usuarioId) {

		verificar(restaurante, restauranteId, usuario, usuarioId);
		if (restaurante.contemUsuario(usuario)) {
			return outputAssembler.toModel(usuario, UsuarioOutput.class);
		} else {
			throw new ObjectNaoEncontradoException(usuarioId, "Usuario no Restaurante " + restauranteId);
		}

	}

	@PutMapping("/{usuarioId}")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public List<UsuarioOutput> adicionar(@PathVariable("restauranteId") Restaurante restaurante,
			@PathVariable Long restauranteId, @PathVariable("usuarioId") Usuario usuario,
			@PathVariable Long usuarioId) {
		try {
			verificar(restaurante, restauranteId, usuario, usuarioId);
			restaurante.adicionarUsuario(usuario);

			restauranteService.s_flush();
			return outputAssembler.toCollectionModel(restaurante.getUsuarios(), UsuarioOutput.class);
		} catch (EntidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
	}

	@DeleteMapping("/{usuarioId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable("restauranteId") Restaurante restaurante, @PathVariable Long restauranteId,
			@PathVariable("usuarioId") Usuario usuario, @PathVariable Long usuarioId) {

		verificar(restaurante, restauranteId, usuario, usuarioId);

		if (restaurante.contemUsuario(usuario)) {
			restaurante.removerUsuario(usuario);

			restauranteService.s_flush();
		} else {
			throw new ObjectNaoEncontradoException(usuarioId, "Usuario no Restaurante " + restauranteId);
		}
	}

	public void verificar(Restaurante restaurante, Long restauranteId, Usuario usuario, Long usuarioId) {
		if (restaurante == null) {
			throw new ObjectNaoEncontradoException(restauranteId, "Restaurante");
		}
		if (usuario == null) {
			throw new ObjectNaoEncontradoException(usuarioId, "Usuario");
		}

	}
}