package com.carloso.minhasFinancas.exception;

public class ErroAutenticacao extends  RuntimeException{
	
	public ErroAutenticacao(String msg) {
		super(msg);
	}
}
