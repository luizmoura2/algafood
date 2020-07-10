package com.algaworks.algafood.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.ObjectNaoEncontradoException;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.repository.ProdutoRepository;

@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository produtoRepository;

	public List<Produto> find_All() {
		return produtoRepository.findAll();
	}

	public Produto findBy_Id(Long produtoId) {
		return produtoRepository.findById(produtoId)
				.orElseThrow(() -> new ObjectNaoEncontradoException(produtoId, "Produto"));
	}

	@Transactional
	public Produto s_save(Produto produto) {			
		return produtoRepository.save(produto);
	}
	
	@Transactional
	public void s_delete(Long produtoId, Long restauranteId) {
		Produto produto = findBy_Id(produtoId);
		Long id = produto.getRestaurante().getId();
		if (id != restauranteId) {
			throw  new ObjectNaoEncontradoException(produtoId, "Produto no Restaurante "+restauranteId);
		}
		try {
			produtoRepository.delete(produto);
			produtoRepository.flush();
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(produtoId, "Usuario ");
		}
	}

	public List<Produto> findByAtivos(List<Produto> todosProdutos) {
		List<Produto> lstProduto = new ArrayList<>();
		todosProdutos.forEach(produto->{			
			if (produto.isAtivo()) {
				lstProduto.add(produto);
			}			
		});
		return lstProduto;
	}



}
