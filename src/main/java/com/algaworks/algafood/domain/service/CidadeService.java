package com.algaworks.algafood.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.CidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.CidadeRepository;

@Service
public class CidadeService {

	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private EstadoService estadoService;
		
	public List<Cidade> find_All() {
		return cidadeRepository.findAll();
	}

	public Cidade findBy_Id(Long cidadeId) {	
		return cidadeRepository.findById(cidadeId).orElseThrow(()->new CidadeNaoEncontradaException( cidadeId ));
		
	}

	@Transactional
	public Cidade s_save(Cidade cidade) {
		Long id = cidade.getEstado().getId();
		Estado estado = estadoService.findBy_Id(id);
		cidade.setEstado(estado);
		return cidadeRepository.save(cidade);
	}

	@Transactional
	public void s_delete(Long cidadeId) {
		 Cidade cidade = findBy_Id(cidadeId);
		 try {
              cidadeRepository.delete(cidade);
              cidadeRepository.flush();
	         } catch (DataIntegrityViolationException e) {
	             throw new EntidadeEmUsoException(cidadeId, "Cidade ");
	         }
     }
}
