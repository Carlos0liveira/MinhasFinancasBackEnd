package com.carloso.minhasFinancas.model.repository;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.carloso.minhasFinancas.model.entity.Usuario;


@SpringBootTest
@ExtendWith(SpringExtension.class)
public class UsuarioRepositoryTest {

	@Autowired
	UsuarioRepository repository;
	
	@Test
	public void verificaExistenciaEmail() {
		 
		Usuario usuario = Usuario.builder().nome("usuario").email("testeusuario@gmail.com").build();
		repository.save(usuario);
		
		
		boolean result = repository.existsByEmail("testeusuario@gmail.com");
		
		Assertions.assertThat(result).isTrue();
		
	}
	
}
