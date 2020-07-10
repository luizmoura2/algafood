USE `algafood`;
CREATE  OR REPLACE VIEW `algafood`.`relatorio_venda` AS
    SELECT 
    	`p`.`id` AS `id`,
        date(convert_tz(`p`.`data_criacao`, '+00:00', '-03:00')) AS `data_criacao`,
        COUNT(*) AS `total_venda`,
        SUM(`p`.`valor_total`) AS `total_faturaddo`,
        `p`.`restaurante_id` as `restaurante`
    FROM
        `algafood`.`pedido` `p`   
    WHERE p.status in ('CONFIRMADO', 'ENTREGUE')  
    GROUP BY date(convert_tz(`p`.`data_criacao`, '+00:00', '-03:00')), `p`.`restaurante_id`;
