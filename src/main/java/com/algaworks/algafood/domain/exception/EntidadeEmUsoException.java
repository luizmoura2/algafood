package com.algaworks.algafood.domain.exception;

public class EntidadeEmUsoException extends NegocioException {

	private static final long serialVersionUID = 1L;

	public EntidadeEmUsoException(String mensagem) {
		super(mensagem);
	}
	public EntidadeEmUsoException(Long id, String obj) {
		 this(String.format("%s de código %d não pode ser removida, pois está em uso", obj,  id));
	}
	
}