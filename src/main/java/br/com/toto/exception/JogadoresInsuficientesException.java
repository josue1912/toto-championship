package br.com.toto.exception;

public class JogadoresInsuficientesException extends Exception {

	private static final long serialVersionUID = 1628239894853814089L;
	private String mensagem;
	
	public JogadoresInsuficientesException(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getMensagem() {
		return mensagem;
	}
	
}
