package com.algaworks.algafood.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.CidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.repository.GrupoRepository;

@Service
public class GrupoService {

	@Autowired
	private GrupoRepository grupoRepository;
		
	public List<Grupo> find_All() {
		return grupoRepository.findAll();
	}

	public Grupo findBy_Id(Long grupoId) {	
		return grupoRepository.findById(grupoId).orElseThrow(()->new CidadeNaoEncontradaException( grupoId ));
		
	}

	@Transactional
	public Grupo s_save(Grupo grupo) {
		grupoRepository.save(grupo);
		
		return findBy_Id(grupo.getId());
	}
	
	@Transactional
	public void s_flush() {
		grupoRepository.flush();
	}

	@Transactional
	public void s_delete(Long grupoId) {
		 Grupo grupo = findBy_Id(grupoId);
		 try {
			 grupoRepository.delete(grupo);
			 grupoRepository.flush();
	     } catch (DataIntegrityViolationException e) {
	         throw new EntidadeEmUsoException(grupoId, "Grupo ");
	     }
     }
}
