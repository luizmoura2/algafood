package com.algaworks.algafood;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;

import java.math.BigDecimal;

import javax.validation.ConstraintViolationException;

//import org.flywaydb.core.Flyway;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.service.RestauranteService;
import com.algaworks.algafood.util.DatabaseCleaner;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
public class TestesRestauranteIT {

	//private static final int COZINHA_ID_INEXISTENTE = 100;
	private Cozinha cozinhaAmericana;
	private Restaurante restauranteArtesanal;
	private int quantidadeRestaurantesCadastradas;
	//private String jsonCorretoRestaurante;
	
	@LocalServerPort
	private int port;
	
	//@Autowired
	//private Flyway flyway;
	
	@Autowired
	private DatabaseCleaner databaseCleaner;
	
	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@Autowired
	private CozinhaRepository cozinhaRepository;
	@Autowired
	private RestauranteService restauranteService;
	
	@Before
	public void setUp() {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.port = port;
		RestAssured.basePath = "/restaurantes";
		//flyway.migrate();
		//jsonCorretoRestaurante = ResourceUtils.getContentFromResource("/json/correto/restaurante.json");
		databaseCleaner.clearTables();
		prepararDados();
	}
	
	
	@Test(expected =  ConstraintViolationException.class)
	public void failCadastroRestauranteSemCozinha() {
		Restaurante novoRestaurante = new Restaurante();
		novoRestaurante.setNome(null);
		novoRestaurante.setCozinha(cozinhaAmericana);
		novoRestaurante.setTaxaFrete(BigDecimal.valueOf(15.00));
		
		novoRestaurante = restauranteService.s_save(novoRestaurante);
	}

	@Test
	public void deveRetornarStatus200_QuandoConsultar() {
		
		given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.statusCode(HttpStatus.OK.value());
	}
	
	@Test
	public void deveConter2Restaurantes_QuandoConsultar() {
		
		given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.body("", hasSize(quantidadeRestaurantesCadastradas))
			.body("nome", hasItems("Boi na Braza", "Artesanal"));
	}

	/*
	 * @Test public void testRetornarStatus201_QuandoCadastrarCozinha() { given()
	 * .body(jsonCorretoRestaurante) .contentType(ContentType.JSON)
	 * .accept(ContentType.JSON) .when() .post() .then()
	 * .statusCode(HttpStatus.CREATED.value()); }
	 * 
	 * @Test public void
	 * deveRetornarRespostaEStatusCorretos_QuandoConsultarCozinhaExistente() {
	 * given() .pathParam("cozinhaId", 2) .accept(ContentType.JSON) .when()
	 * .get("/{cozinhaId}") .then() .statusCode(HttpStatus.OK.value())
	 * .body("titulo", equalTo(cozinhaAmericana.getNome())); }
	 * 
	 * @Test public void deveRetornarStatus404_QuandoConsultarCozinhaInexistente() {
	 * given() .pathParam("cozinhaId", COZINHA_ID_INEXISTENTE)
	 * .accept(ContentType.JSON) .when() .get("/{cozinhaId}") .then()
	 * .statusCode(HttpStatus.NOT_FOUND.value()); }
	 */
	
	private void prepararDados() {
		
		Cozinha cozinha1 = new Cozinha();
		cozinha1.setNome("Tailandesa");
		cozinhaRepository.save(cozinha1);

		cozinhaAmericana = new Cozinha();
		cozinhaAmericana.setNome("Americana");
		cozinhaRepository.save(cozinhaAmericana);
		
		Restaurante restaurante = new Restaurante();
		restaurante.setNome("Boi na Braza");
		restaurante.setTaxaFrete(BigDecimal.valueOf(10.00));
		restaurante.setCozinha(cozinha1);		
		restauranteRepository.save(restaurante);

		restauranteArtesanal = new Restaurante();
		restauranteArtesanal.setNome("Artesanal");
		restauranteArtesanal.setTaxaFrete(BigDecimal.valueOf(20.00));
		restauranteArtesanal.setCozinha(cozinhaAmericana);
		restauranteRepository.save(restauranteArtesanal);
		
		
		quantidadeRestaurantesCadastradas = (int) restauranteRepository.count();
	}
	
}