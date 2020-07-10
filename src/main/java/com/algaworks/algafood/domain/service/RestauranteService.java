package com.algaworks.algafood.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.api.model.view.VendaDiaria2;
import com.algaworks.algafood.domain.exception.BDException;
import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.RestauranteNaoEncontradoException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;

@Service
public class RestauranteService {

	@Autowired
	private RestauranteRepository restauranteRepository;
	@Autowired
	private CozinhaService cozinhaService;
	
	@Autowired
	private CidadeService cidadeService;
	
	@Autowired
	private FormaPagamentoService formaPagamentoServic;
	
	public List<Restaurante> find_All() {
		return restauranteRepository.findAll();		
	}
	
	public Page<Restaurante> find_AllPg(Pageable pageable) {
		return restauranteRepository.findAll(pageable);
	}

	public Restaurante findBy_Id(Long restauranteId) {		
		return restauranteRepository.findById(restauranteId)
				.orElseThrow(() ->new RestauranteNaoEncontradoException(restauranteId));		
	}

	@Transactional
	public Restaurante s_save(Restaurante restaurante) {
		
		Long cozinhaId = restaurante.getCozinha().getId();
		if (cozinhaId != null) {
			var cozinha = cozinhaService.findBy_Id(cozinhaId);
			restaurante.setCozinha(cozinha);
			
			var cidadeId = restaurante.getEndereco().getCidade().getId();
			Cidade cidade = cidadeService.findBy_Id(cidadeId);
			
			restaurante.getEndereco().setCidade(cidade);
			
			
		}else {
			throw new CozinhaNaoEncontradaException("A cozinha é obrigatoria");
		}
		try {	
			return restauranteRepository.save(restaurante);
		} catch (DataIntegrityViolationException e) {
			throw new BDException(e.getMostSpecificCause().getMessage());
		}
	}
	
	public void s_flush() {
		restauranteRepository.flush();		
	}

	@Transactional
	public void s_delete(Long restauranteId){
		
		Restaurante restaurante = findBy_Id(restauranteId);
		try {
			restauranteRepository.delete(restaurante);
			restauranteRepository.flush();
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(restauranteId, "Restaurante");
		}
		
	}

	@Transactional
	public Restaurante s_mergeParc(Restaurante source, Restaurante target) {
		return restauranteRepository.mergeParc(source, target);
	}
	
	@Transactional
	public void ativar(Long restauranteId) {
		Restaurante restauranteAtual = findBy_Id(restauranteId);
		
		restauranteAtual.ativar();
	}
	
	@Transactional
	public void inativar(Long restauranteId) {
		Restaurante restauranteAtual = findBy_Id(restauranteId);
		
		restauranteAtual.inativar();
	}
	
	@Transactional
	public void ativar(List<Long> restauranteIds ) {
		restauranteIds.forEach(this::ativar);
	}
	
	@Transactional
	public void inativar(List<Long> restauranteIds ) {
		restauranteIds.forEach(this::inativar);
	}
	
	@Transactional
	public void abrir(Long restauranteId) {
		Restaurante restauranteAtual = findBy_Id(restauranteId);		
		restauranteAtual.abrir();
	}
	
	@Transactional
	public void fechar(Long restauranteId) {
		Restaurante restauranteAtual = findBy_Id(restauranteId);		
		restauranteAtual.fechar();
	}
	
	@Transactional
	public void desassociarFormaPagamento(Long restauranteId, Long formaPagamentoId) {
		Restaurante restaurante = findBy_Id(restauranteId);
		FormaPagamento formaPagamento = formaPagamentoServic.findBy_Id(formaPagamentoId);
		
		restaurante.removerFormaPagamento(formaPagamento);
	}
	
	@Transactional
	public void associarFormaPagamento(Long restauranteId, Long formaPagamentoId) {
		Restaurante restaurante = findBy_Id(restauranteId);
		FormaPagamento formaPagamento = formaPagamentoServic.findBy_Id(formaPagamentoId);
		
		restaurante.adicionarFormaPagamento(formaPagamento);
	}

	public List<VendaDiaria2> findBy_RelDia(String startDate, String endDate) {
		return restauranteRepository.getRelatorioVenda(startDate, endDate);
	}

	
}
