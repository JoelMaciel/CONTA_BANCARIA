
set foreign_key_checks = 0;

delete from conta;
delete from transferencia;;

set foreign_key_checks = 1;

alter table conta auto_increment = 1;
alter table transferencia auto_increment = 1;



INSERT INTO conta (id_conta, nome_responsavel) VALUES (1,'Fulano');
INSERT INTO conta (id_conta, nome_responsavel) VALUES (2,'Sicrano');

INSERT INTO transferencia (id,data_transferencia, valor, tipo, nome_operador_transacao, conta_id) VALUES (1,'2019-01-01 12:00:00+00:00',30895.46,'DEPOSITO', 'Beltrano', 1);
INSERT INTO transferencia (id,data_transferencia, valor, tipo, nome_operador_transacao, conta_id) VALUES (2,'2019-02-03 09:53:27+00:00',12.24,'DEPOSITO', 'Ronnyscley',2);
INSERT INTO transferencia (id,data_transferencia, valor, tipo, nome_operador_transacao, conta_id) VALUES (3,'2019-05-04 08:12:45+00:00',-500.50,'SAQUE', 'Ronnyscley',1);
INSERT INTO transferencia (id,data_transferencia, valor, tipo, nome_operador_transacao, conta_id) VALUES (4,'2019-08-07 08:12:45+00:00',-530.50,'SAQUE', 'Sicrano',2);
INSERT INTO transferencia (id,data_transferencia, valor, tipo, nome_operador_transacao, conta_id) VALUES (5,'2019-06-08 10:15:01+00:00', -241.23,'TRANSFERENCIA DE SAÍDA', 'Fulano',1);
INSERT INTO transferencia (id,data_transferencia, valor, tipo, nome_operador_transacao, conta_id) VALUES (6,'2020-04-01 12:12:04+00:00',5173.09,'TRANSFERENCIA DE ENTRADA', 'Ronnyscley',2);
INSERT INTO transferencia (id,data_transferencia, valor, tipo, nome_operador_transacao, conta_id) VALUES (7,'2020-01-01 12:00:00+00:00',895.46,'DEPOSITO', 'Beltrano', 1);
INSERT INTO transferencia (id,data_transferencia, valor, tipo, nome_operador_transacao, conta_id) VALUES (8,'2020-02-03 09:53:27+00:00',12.24,'DEPOSITO', 'Ronnyscley',2);
INSERT INTO transferencia (id,data_transferencia, valor, tipo, nome_operador_transacao, conta_id) VALUES (9,'2020-05-04 08:12:45+00:00',-800.50,'SAQUE', 'Ronnyscley',1);
INSERT INTO transferencia (id,data_transferencia, valor, tipo, nome_operador_transacao, conta_id) VALUES (10,'2021-08-07 08:12:45+00:00',-690.50,'TRANSFERENCIA DE SAÍDA', 'Sicrano',2);
INSERT INTO transferencia (id,data_transferencia, valor, tipo, nome_operador_transacao, conta_id) VALUES (11,'2021-06-08 10:15:01+00:00', -360.23,'SAQUE', 'Fulano',1);
INSERT INTO transferencia (id,data_transferencia, valor, tipo, nome_operador_transacao, conta_id) VALUES (12,'2021-04-01 12:12:04+00:00',173.09,'TRANSFERENCIA DE ENTRADA', 'Ronnyscley',2);