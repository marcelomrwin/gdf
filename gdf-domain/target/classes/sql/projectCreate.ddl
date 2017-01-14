create table comum.T_CERTIFICADO (ID_CERTIFICADO int8 not null, DAT_CRIACAO timestamp without time zone default CURRENT_TIMESTAMP, DAT_ULTIMA_ALTERACAO timestamp without time zone default CURRENT_TIMESTAMP, NUM_VERSAO INTEGER DEFAULT 0 not null, BIN_ARQUIVO bytea not null, TXT_SENHA varchar(300) not null, primary key (ID_CERTIFICADO))
create table comum.T_CONTRATO (ID_CONTRATO int8 not null, DAT_CRIACAO timestamp without time zone default CURRENT_TIMESTAMP, DAT_ULTIMA_ALTERACAO timestamp without time zone default CURRENT_TIMESTAMP, NUM_VERSAO INTEGER DEFAULT 0 not null, COD_CNPJ varchar(18), primary key (ID_CONTRATO))
create table comum.T_EMPRESA (TXT_CNPJ varchar(255) not null, DAT_CRIACAO timestamp without time zone default CURRENT_TIMESTAMP, DAT_ULTIMA_ALTERACAO timestamp without time zone default CURRENT_TIMESTAMP, NUM_VERSAO INTEGER DEFAULT 0 not null, TXT_NOME varchar(300) not null, SIG_UF varchar(255) not null, DT_ULTT_NSU timestamp, NUM_ULT_NSU int8, certificado_ID_CERTIFICADO int8, contrato_ID_CONTRATO int8 not null, primary key (TXT_CNPJ))
create index IDX_CNPJ on comum.T_CONTRATO (COD_CNPJ)
create index IDX_NM_EMPRESA on comum.T_EMPRESA (TXT_NOME)
create sequence SEQ_ID_CERTIFICADO start 1 increment 1
create sequence SEQ_ID_CONTRATO start 1 increment 1
alter table comum.T_EMPRESA add constraint FK37b7jnaila1k0megwoarntbjj foreign key (certificado_ID_CERTIFICADO) references comum.T_CERTIFICADO
alter table comum.T_EMPRESA add constraint FKknubp1vuqonef37898gxwrs19 foreign key (contrato_ID_CONTRATO) references comum.T_CONTRATO
