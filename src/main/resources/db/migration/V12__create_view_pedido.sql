CREATE 
    ALGORITHM = UNDEFINED 
    DEFINER = `root`@`localhost` 
    SQL SECURITY DEFINER
VIEW `pedido_view` AS
    SELECT 
        `p`.`id` AS `pedidoid`,
        `r`.`nome` AS `restaurante`,
        `r`.`taxa_frete` AS `taxa_frete`,
        `p`.`subtotal` AS `subtotal`,
        `p`.`valor_total` AS `total`,
        `f`.`descricao` AS `forma_agamento`,
        `u`.`nome` AS `cliente`,
        `u`.`email` AS `email`,
        `p`.`endereco_cep` AS `cep`,
        `p`.`endereco_logradouro` AS `rua`,
        `p`.`endereco_numero` AS `nr`,
        `p`.`endereco_complemento` AS `complemento`,
        `p`.`endereco_bairro` AS `bairro`,
        `p`.`status` AS `status`,
        `p`.`data_criacao` AS `data_pedido`,
        `p`.`data_confirmacao` AS `data_confirmacao`,
        `p`.`data_cancelamento` AS `cancelamento`,
        `p`.`data_entrega` AS `entrega`,
        `i`.`id` AS `id`,
        `i`.`quantidade` AS `quantidade`,
        `i`.`preco_unitario` AS `preco_unitario`,
        `i`.`preco_total` AS `preco_total`,
        `i`.`observacao` AS `observacao`,
        `i`.`pedido_id` AS `pedido_id`,
        `i`.`produto_id` AS `produto_id`
    FROM
        ((((`pedido` `p`
        LEFT JOIN `item_pedido` `i` ON ((`i`.`pedido_id` = `p`.`id`)))
        JOIN `restaurante` `r` ON ((`p`.`restaurante_id` = `r`.`id`)))
        JOIN `usuario` `u` ON ((`p`.`usuario_cliente_id` = `u`.`id`)))
        JOIN `forma_pagamento` `f` ON ((`p`.`forma_pagamento_id` = `f`.`id`)))