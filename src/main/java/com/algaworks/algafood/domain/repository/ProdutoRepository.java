package com.algaworks.algafood.domain.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.model.FotoProduto;
import com.algaworks.algafood.domain.model.Produto;

@Repository
public interface ProdutoRepository extends CustomJpaRepository<Produto, Long>, ProdutoRepositoryQueries {
	
	@Query("select f from FotoProduto f join f.produto p "
			+ "where f.produto.id = :produtoId")
	FotoProduto findFotoByProdutoId( Long produtoId);
}