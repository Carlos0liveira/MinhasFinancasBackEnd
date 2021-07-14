package com.carloso.minhasFinancas.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carloso.minhasFinancas.exception.RegraNegocioExeption;
import com.carloso.minhasFinancas.model.entity.Usuario;
import com.carloso.minhasFinancas.model.repository.UsuarioRepository;
import com.carloso.minhasFinancas.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	private UsuarioRepository repository;
	
	@Autowired
	public UsuarioServiceImpl(UsuarioRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Usuario autenticar(String email, String senha) {
		
		return null;
	}

	@Override
	public Usuario salvarUsuario(Usuario usuario) {
		
		return null;
	}

	@Override
	public void validarEmail(String email) {
		
		boolean existe = repository.existsByEmail(email);
		
		if (existe) {
			throw new RegraNegocioExeption("Já existe um Usuario cadastrado com este Email");
		}
	}

}
