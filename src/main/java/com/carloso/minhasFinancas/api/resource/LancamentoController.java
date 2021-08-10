package com.carloso.minhasFinancas.api.resource;


import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.carloso.minhasFinancas.api.dto.AtualizaStatusDTO;
import com.carloso.minhasFinancas.api.dto.LancamentoDTO;
import com.carloso.minhasFinancas.exception.RegraNegocioExeption;
import com.carloso.minhasFinancas.model.entity.Lancamento;
import com.carloso.minhasFinancas.model.entity.Usuario;
import com.carloso.minhasFinancas.model.unum.StatusLancamento;
import com.carloso.minhasFinancas.model.unum.TipoLancamento;
import com.carloso.minhasFinancas.service.LancamentoService;
import com.carloso.minhasFinancas.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/lancamentos")
@RequiredArgsConstructor
public class LancamentoController {

	private final LancamentoService service;
	private final UsuarioService usuarioService;
	

	
	private Lancamento converter(LancamentoDTO dto) {
		Lancamento lancamento = new Lancamento();
		lancamento.setId(dto.getId());
		lancamento.setDescricao(dto.getDescricao());
		lancamento.setAno(dto.getAno());
		lancamento.setMes(dto.getMes());
		lancamento.setValor(dto.getValor());
		
		Usuario usuario = usuarioService.buscarPorID(dto.getUsuario()).orElseThrow(() -> new RegraNegocioExeption("Usuario Não encontrado para o id Informado"));
		
		lancamento.setUsuario(usuario);
		
		if (dto.getTipo() != null) {
			lancamento.setTipo(TipoLancamento.valueOf(dto.getTipo()));
		}
		
		
		
		if(dto.getStatus() != null) {
			lancamento.setStatus(StatusLancamento.valueOf(dto.getStatus()));
		}
		
		
		return lancamento;
	}
	
	private LancamentoDTO converter( Lancamento lancamento ) {
		return LancamentoDTO.builder()
				.id(lancamento.getId())
				.descricao(lancamento.getDescricao())
				.valor(lancamento.getValor())
				.mes(lancamento.getMes())
				.ano(lancamento.getAno())
				.status(lancamento.getStatus().name())
				.tipo(lancamento.getTipo().name())
				.usuario(lancamento.getUsuario().getId())
				.build();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping
	public ResponseEntity salvar(@RequestBody LancamentoDTO dto ) {
		try {
			Lancamento entidade  = converter(dto);
			entidade = service.salvar(entidade); 
			return new ResponseEntity(entidade, HttpStatus.CREATED);
			
		} catch (RegraNegocioExeption e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GetMapping("{id}")
	public ResponseEntity lancamentoPorId( @PathVariable("id") long id ) {
		return service.obterPorId(id)
				.map( lancamento -> new ResponseEntity(converter(lancamento), HttpStatus.OK) )
				.orElseGet( () -> new ResponseEntity(HttpStatus.NOT_FOUND) );
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PutMapping("{id}")
	public ResponseEntity atualizar( @PathVariable("id") Long id, @RequestBody LancamentoDTO dto) {
		return service.obterPorId(id).map(entity -> {
			try {
				Lancamento lancamento = converter(dto);
				lancamento.setId(entity.getId());
				service.atualizar(lancamento);
				return ResponseEntity.ok(lancamento);
			}catch( RegraNegocioExeption e  ) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
			
		}).orElseGet( () ->
			new ResponseEntity("Lancamento nao encontrado na base de dados", HttpStatus.BAD_REQUEST));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@DeleteMapping("{id}")
	public ResponseEntity deletar( @PathVariable("id") Long id) {
		return service.obterPorId(id).map(entidade -> {
			service.deletar(entidade);
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}).orElseGet( () ->
		new ResponseEntity("Lancamento nao encontrado na base de dados", HttpStatus.BAD_REQUEST));
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PutMapping("{id}/atualiza-status")
	public ResponseEntity atualizarStatus( @PathVariable("id") Long id, @RequestBody AtualizaStatusDTO dto ) {
		return service.obterPorId(id).map(entity -> {
			StatusLancamento statusInst = StatusLancamento.valueOf(dto.getStatus());
			if (statusInst ==  null) {
				return ResponseEntity.badRequest().body("Não foi possivel encontrar esse tipo de Status para o lancamento");
			}
			 try {
				entity.setStatus(statusInst);
				service.atualizar(entity);
				return ResponseEntity.ok(entity); 
			 }catch( RegraNegocioExeption e  ) {
					return ResponseEntity.badRequest().body(e.getMessage());
				}	
		}).orElseGet( () ->
		new ResponseEntity("Lancamento nao encontrado na base de dados", HttpStatus.BAD_REQUEST));
	}
	
	@SuppressWarnings("rawtypes")
	@GetMapping
	public ResponseEntity buscarLancamento(
			@RequestParam(value = "descricao", required = false) String descricao,
			@RequestParam(value = "mes", required = false) Integer mes,
			@RequestParam(value = "ano", required = false) Integer ano,
			@RequestParam(value = "tipo", required = false) TipoLancamento tipo,
			@RequestParam("usuario") Long  idUsuario
			) {
		Lancamento lancamentoFiltro = new Lancamento();
		lancamentoFiltro.setDescricao(descricao);
		lancamentoFiltro.setMes(mes);
		lancamentoFiltro.setAno(ano);
		lancamentoFiltro.setTipo(tipo);
		
		Optional<Usuario> usuario = usuarioService.buscarPorID(idUsuario);
		if (!usuario.isPresent()) {
			return ResponseEntity.badRequest().body("Nao foi possivel realizar a consulta");
		}else {
			lancamentoFiltro.setUsuario(usuario.get());
		}
		
		List<Lancamento> lancamentos = service.buscar(lancamentoFiltro);
		
		return ResponseEntity.ok(lancamentos);
	}
}
