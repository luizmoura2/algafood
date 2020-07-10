package com.algaworks.algafood.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.ObjectNaoEncontradoException;
import com.algaworks.algafood.domain.model.Permissao;
import com.algaworks.algafood.domain.repository.PermissaoRepository;

@Service
public class PermissaoService {

	@Autowired
	private PermissaoRepository permissaoRepository;
		
	public List<Permissao> find_All() {
		return permissaoRepository.findAll();
	}

	public Permissao findBy_Id(Long permissaoId) {
		
		return permissaoRepository.findById(permissaoId)
				.orElseThrow(() ->new ObjectNaoEncontradoException(permissaoId, "Permissão "));
	}

	@Transactional
	public Permissao s_save(Permissao permissao) {
		
		return permissaoRepository.save(permissao);
	}

	@Transactional
	public void s_delete(Long permissaoId) {
		
		Permissao permissao = findBy_Id(permissaoId);
		
		try {
			permissaoRepository.delete(permissao); 
			permissaoRepository.flush();
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException( permissaoId, "Permissão " );
		}
     }

	
}
