-- ### CADASTRO DE PAISES
insert into pais (id, nome) values (1, 'Brasil');
-- ### CADASTRO DE TIMES
insert into time (id, nome, pais_id) values (1, 'América MG', 1);
insert into time (id, nome, pais_id) values (2, 'Atlético MG', 1);
insert into time (id, nome, pais_id) values (3, 'Atlético PR', 1);
insert into time (id, nome, pais_id) values (4, 'Bahia', 1);
insert into time (id, nome, pais_id) values (5, 'Botafogo', 1);
insert into time (id, nome, pais_id) values (6, 'Ceará', 1);
insert into time (id, nome, pais_id) values (7, 'Chapecoense', 1);
insert into time (id, nome, pais_id) values (8, 'Corinthians', 1);
insert into time (id, nome, pais_id) values (9, 'Cruzeiro', 1);
insert into time (id, nome, pais_id) values (10, 'Flamengo', 1);
insert into time (id, nome, pais_id) values (11, 'Fluminense', 1);
insert into time (id, nome, pais_id) values (12, 'Grêmio', 1);
insert into time (id, nome, pais_id) values (13, 'Internacional', 1);
insert into time (id, nome, pais_id) values (14, 'Palmeiras', 1);
insert into time (id, nome, pais_id) values (15, 'Paraná', 1);
insert into time (id, nome, pais_id) values (16, 'Santos', 1);
insert into time (id, nome, pais_id) values (17, 'São Paulo', 1);
insert into time (id, nome, pais_id) values (18, 'Sport', 1);
insert into time (id, nome, pais_id) values (19, 'Vasco', 1);
insert into time (id, nome, pais_id) values (20, 'Vitória', 1);


insert into jogador (id, nome, email) values (1, 'Zico', 'zico@melhorquepele.com.br');
insert into regra (id, descricao) values(1, 'Roletou? É penalti');
insert into campeonato(id, nome, data_realizacao, status) values (1, 'Estadual 2018', '2018-01-01', 'EM_CRIACAO');

