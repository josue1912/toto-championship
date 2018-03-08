package br.com.toto.exception;

public class NumeroDeJogadoresImparException extends Exception {

	private static final long serialVersionUID = 2314215027831008815L;
	public String mensagem;
	
	public NumeroDeJogadoresImparException(String mensagem) {
		this.mensagem = mensagem;
	}
	
	public String getMensagem() {
		return mensagem;
	}
}
