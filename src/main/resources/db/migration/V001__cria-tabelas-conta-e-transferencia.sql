CREATE TABLE conta
(
    id_conta BIGINT AUTO_INCREMENT  NOT NULL PRIMARY KEY,
    nome_responsavel VARCHAR(50) NOT NULL
) engine=InnoDB default charset=utf8;


CREATE TABLE transferencia
(
    id BIGINT AUTO_INCREMENT  NOT NULL PRIMARY KEY,
    data_transferencia TIMESTAMP NOT NULL,
    valor NUMERIC (20,2) NOT NULL,
    tipo VARCHAR(15) NOT NULL,
    nome_operador_transacao VARCHAR (50),
    conta_id BIGINT NOT NULL,

        CONSTRAINT FK_CONTA
        FOREIGN KEY (conta_id)
        REFERENCES conta(id_conta)
) engine=InnoDB default charset=utf8;