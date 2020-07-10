package com.algaworks.algafood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.InputAssembler;
import com.algaworks.algafood.api.assembler.OutputAssembler;
import com.algaworks.algafood.api.model.input.RestauranteInput;
import com.algaworks.algafood.api.model.output.RestauranteOutput;
import com.algaworks.algafood.api.model.view.VendaDiaria2;
import com.algaworks.algafood.core.validation.ValidacaoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.exception.RestauranteNaoEncontradoException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.service.RestauranteService;

@RestController
@RequestMapping(value = "/restaurantes")
public class RestauranteController {

	@Autowired
	private RestauranteService restauranteService;
	
	@Autowired
	private OutputAssembler<Restaurante, RestauranteOutput> outputAssembler;
	
	@Autowired
	private InputAssembler<RestauranteInput, Restaurante> inputAssembler;

	@Autowired
	private SmartValidator validator;

//	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/relvenda")
	public List<VendaDiaria2> listaRel(@RequestParam String startDate, String endDate) {
		return restauranteService.findBy_RelDia(startDate, endDate);
	};

	
	//@JsonView(RestauranteView.Resumo.class)
	@GetMapping
	public Page<RestauranteOutput> listar(Pageable pageable) {
		
		Page<Restaurante> restaurantePage = restauranteService.find_AllPg(pageable);
		
		List<RestauranteOutput> restauranteOutput = outputAssembler
						.toCollectionModel(restaurantePage.getContent(), RestauranteOutput.class);
		
		Page<RestauranteOutput> restauranteOutputPage = new PageImpl<>(restauranteOutput, pageable, 
				restaurantePage.getTotalElements());
		
		return  restauranteOutputPage;
	}
	
//	@JsonView(RestauranteView.ApenasNome.class)
//	@GetMapping(params = "projecao=apenas-nome")
//	public List<RestauranteOutput> listarApenasNomes() {
//		return listar();
//	}
	
//	@GetMapping
//	public MappingJacksonValue listar(@RequestParam(required = false) String projecao) {
//		List<Restaurante> restaurantes = restauranteRepository.findAll();
//		List<RestauranteModel> restaurantesModel = restauranteModelAssembler.toCollectionModel(restaurantes);
//		
//		MappingJacksonValue restaurantesWrapper = new MappingJacksonValue(restaurantesModel);
//		
//		restaurantesWrapper.setSerializationView(RestauranteView.Resumo.class);
//		
//		if ("apenas-nome".equals(projecao)) {
//			restaurantesWrapper.setSerializationView(RestauranteView.ApenasNome.class);
//		} else if ("completo".equals(projecao)) {
//			restaurantesWrapper.setSerializationView(null);
//		}
//		
//		return restaurantesWrapper;
//	}


	
	@GetMapping("/{restauranteId}")
	@ResponseStatus(HttpStatus.OK)
	public RestauranteOutput buscar(@PathVariable Long restauranteId) {
		Restaurante restaurante = restauranteService.findBy_Id(restauranteId);

		return outputAssembler.toModel(restaurante, RestauranteOutput.class);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public RestauranteOutput adicionar(@RequestBody @Valid RestauranteInput restauranteInput) {
		Restaurante restaurante = inputAssembler.toDomainObject(restauranteInput, Restaurante.class);
		try {
			return outputAssembler.toModel(restauranteService.s_save(restaurante), RestauranteOutput.class);
		} catch (EntidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
	}

	@PutMapping("/{restauranteId}")
	public RestauranteOutput atualizar(@PathVariable Long restauranteId,
			@RequestBody @Valid RestauranteInput restauranteInput) {
		
		Restaurante restauranteAtual = restauranteService.findBy_Id(restauranteId);
		
		restauranteAtual.setCozinha(new Cozinha());
		if (restauranteAtual.getEndereco() != null) {
			restauranteAtual.getEndereco().setCidade(new Cidade());
		}
		
		inputAssembler.copyToDomainObject(restauranteInput, restauranteAtual);
		try {

			return outputAssembler.toModel(restauranteService.s_save(restauranteAtual), RestauranteOutput.class);
		} catch (EntidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
	}

	@DeleteMapping("/{restauranteId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long restauranteId) {
		restauranteService.s_delete(restauranteId);
	}

	@PatchMapping("/{restauranteId}")
	public RestauranteOutput atualizarParcial(@PathVariable Long restauranteId,
			@RequestBody RestauranteInput restauranteInput) {

		Restaurante restauranteAtual = restauranteService.findBy_Id(restauranteId);
		/**
		 * Aula 11.17
		 */
		restauranteAtual.setCozinha(new Cozinha());
		inputAssembler.copyToDomainObject(restauranteInput, restauranteAtual);

		Restaurante restaurante = inputAssembler.toDomainObject(restauranteInput, Restaurante.class);
		restaurante = restauranteService.s_mergeParc(restaurante, restauranteAtual);
		validate(restaurante, "restaurante");

		return outputAssembler.toModel(restauranteService.s_save(restaurante), RestauranteOutput.class);
	}

	private void validate(Restaurante restaurante, String objectName) {
		BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(restaurante, objectName);
		validator.validate(restaurante, bindingResult);

		if (bindingResult.hasErrors()) {
			throw new ValidacaoException(bindingResult);
		}
	}

	@PutMapping("/{restauranteId}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void ativar(@PathVariable Long restauranteId) {
		restauranteService.ativar(restauranteId);
	}

	@DeleteMapping("/{restauranteId}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void inativar(@PathVariable Long restauranteId) {
		restauranteService.inativar(restauranteId);
	}
	
	
	@PutMapping("/ativacoes")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void ativarMultiplos(@RequestBody List<Long> restauranteIds) {
		try {
			restauranteService.ativar(restauranteIds);
		} catch (RestauranteNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}
	
	@DeleteMapping("/ativacoes")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void inativarMultiplos(@RequestBody List<Long> restauranteIds) {
		try {
			restauranteService.inativar(restauranteIds);
		} catch (RestauranteNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}
	@PutMapping("/{restauranteId}/abrir")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void abrir(@PathVariable Long restauranteId) {
		restauranteService.abrir(restauranteId);
	}

	@PutMapping("/{restauranteId}/fechar")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void fechar(@PathVariable Long restauranteId) {
		restauranteService.fechar(restauranteId);
	}

}
