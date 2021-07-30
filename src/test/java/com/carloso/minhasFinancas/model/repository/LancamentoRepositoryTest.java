package com.carloso.minhasFinancas.model.repository;

import java.math.BigDecimal;
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

import com.carloso.minhasFinancas.model.entity.Lancamento;
import com.carloso.minhasFinancas.model.unum.StatusLancamento;
import com.carloso.minhasFinancas.model.unum.TipoLancamento;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest 
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class LancamentoRepositoryTest {
	
	@Autowired
	LancamentoRepository repository;
	
	@Autowired
	TestEntityManager entityManager;
	
	public static Lancamento criarLancamento() {
		return Lancamento.builder()
				.descricao("Lancamento Teste")
				.ano(2021)
				.mes(07)
				.valor(BigDecimal.valueOf(10))
				.status(StatusLancamento.PENDENTE)
				.tipo(TipoLancamento.RECEITA)
				.build();
	}
	
	@Test
	public void salvarLancamento() {
		Lancamento lancamento = criarLancamento();		
		lancamento = repository.save(lancamento);		
		Assertions.assertThat(lancamento.getId()).isNotNull();
		
	}
	
	@Test
	public void deletarLancamento() {
		Lancamento lancamento = criarLancamento();
		entityManager.persist(lancamento);
		
		lancamento = entityManager.find(Lancamento.class, lancamento.getId());
		repository.delete(lancamento);
		
		Lancamento lancamentoIne = entityManager.find(Lancamento.class, lancamento.getId());
		Assertions.assertThat(lancamentoIne).isNull();
		
	}
	
	@Test
	public void alterarLancamento() {
		Lancamento lancamento = criarLancamento();
		entityManager.persist(lancamento);
		
		lancamento.setAno(2018);
		lancamento.setDescricao("Testando");
		lancamento.setStatus(StatusLancamento.EFETIVADO);
		repository.save(lancamento);
		
		Lancamento lancamentoAtt = entityManager.find(Lancamento.class, lancamento.getId());
		
		Assertions.assertThat(lancamentoAtt.getAno()).isEqualTo(2018);
		Assertions.assertThat(lancamentoAtt.getDescricao()).isEqualTo("Testando");
		Assertions.assertThat(lancamentoAtt.getStatus()).isEqualTo(StatusLancamento.EFETIVADO);
	}
	
	@Test
	public void buscarLancamentoId() {
		Lancamento lancamento = criarLancamento();
		entityManager.persist(lancamento);
		
		Optional<Lancamento> lancamentoFind =   repository.findById(lancamento.getId());
		
		Assertions.assertThat(lancamentoFind.isPresent()).isTrue();
	}

}
