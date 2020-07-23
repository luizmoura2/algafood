package com.algaworks.algafood.domain.service;

import java.io.InputStream;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

public interface FotoStorageService {

	InputStream recuperar(String nomeArquivo);
	void armazenar(NovaFoto novaFoto);
	void remover(String nomeArquivo);
	
	default String gerarNomeArquivo(String nomeOriginal) {
		return UUID.randomUUID().toString() + "_" + nomeOriginal;
	}
	
	default void substituir(String nomeArquivoAntigo, NovaFoto novaFoto) {
		
		this.armazenar(novaFoto);
		
		if (nomeArquivoAntigo != null) {
			this.remover(nomeArquivoAntigo);
		}
		
	}
	
	@Builder
	@Getter
	class NovaFoto {
		
		private String nomeArquivo;
		private String contentType;
		private InputStream inputStream;
		
	}
	
}