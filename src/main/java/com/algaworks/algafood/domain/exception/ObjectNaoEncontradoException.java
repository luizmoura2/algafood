package com.algaworks.algafood.domain.exception;

public class ObjectNaoEncontradoException extends EntidadeNaoEncontradaException {

	private static final long serialVersionUID = 1L;

	public ObjectNaoEncontradoException(String mensagem) {
		super(mensagem);
	}
	
	public ObjectNaoEncontradoException(Long id, String obj) {
		 this(String.format("Não existe um cadastro de %s com código %d", obj,id));
	}
	
	public ObjectNaoEncontradoException(String e, String obj) {
		 this(String.format("Não existe um cadastro de %s com código %s", obj, e));
	}
	
}