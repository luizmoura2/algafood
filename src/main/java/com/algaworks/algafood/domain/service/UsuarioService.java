package com.algaworks.algafood.domain.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.exception.ObjectNaoEncontradoException;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	public List<Usuario> find_All() {
		return usuarioRepository.findAll();
	}

	public Usuario findBy_Id(Long usuarioId) {
		return usuarioRepository.findById(usuarioId)
				.orElseThrow(() -> new ObjectNaoEncontradoException(usuarioId, "Usuário"));

	}

	@Transactional
	public Usuario s_save(Usuario usuario) {
		usuarioRepository.detach(usuario);
		
		Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(usuario.getEmail());
		
		if (usuarioExistente.isPresent() && !usuarioExistente.get().equals(usuario)) {
			throw new NegocioException(
					String.format("Já existe um usuário cadastrado com o e-mail %s", usuario.getEmail()));
		}
		return usuarioRepository.save(usuario);
	}
	
	@Transactional
	public void s_delete(Long usuarioId) {
		Usuario usuario = findBy_Id(usuarioId);
		try {
			usuarioRepository.delete(usuario);
			usuarioRepository.flush();
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(usuarioId, "Usuario ");
		}
	}

	@Transactional
	public void alterarSenha(Long usuarioId, String senhaAtual, String novaSenha) {
		Usuario usuario = findBy_Id(usuarioId);

		if (usuario.senhaNaoCoincideCom(senhaAtual)) {
			throw new NegocioException("Senha atual informada não coincide com a senha do usuário.");
		}

		usuario.setSenha(novaSenha);
	}

	public void s_flush() {
		usuarioRepository.flush();		
	}

}
