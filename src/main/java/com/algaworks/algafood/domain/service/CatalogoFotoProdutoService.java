package com.algaworks.algafood.domain.service;

import java.io.InputStream;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.ObjectNaoEncontradoException;
import com.algaworks.algafood.domain.model.FotoProduto;
import com.algaworks.algafood.domain.repository.ProdutoRepository;
import com.algaworks.algafood.domain.service.FotoStorageService.NovaFoto;

@Service
public class CatalogoFotoProdutoService {

	@Autowired
	private FotoStorageService fotoStorage;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Transactional
	public FotoProduto salvar(FotoProduto foto, InputStream dadosArquivo) {
		String nomeArquivoExistente = null;
		FotoProduto fotoExist = produtoRepository.findFoto(foto.getProduto().getId());
		
		String nomeNovoArquivo = fotoStorage.gerarNomeArquivo(foto.getNomeArquivo());
		
		Optional<FotoProduto> photo = Optional.ofNullable(fotoExist);
		System.out.println();
		if ( photo.isPresent() ) {
			nomeArquivoExistente = fotoExist.getNomeArquivo();
			produtoRepository.delete(fotoExist);
		}
		foto.setNomeArquivo(nomeNovoArquivo);
		foto =  produtoRepository.save(foto);
		produtoRepository.flush();
		
		NovaFoto novaFoto = NovaFoto.builder()
				.nomeAquivo(foto.getNomeArquivo())
				.inputStream(dadosArquivo)
				.build();
				
		//fotoStorage.armazenar(novaFoto);
		fotoStorage.substituir(nomeArquivoExistente, novaFoto);
		
		return foto;
	}
	
	public FotoProduto findBy_id(Long produtoId) {
		
		FotoProduto foto = produtoRepository.findFoto(produtoId);
		
		return foto;
	            
		
	}
	
}