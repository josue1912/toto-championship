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

-- ### CADASTRO DE JOGADORES
insert into jogador (id, nome, email) values (1, 'Zico', 'zico@futebol.com.br');
insert into jogador (id, nome, email) values (2, 'Pele', 'pele@futebol.com.br');
insert into jogador (id, nome, email) values (3, 'Romario', 'romario@futebol.com.br');
insert into jogador (id, nome, email) values (4, 'Ronaldo', 'r9@futebol.com.br');
insert into jogador (id, nome, email) values (5, 'Ronaldinho Gaucho', 'r10@futebol.com.br');
insert into jogador (id, nome, email) values (6, 'Bebeto', 'bebeto@futebol.com.br');

-- ### CADASTRO DE REGRAS
insert into regra (id, descricao) values(1, 'Roletou? É penalti');

-- ### CADASTRO DE CAMPEONATO
insert into campeonato(id, nome, data_realizacao, status) values (1, 'Estadual 2018', '2018-01-01', 'EM_CRIACAO');
insert into campeonato(id, nome, data_realizacao, status) values (2, 'Brasileiro 2018', '2018-01-01', 'EM_ANDAMENTO');

insert into equipe (id, derrotas, empates, golsafavor, gols_contra, nome, pontos, saldo_de_gols, vitorias) values(1,0,0,0,0,'Bahia',0,0,0);
insert into equipe (id, derrotas, empates, golsafavor, gols_contra, nome, pontos, saldo_de_gols, vitorias) values(2,0,0,0,0,'Fluminense',0,0,0);
insert into equipe (id, derrotas, empates, golsafavor, gols_contra, nome, pontos, saldo_de_gols, vitorias) values(3,0,0,0,0,'Palmeiras',0,0,0);

insert into campeonato_equipes (campeonato_id, equipes_id) values(1,1);
insert into campeonato_equipes (campeonato_id, equipes_id) values(1,2);
insert into campeonato_equipes (campeonato_id, equipes_id) values(1,3);

insert into equipe_jogadores (equipe_id, jogadores_id) values(1,1);
insert into equipe_jogadores (equipe_id, jogadores_id) values(1,2);
insert into equipe_jogadores (equipe_id, jogadores_id) values(2,3);
insert into equipe_jogadores (equipe_id, jogadores_id) values(2,4);
insert into equipe_jogadores (equipe_id, jogadores_id) values(3,5);
insert into equipe_jogadores (equipe_id, jogadores_id) values(3,6);

insert into partida (id, placar_timea, placar_timeb, status, timea_id, timeb_id) values (1,5,3,'ENCERRADA', 1,2);
insert into partida (id, placar_timea, placar_timeb, status, timea_id, timeb_id) values (2,2,1,'EM_ANDAMENTO', 1,3);
insert into partida (id, placar_timea, placar_timeb, status, timea_id, timeb_id) values (3,0,0,'NAO_REALIZADA', 2,3);

insert into campeonato_partidas (campeonato_id, partidas_id) values (1,1);
insert into campeonato_partidas (campeonato_id, partidas_id) values (1,2);
insert into campeonato_partidas (campeonato_id, partidas_id) values (1,3);



