package com.algaworks.algafood.domain.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;

@Service
public class CozinhaService {
	
	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	public Cozinha findBy_Id(Long cozinhaId) {
		return cozinhaRepository.findById(cozinhaId)
				.orElseThrow(() -> new CozinhaNaoEncontradaException(cozinhaId));		
	}

	public List<Cozinha> find_All() {		
		return cozinhaRepository.findAll();
	}

	public List<Cozinha> findBy_Nome(String nome) {
		return cozinhaRepository.findByNome(nome);
	}
	
	@Transactional
	public Cozinha s_save(Cozinha cozinha) {
		return cozinhaRepository.save(cozinha);
	}

	@Transactional
	public void s_delete(Long cozinhaId) {
		try {
			cozinhaRepository.deleteById(cozinhaId);
			cozinhaRepository.flush();
			
		} catch (EmptyResultDataAccessException e) {
			throw new CozinhaNaoEncontradaException(cozinhaId);
		
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(cozinhaId, "Cozinha ");
		}
	}

	public Optional<Cozinha> findUnicaBy_Nome(String nome) {
		return cozinhaRepository.findUnicaByNome(nome);
	}

	public Optional<Cozinha> buscarPrimeiro() {
		
		return cozinhaRepository.buscarPrimeiro();
	}

	public Page<Cozinha> find_All(Pageable pageable) {
		return cozinhaRepository.findAll(pageable);
	}

}