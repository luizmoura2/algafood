package com.algaworks.algafood.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EstadoNaoEncontradaException;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.EstadoRepository;

@Service
public class EstadoService {
	
	@Autowired
	private EstadoRepository estadoRepository;
		
	public List<Estado> find_All() {
		return estadoRepository.findAll();
	}

	public Estado findBy_Id(Long estadoId) {		
		return estadoRepository.findById(estadoId)
				.orElseThrow(() ->new EstadoNaoEncontradaException(estadoId));		
	}

	@Transactional
	public Estado s_save(Estado estado) {
		
		return estadoRepository.save(estado); 
	}

	@Transactional
	public void s_delete(Long estadoId){
		try {
			Estado estado = findBy_Id(estadoId);
			estadoRepository.delete(estado);
			estadoRepository.flush();
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException( estadoId, "Estado ");
		}		
	}
	
}
