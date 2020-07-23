package com.algaworks.algafood.infraestrutura.service;

import java.io.InputStream;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;

import com.algaworks.algafood.core.storage.StorageProperties;
import com.algaworks.algafood.domain.service.FotoStorageService;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

//@Service
public class S3FotoStorageService implements FotoStorageService {

	/**
	 * Injeta a classe de propiedade
	 */
	@Autowired
	private AmazonS3 amazonS3;
	
	@Autowired
	private StorageProperties storageProperties;
	
	@Override
	public void armazenar(NovaFoto novaFoto) {
		
		try {
			
			String caminhaArquivo = getCaminhoArquivo(novaFoto.getNomeAquivo());
			var objMetadata = new ObjectMetadata();
			objMetadata.getContentType(novaFoto.get);
			var putObjectRequest = new PutObjectRequest(
					storageProperties.getS3().getBucket(),
					caminhaArquivo,
					novaFoto.getInputStream(),
					objMetadata).withCannedAcl(CannedAccessControlList.PublicRead);
			
			amazonS3.putObject(putObjectRequest);
		
		}catch(Exception e) {
			throw new StorageException("Não foi possível enviar o arquivo para amazon S3", e );
		}
		
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

	private String getCaminhoArquivo(String nomeArquivo) {
		return String.format("%s/%s", storageProperties.getS3().getDiretorioFotos(), nomeArquivo);
		
	}
	
	private Path getArquivoPath(String nomeArquivo) {
		return null;
		//
	}

}