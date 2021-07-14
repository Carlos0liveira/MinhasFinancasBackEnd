package com.carloso.minhasFinancas.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.carloso.minhasFinancas.exception.RegraNegocioExeption;
import com.carloso.minhasFinancas.model.entity.Usuario;
import com.carloso.minhasFinancas.model.repository.UsuarioRepository;
import com.carloso.minhasFinancas.service.UsuarioService;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {
	
	@Autowired
	UsuarioService service;
	
	@Autowired
	UsuarioRepository repository;
	
	@Test
	public void deveValidarEmail() {
		repository.deleteAll();
		
		
		service.validarEmail("teste@gmail.com");
	}
	
	@Test
	public void deveLancarErroQuandoTiveEmailCadastrado() {
		Usuario usuario = Usuario.builder().nome("Carlos").email("carlos@gmail.com").build();
		repository.save(usuario);
		
		Assertions.assertThrows(RegraNegocioExeption.class, () -> service.validarEmail("carlos@gmail.com"));
		
		
	}
	
}
