package com.carloso.minhasFinancas.api.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.carloso.minhasFinancas.api.dto.UsuarioDTO;
import com.carloso.minhasFinancas.exception.ErroAutenticacao;
import com.carloso.minhasFinancas.exception.RegraNegocioExeption;
import com.carloso.minhasFinancas.model.entity.Usuario;
import com.carloso.minhasFinancas.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
	
	private final UsuarioService service;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/salvarusuario")
	public ResponseEntity salvar( @RequestBody UsuarioDTO dto){
		
		Usuario usuario  = Usuario.builder()
				.nome(dto.getNome())
				.email(dto.getEmail())
				.senha(dto.getSenha()).build();
		
		try {
			Usuario usuarioSalvo = service.salvarUsuario(usuario);
			return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
		} catch (RegraNegocioExeption e) {
			
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}	
	
	
	@SuppressWarnings("rawtypes")
	@PostMapping("/autenticar")
	public ResponseEntity autenticar(@RequestBody UsuarioDTO dto) {
		try {
			Usuario usuarioAutenticado = service.autenticar(dto.getEmail(), dto.getSenha());
			return ResponseEntity.ok(usuarioAutenticado);
		} catch (ErroAutenticacao e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
		
	}
}
