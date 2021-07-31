package com.carloso.minhasFinancas.model.service;

import java.util.Arrays;
import java.util.List;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.carloso.minhasFinancas.exception.RegraNegocioExeption;
import com.carloso.minhasFinancas.model.entity.Lancamento;
import com.carloso.minhasFinancas.model.repository.LancamentoRepository;
import com.carloso.minhasFinancas.model.repository.LancamentoRepositoryTest;
import com.carloso.minhasFinancas.service.impl.LancamentoServiceImpl;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LancamentoServiceTest {
	
	@SpyBean
	LancamentoServiceImpl service; 
	
	@MockBean
	LancamentoRepository repository;
	
	@Test
	public void salvarLancamento() {
		Lancamento lancamentoSalvar = LancamentoRepositoryTest.criarLancamento(); 
		Mockito.doNothing().when(service).validar(lancamentoSalvar);
		
		Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento(); 
		lancamentoSalvo.setId(1l);
		Mockito.when(repository.save(lancamentoSalvar)).thenReturn(lancamentoSalvo);
		
		Lancamento lancamento = service.salvar(lancamentoSalvar);
		
		
		Assertions.assertThat(lancamento.getId()).isEqualTo(lancamentoSalvo.getId());
		
	}
	
	
	@Test
	public void naoDeveSalvarLancamento() {
		Lancamento lancamentoSalvar = LancamentoRepositoryTest.criarLancamento();
		Mockito.doThrow(RegraNegocioExeption.class).when(service).validar(lancamentoSalvar);
		
		
		Assertions.catchThrowableOfType(() -> {service.salvar(lancamentoSalvar);}, RegraNegocioExeption.class);
		Mockito.verify(repository, Mockito.never()).save(lancamentoSalvar);
		 
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void filtraLancamentos() {
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1l);
		
		List<Lancamento> lista = Arrays.asList(lancamento);
		Mockito.when(repository.findAll(Mockito.any(org.springframework.data.domain.Example.class))).thenReturn(lista);
	
		List<Lancamento> resultado = service.buscar(lancamento);
		
		Assertions.assertThat(resultado).isNotEmpty().hasSize(1).contains(lancamento);
	}
	
}
