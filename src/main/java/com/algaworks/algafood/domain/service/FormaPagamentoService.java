package com.algaworks.algafood.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.FormaPagamentoNaoEncontradaException;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.repository.FormaPagamentoRepository;

@Service
public class FormaPagamentoService {

	@Autowired
	private FormaPagamentoRepository formaPagamentoRepository;
		
	public List<FormaPagamento> find_All() {
		return formaPagamentoRepository.findAll();
	}

	public FormaPagamento findBy_Id(Long formaPagamentoId) {
		
		return formaPagamentoRepository.findById(formaPagamentoId)
				.orElseThrow(() ->new FormaPagamentoNaoEncontradaException(formaPagamentoId));		
	}

	@Transactional
	public FormaPagamento s_save(FormaPagamento formaPagamento) {
		
		return formaPagamentoRepository.save(formaPagamento);
	}

	@Transactional
	public void s_delete(Long formaPagamentoId) {
		 var formaPagamento = findBy_Id(formaPagamentoId);
		 try {
             	formaPagamentoRepository.delete(formaPagamento); 
             	formaPagamentoRepository.flush();
	         } catch (DataIntegrityViolationException e) {
	             throw new EntidadeEmUsoException( formaPagamentoId, "Forma pagamento" );
	         }
     }

	
}
