package com.algaworks.algafood.api.controller;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.algaworks.algafood.api.assembler.OutputAssembler;
import com.algaworks.algafood.api.model.input.FotoProdutoInput;
import com.algaworks.algafood.api.model.output.FotoProdutoOutput;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.FotoProdutoNaoEncontradaException;
import com.algaworks.algafood.domain.exception.ObjectNaoEncontradoException;
import com.algaworks.algafood.domain.model.FotoProduto;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.service.CatalogoFotoProdutoService;
import com.algaworks.algafood.domain.service.FotoStorageService;
import com.algaworks.algafood.domain.service.FotoStorageService.FotoRecuperada;
import com.algaworks.algafood.domain.service.ProdutoService;

@RestController
@RequestMapping("/restaurantes/{restauranteId}/produtos/{produtoId}/foto")
public class RestauranteProdutoFotoController {

	
	//private LocalFotoStorageService fotoStorage;
	//private S3FotoStorageService fotoStorage;
	@Autowired
	private FotoStorageService fotoStorage;
	
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
	        						@PathVariable("produtoId") Produto produto) {
			
		Long produtoId = produto.getId();
		Optional.ofNullable(produto).orElseThrow(
				() -> new ObjectNaoEncontradoException(produtoId, "Produto"));
		
		if (produto.getRestaurante().getId() != restauranteId) {
			throw new ObjectNaoEncontradoException(restauranteId, "Restaurante");
		};
		
	    FotoProduto fotoProduto = catalogoFotoProduto.findBy_id(produtoId);
	    Optional.ofNullable(fotoProduto).orElseThrow(() -> new FotoProdutoNaoEncontradaException(restauranteId, produtoId));
	    
	    return outputAssembler.toModel(fotoProduto, FotoProdutoOutput.class);
	}
	
	@GetMapping(produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<?> servirFoto(@PathVariable Long restauranteId, 
			@PathVariable Long produtoId, @RequestHeader(name = "accept") String acceptHeader) throws HttpMediaTypeNotAcceptableException {
		try {
			FotoProduto fotoProduto = catalogoFotoProduto.findBy_id(produtoId);
			
			MediaType mediaTypeFoto = MediaType.parseMediaType(fotoProduto.getContentType());
			List<MediaType> mediaTypesAceitas = MediaType.parseMediaTypes(acceptHeader);
			
			verificarCompatibilidadeMediaType(mediaTypeFoto, mediaTypesAceitas);
						
			FotoRecuperada fotoRecuperada = fotoStorage.recuperar(fotoProduto.getNomeArquivo());
			
			if (fotoRecuperada.temUrl()) {
				return ResponseEntity
						.status(HttpStatus.FOUND)
						.header(HttpHeaders.LOCATION, fotoRecuperada.getUrl())
						.build();
			} else {
				return ResponseEntity.ok()
						.contentType(mediaTypeFoto)
						.body(new InputStreamResource(fotoRecuperada.getInputStream()));
			}
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	private void verificarCompatibilidadeMediaType(MediaType mediaTypeFoto, 
			List<MediaType> mediaTypesAceitas) throws HttpMediaTypeNotAcceptableException {
		
		boolean compativel = mediaTypesAceitas.stream()
				.anyMatch(mediaTypeAceita -> mediaTypeAceita.isCompatibleWith(mediaTypeFoto));
		
		if (!compativel) {
			throw new HttpMediaTypeNotAcceptableException(mediaTypesAceitas);
		}
	}
	
	@DeleteMapping
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void excluir(@PathVariable Long restauranteId, 
	        			@PathVariable Long produtoId) {
		
		//Optional.ofNullable(foto).orElseThrow(
				//() -> new ObjectNaoEncontradoException(foto.getId(), "FotoProduto"));
		
	    catalogoFotoProduto.excluir(produtoId);//restauranteId, produtoId);
	}  
	
}