select * from usuario u


select * from lancamento l 



select * from usuario u 

INSERT INTO financas.lancamento
(id, descricao, mes, ano, valor, tipo, status, id_usuario, data_lancamento)
VALUES(1, 'Aluguel', 7, 2021, 650.00, 'DESPESA', 'PENDENTE', 1, NULL);
INSERT INTO financas.lancamento
(id, descricao, mes, ano, valor, tipo, status, id_usuario, data_lancamento)
VALUES(2, 'Condominio', 7, 2021, 500.00, 'DESPESA', 'PENDENTE', 1, NULL);
INSERT INTO financas.lancamento
(id, descricao, mes, ano, valor, tipo, status, id_usuario, data_lancamento)
VALUES(3, 'Salário', 7, 2021, 1700.00, 'RECEITA', 'EFETIVADO', 1, NULL);
