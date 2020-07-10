package com.algaworks.algafood.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.algaworks.algafood.api.model.output.PedidoIIOutput;
import com.algaworks.algafood.domain.model.Pedido;

@Repository
public interface PedidoRepository extends CustomJpaRepository<Pedido, Long>,
							JpaSpecificationExecutor<Pedido> {

	List<PedidoIIOutput> findAllP();

	Optional<Pedido> findByCodigo(String codigo);
	
	
	
}