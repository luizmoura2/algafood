package com.algaworks.algafood.core.storage;

/**
 * Configurando properties através de classe
 * no arquivo:
 * application.properties sao configurados:
 * algafood.storage.	-> <@ConfigurationProperties("algafood.storage")>
 * 					local. 	-> <private Local local;>
 * 						  diretorio-fotos=/Users/luizmoura/Documents/algafood/uploads -> <private Path diretorioFotos;>
 * 
 *  algafood.storage	-> <@ConfigurationProperties("algafood.storage")>
 *  				.s3 	-> < private Local local; > 
 *  				   .bucket ->< private String bucket >;
 */

import java.nio.file.Path;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.amazonaws.regions.Regions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties("algafood.storage")
public class StorageProperties {

	private Local local = new Local();
	private S3 s3 = new S3();
	
	@Getter
	@Setter
	public class Local {
		
		private Path diretorioFotos;
		
	}
	
	@Getter
	@Setter
	public class S3 {
		
		private String idChaveAcesso;
		private String chaveAcessoSecreta;
		private String bucket;
		private Regions regiao;
		private String diretorioFotos;
		
	}
	
}