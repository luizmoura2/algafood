package com.algaworks.algafood.core.modelmapper;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.algaworks.algafood.api.model.output.EnderecoOutput;
import com.algaworks.algafood.domain.model.Endereco;

@Configuration
public class ModelMapperConfig<T> {

	@Bean
	public ModelMapper modelMapper() {
		var modelMapper = new ModelMapper();
		/**
		 * Aula 11.16
		 * modelMapper.createTypeMap(Restaurante.class, RestauranteModel.class)
		 * .addMapping(Restaurante::getTaxaFrete, RestauranteModel::setPrecoFrete);
		 **/
		/**
		 * Aula 12.6. Adicionando endereço no modelo da representação do recurso de restaurante
		 */
		var enderecoToEnderecoModelTypeMap = modelMapper.createTypeMap(Endereco.class, EnderecoOutput.class);
		  enderecoToEnderecoModelTypeMap.<String>addMapping( enderecoSrc ->
		  			enderecoSrc.getCidade().getEstado().getNome(), (enderecoModelDest, value) ->
		  			enderecoModelDest.getCidade().setNomeEstado(value));
		 
	
	
		return modelMapper;
	}
	
}