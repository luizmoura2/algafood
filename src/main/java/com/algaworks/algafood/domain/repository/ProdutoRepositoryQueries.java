package com.algaworks.algafood.domain.repository;

import com.algaworks.algafood.domain.model.FotoProduto;

public interface ProdutoRepositoryQueries {

	FotoProduto save(FotoProduto foto);
	FotoProduto findFoto(Long id);
	void delete(FotoProduto foto);
	//FotoProduto findBy_ids(Long restauranteId, Log produtoId);
	
}