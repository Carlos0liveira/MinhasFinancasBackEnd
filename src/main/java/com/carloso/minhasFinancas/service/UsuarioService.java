package com.carloso.minhasFinancas.service;

import java.util.Optional;

import com.carloso.minhasFinancas.model.entity.Usuario;

public interface UsuarioService {
	
	Usuario autenticar(String email, String senha);
	
	Usuario salvarUsuario(Usuario usuario);
	
	void validarEmail(String email);
	
	Optional<Usuario> buscarPorID(Long id);
	
}
