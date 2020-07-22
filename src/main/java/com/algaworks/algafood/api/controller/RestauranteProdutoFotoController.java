package com.algaworks.algafood.api.controller;
import java.io.IOException;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.algaworks.algafood.api.assembler.OutputAssembler;
import com.algaworks.algafood.api.model.input.FotoProdutoInput;
import com.algaworks.algafood.api.model.output.FotoProdutoOutput;
import com.algaworks.algafood.domain.exception.FotoProdutoNaoEncontradaException;
import com.algaworks.algafood.domain.exception.ObjectNaoEncontradoException;
import com.algaworks.algafood.domain.model.FotoProduto;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.service.CatalogoFotoProdutoService;

import com.algaworks.algafood.domain.service.ProdutoService;

@RestController
@RequestMapping("/restaurantes/{restauranteId}/produtos/{produtoId}/foto")
public class RestauranteProdutoFotoController {

	@Autowired
	private ProdutoService cadastroProduto;
	
	@Autowired
	private CatalogoFotoProdutoService catalogoFotoProduto;
	
	@Autowired
	private OutputAssembler<FotoProduto, FotoProdutoOutput> outputAssembler;
	
	@PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public FotoProdutoOutput atualizarFoto(@PathVariable Long restauranteId,
			@PathVariable Long produtoId,  @Valid FotoProdutoInput fotoProdutoInput) throws IOException {
		
		Produto produto = cadastroProduto.findBy_Id(produtoId);
		
		if (produto.getRestaurante().getId() == restauranteId) {
			MultipartFile arquivo = fotoProdutoInput.getArquivo();
			
			FotoProduto foto = new FotoProduto();
			foto.setProduto(produto);
			foto.setDescricao(fotoProdutoInput.getDescricao());
			foto.setContentType(arquivo.getContentType());
			foto.setTamanho(arquivo.getSize());
			foto.setNomeArquivo(arquivo.getOriginalFilename());
			
			FotoProduto fotoSalva = catalogoFotoProduto.salvar(foto, arquivo.getInputStream());
			return outputAssembler.toModel(fotoSalva, FotoProdutoOutput.class);
		}else {
			throw new ObjectNaoEncontradoException(restauranteId, "Restaurante");
		}
	}
	
	@GetMapping
	public FotoProdutoOutput buscar(@PathVariable Long restauranteId, 
	        						@PathVariable("produtoId") Produto produto, Long produtoId) {
		System.out.println(produto);
		System.out.println(produto.getRestaurante());
		
		Optional.ofNullable(produto).orElseThrow(
				() -> new ObjectNaoEncontradoException(produtoId, "Produto"));
		
		if (produto.getRestaurante().getId() != restauranteId) {
			throw new ObjectNaoEncontradoException(restauranteId, "Restaurante");
		};
		
	    FotoProduto fotoProduto = catalogoFotoProduto.findBy_id(produtoId);
	    Optional.ofNullable(fotoProduto).orElseThrow(() -> new FotoProdutoNaoEncontradaException(restauranteId, produtoId));
	    
	    return outputAssembler.toModel(fotoProduto, FotoProdutoOutput.class);
	}
	
}