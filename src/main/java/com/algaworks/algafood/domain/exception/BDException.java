package com.algaworks.algafood.domain.exception;

public class BDException extends NegocioException {

	private static final long serialVersionUID = 1L;

	public BDException(String mensagem) {
		super(mensagem);
	}
	public BDException(Long id, String obj) {
		 this(String.format("%s de código %d não pode ser removida, pois está em uso", obj,  id));
	}
	
}