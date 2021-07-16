package com.carloso.minhasFinancas.model.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.carloso.minhasFinancas.exception.ErroAutenticacao;
import com.carloso.minhasFinancas.exception.RegraNegocioExeption;
import com.carloso.minhasFinancas.model.entity.Usuario;
import com.carloso.minhasFinancas.model.repository.UsuarioRepository;
import com.carloso.minhasFinancas.service.impl.UsuarioServiceImpl;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {
	
	@SpyBean
	UsuarioServiceImpl service;
	
	@MockBean
	UsuarioRepository repository;
	

	@Test
	public void deveAutenticarUsuarioComSucesso() {
		String email = "teste@gmail.com";
		String senha = "senha";
		
		Usuario usuario = Usuario.builder().email(email).senha(senha).id(1l).build();
		Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));
		
		Usuario result = service.autenticar(email, senha);
		
		Assertions.assertThat(result).isNotNull();
	}
	
	@Test
	public void deveLancarErroQuandoNaoEncontrarEmail() {
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
		
		Throwable exception = assertThrows(ErroAutenticacao.class, () -> service.autenticar("email.com", "senha"));
		Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Email não encontrado");
		
	}
	
	@Test
	public void deveLancarErroSeSenhaNaoBater(){
		String senha = "teste";
		Usuario usuario = Usuario.builder().email("testeemail@gmail.com").senha(senha).id(1l).build();
		
		Mockito.when(repository.findByEmail("testeemail@gmail.com")).thenReturn(Optional.of(usuario));
		
		Throwable exception = assertThrows(ErroAutenticacao.class, () -> service.autenticar("testeemail@gmail.com", "123") );
		Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Senha Incorreta");
	}	
	
	@Test
	public void deveSalvarUsuario() {
		Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
		Usuario usuario = Usuario.builder()
				.id(1l)
				.nome("Julia Soares")
				.email("julinhafreefire_gameplays@gmail.com")
				.senha("senha").build();
		Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);
		
		Usuario usuarioSalvo = service.salvarUsuario(new Usuario());
		
		Assertions.assertThat(usuarioSalvo).isNotNull();
		Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(usuario.getId());
		Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo(usuario.getNome());
		Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo(usuario.getEmail());
		Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo(usuario.getSenha());
	}
	
	@Test
	public void naoDeveSalvarUsuarioComEmailDuplicado() {
		Usuario usuario = Usuario.builder().email("testedeemail@gmail.com").build();
		Mockito.doThrow(RegraNegocioExeption.class).when(service).validarEmail("testedeemail@gmail.com");
		
		assertThrows(RegraNegocioExeption.class, () -> service.salvarUsuario(usuario));
	}
	
	
	@Test
	public void deveValidarEmail() {
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
	}
	
	@Test
	public void deveLancarErroQuandoTiveEmailCadastrado() {
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
	}
	
}
