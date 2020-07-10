package com.algaworks.algafood.api.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.output.RestauranteOutput;
import com.algaworks.algafood.domain.model.Restaurante;

@Component
public class RestauranteModelAssembler {

	@Autowired
	private ModelMapper modelMapper;
	
	
	public RestauranteOutput toModel(Restaurante restaurante) {
		return modelMapper.map(restaurante, RestauranteOutput.class);
		/*
		 * CozinhaModel cozinhaModel = new CozinhaModel();
		 * cozinhaModel.setId(restaurante.getCozinha().getId());
		 * cozinhaModel.setNome(restaurante.getCozinha().getNome());
		 * 
		 * RestauranteModel restauranteModel = new RestauranteModel();
		 * restauranteModel.setId(restaurante.getId());
		 * restauranteModel.setNome(restaurante.getNome());
		 * restauranteModel.setTaxaFrete(restaurante.getTaxaFrete());
		 * restauranteModel.setCozinha(cozinhaModel); return restauranteModel;
		 */
	}
	
	public List<RestauranteOutput> toCollectionModel(List<Restaurante> restaurantes) {
		return restaurantes.stream()
				.map(restaurante -> toModel(restaurante))
				.collect(Collectors.toList());
	}
	
}