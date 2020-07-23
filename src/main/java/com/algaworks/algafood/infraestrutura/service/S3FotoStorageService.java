package com.algaworks.algafood.infraestrutura.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.algaworks.algafood.core.storage.StorageProperties;
import com.algaworks.algafood.domain.service.FotoStorageService;
import com.amazonaws.services.s3.AmazonS3;

//@Service
public class S3FotoStorageService implements FotoStorageService {

	/**
	 * Injeta a classe de propiedade
	 */
	@Autowired
	private AmazonS3 amazonS3;
	
	@Override
	public void armazenar(NovaFoto novaFoto) {
		//
	}
	
	@Override
	public void remover(String nomeArquivo) {
		//
	}
	
	@Override
	public InputStream recuperar(String nomeArquivo){
		return null;
		//
	}

	private Path getArquivoPath(String nomeArquivo) {
		return null;
		//
	}

}