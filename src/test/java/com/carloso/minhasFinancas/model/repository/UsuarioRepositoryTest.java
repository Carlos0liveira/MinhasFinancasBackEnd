package com.carloso.minhasFinancas.model.repository;


import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.carloso.minhasFinancas.model.entity.Usuario;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest 
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UsuarioRepositoryTest {

	@Autowired
	UsuarioRepository repository;
	
	@Autowired
	TestEntityManager entityManager;
	
	public static Usuario criarUsuario() {
		return  Usuario.builder().
				 nome("Carlos").email("testeemail@gmail.com").senha("123")
				 .build();
	}
	
	
	@Test
	public void verificaExistenciaEmail() {
		 
		Usuario usuario = criarUsuario();
		entityManager.persist(usuario);
		
		
		boolean result = repository.existsByEmail("testeemail@gmail.com");
		
		Assertions.assertThat(result).isTrue();
		
	}
	
	@Test
	public void retornaFalsoCasoNaoTenhaEmail() {
		
		boolean result  = repository.existsByEmail("testeemail@gmail.com");
		
		Assertions.assertThat(result).isFalse();
	}
	
	@Test
	public void persisteUsuarioNaBaseDeDados() {
	 	Usuario usuario = criarUsuario();
	 	Usuario usuarioSave = repository.save(usuario);
	  
		  
		Assertions.assertThat(usuarioSave.getId()).isNotNull();
	}
	
	
	@Test
	public void deveBuscarUsuarioPorEmail() {
		 Usuario usuario = criarUsuario();
		 entityManager.persist(usuario);
		 
		 Optional<Usuario> result =  repository.findByEmail("testeemail@gmail.com");
		 
		 Assertions.assertThat(result.isPresent());
		  
	}
	
	public void deveRetornarVazioAoBuscarUsuarioPorEmailQuandoNaoExiste() {

		
		Optional<Usuario> result =  repository.findByEmail("testeemail@gmail.com");
		
		Assertions.assertThat(result.isPresent()).isFalse();
		
	}
	
	
}
