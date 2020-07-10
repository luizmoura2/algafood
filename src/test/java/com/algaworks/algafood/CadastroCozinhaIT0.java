package com.algaworks.algafood;

import static org.assertj.core.api.Assertions.assertThat;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.service.CozinhaService;


//@RunWith(SpringRunner.class)
//@SpringBootTest
public class CadastroCozinhaIT0 {

	@Autowired
	private CozinhaService cozinhaService;
	
	@Test
	public void successCadastroCozinha() {
		// cenário
		Cozinha novaCozinha = new Cozinha();
		novaCozinha.setNome("Baiana");
		
		// ação
		novaCozinha = cozinhaService.s_save(novaCozinha);
		
		// validação
		assertThat(novaCozinha).isNotNull();
		assertThat(novaCozinha.getId()).isNotNull();
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void failCadastroCozinha_SemNome() {
		Cozinha novaCozinha = new Cozinha();
		novaCozinha.setNome(null);
		
		novaCozinha = cozinhaService.s_save(novaCozinha);
	}

	@Test(expected = EntidadeEmUsoException.class)
	public void failExcluirCozinha_EmUso() {
		Long id = 1L;		
		cozinhaService.s_delete(id);
	}
	
	@Test(expected = CozinhaNaoEncontradaException.class)
	public void failExcluirCozinha_Inexistente() {
		Long id = 10L;		
		cozinhaService.s_delete(id);
	}

}