package com.algaworks.algafood.infraestrutura.service;

import java.net.URL;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;

import com.algaworks.algafood.core.storage.StorageProperties;
import com.algaworks.algafood.domain.service.FotoStorageService;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

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
			
			String caminhaArquivo = getCaminhoArquivo(novaFoto.getNomeArquivo());
			var objMetadata = new ObjectMetadata();
			objMetadata.setContentType(novaFoto.getContentType());
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
		try {
			
			String caminhaArquivo = getCaminhoArquivo(nomeArquivo);
			var putObjectRequest = new DeleteObjectRequest(
					storageProperties.getS3().getBucket(),
					caminhaArquivo);
					
			//Path arquivoPath = getArquivoPath(nomeArquivo);			
			//Files.deleteIfExists(arquivoPath);
			amazonS3.deleteObject(putObjectRequest);
			
		} catch (Exception e) {
			throw new StorageException("Não foi possível excluir arquivo.", e);
		}
	}
	
	@Override
	public FotoRecuperada recuperar(String nomeArquivo) {
		String caminhoArquivo = getCaminhoArquivo(nomeArquivo);
		
		URL url = amazonS3.getUrl(storageProperties.getS3().getBucket(), caminhoArquivo);
		
		return FotoRecuperada.builder()
				.url(url.toString()).build();
	}

	private String getCaminhoArquivo(String nomeArquivo) {
		return String.format("%s/%s", storageProperties.getS3().getDiretorioFotos(), nomeArquivo);
		
	}
	
}