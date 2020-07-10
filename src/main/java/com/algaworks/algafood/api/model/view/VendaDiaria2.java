package com.algaworks.algafood.api.model.view;
import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Usando @NamedNativeQuery em uma VIEW
 * 
 * Esta classe é um entidade representada no banco de dados por uma VIEW 
 * denominada 'relatorio_venda': @Table(name="relatorio_venda").
 * Não é uma tabela, é uma VIEW no mysql.
 * 
 * Obs.: é obrigaório definir um id, ou um campo que seja um identificador
 * único na class, que vem do Banco de Dados.
 * 
 * ------------Aqui a View relatorio_venda------------------
 * 
 * USE `algafood`;
 * CREATE  OR REPLACE VIEW `algafood`.`relatorio_venda` AS
 *  SELECT
 *  	`p`.`id` AS `id`,
 *      date(`p`.`data_criacao`) AS `data_criacao`,
 *      COUNT(*) AS `total_venda`,
 *      SUM(`p`.`valor_total`) AS `total_faturaddo`,
 *      `p`.`restaurante_id` as `restaurante`
 *  FROM  `algafood`.`pedido` `p`
 *  WHERE `p`.`status` IN ('CONFIRMADO' , 'ENTREGUE')
 *  GROUP BY `p`.`data_criacao`, `p`.`restaurante_id`;
 *  
 *  --------------------------------------------------------
 *  
 *  No Repositorio de restaurante chamar: List<VendaDiaria> getRelatorioVenda(String startDate, String endDate)
 *   @NamedNativeQuery(
 *   				name = "Restaurante.getRelatorioVenda",
 *   ...
 *  
 *  No Controller 
 *  @GetMapping("/relvenda")
 *  public List<VendaDiaria2> listaRel(@RequestParam String startDate, String endDate) {
 *  	return restauranteService.findBy_RelDia(startDate, endDate);
 *  };
 *  	
 *   Para executar: localhost:8080/restaurantes/relvenda?startDate=22/06/2020&endDate=22/06/2020
 *   
 * @author luizmoura2@hotmail.com
 *
 */
@Entity
@Table(name="relatorio_venda")
@NamedNativeQuery(
		name = "Restaurante.getRelatorioVenda",
		query = "SELECT * from `relatorio_venda` `r`"
			+ " WHERE  DATE_FORMAT(`r`.`data_criacao`, '%d/%m/%Y') BETWEEN ?1 AND ?2",
			
	resultClass = VendaDiaria2.class
)
@Getter
@Setter
public class VendaDiaria2 {
	
	@Id
	private Long id;
	private LocalDate data_criacao;
	private Long total_venda;
	private BigDecimal total_faturaddo;
	
}