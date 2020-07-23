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

@Service
public class LocalFotoStorageService implements FotoStorageService {

	//@Value("${algafood.storage.local.diretorio-fotos}")
	//private Path diretorioFotos;
	/**
	 * Injeta a classe de propiedade
	 */
	@Autowired
	private StorageProperties storageProperties;
	
	@Override
	public void armazenar(NovaFoto novaFoto) {
		try {
			Path arquivoPath = getArquivoPath(novaFoto.getNomeAquivo());
			
			FileCopyUtils.copy(novaFoto.getInputStream(), 
					Files.newOutputStream(arquivoPath));
		} catch (Exception e) {
			throw new StorageException("Não foi possível armazenar arquivo.", e);
		}
	}
	
	@Override
	public void remover(String nomeArquivo) {
		try {
			Path arquivoPath = getArquivoPath(nomeArquivo);
			
			Files.deleteIfExists(arquivoPath);
		} catch (Exception e) {
			throw new StorageException("Não foi possível excluir arquivo.", e);
		}
	}
	
	@Override
	public InputStream recuperar(String nomeArquivo){
		Path arquivoPath = getArquivoPath(nomeArquivo);
		try {
			InputStream fileStream = Files.newInputStream(arquivoPath);
			return fileStream;
		} catch (IOException e) {
			throw new StorageException("Não foi possível obter o  arquivo.", e);
		}
	}

	private Path getArquivoPath(String nomeArquivo) {
		//return diretorioFotos.resolve(Path.of(nomeArquivo));
		/*Obtém o valor da propriedade: algafood.storage.s3.diretorio-fotos=catalogo
		 * no arquivo application.properties
		 * através da injeção do arquido de propriedade
		 */
		return storageProperties.getLocal().getDiretorioFotos()
				.resolve(Path.of(nomeArquivo));
	}

}