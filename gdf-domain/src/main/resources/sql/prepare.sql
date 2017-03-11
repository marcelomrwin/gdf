--drop comum
DROP SCHEMA IF EXISTS comum CASCADE;
CREATE SCHEMA comum AUTHORIZATION gdf_user;

--drop
DROP SCHEMA IF EXISTS scm_07932968000103 CASCADE;
DROP SCHEMA IF EXISTS scm_78570595000108 CASCADE;

--create
CREATE SCHEMA scm_07932968000103 AUTHORIZATION gdf_user;
CREATE SCHEMA scm_78570595000108 AUTHORIZATION gdf_user;

SET SCHEMA 'comum';

create sequence comum.ID_TENANT_SEQ start 1 increment 1
create table comum.T_TENANT (ID_TENANT int8 not null, DAT_CRIACAO timestamp without time zone not null, DAT_ULTIMA_ALTERACAO timestamp without time zone not null, NUM_VERSAO INTEGER DEFAULT 0 not null, TXT_NM_TENANT varchar(255) not null, TXT_NM_ESQUEMA varchar(255) not null, primary key (ID_TENANT))
create table comum.T_USUARIO (NUM_CPF varchar(11) not null, DAT_CRIACAO timestamp without time zone not null, DAT_ULTIMA_ALTERACAO timestamp without time zone not null, NUM_VERSAO INTEGER DEFAULT 0 not null, TXT_NOME varchar(250) not null, primary key (NUM_CPF))
create table comum.T_USUARIO_TENANT (NUM_CPF varchar(11) not null, ID_TENANT int8 not null, primary key (NUM_CPF, ID_TENANT))
alter table comum.T_TENANT add constraint UK_UNQ_TENANT_NM_SCHEMA unique (TXT_NM_ESQUEMA)

alter table comum.T_USUARIO_TENANT add constraint FK_TENANT_USUARIO foreign key (ID_TENANT) references comum.T_TENANT
alter table comum.T_USUARIO_TENANT add constraint FK_USUARIO_TENANT foreign key (NUM_CPF) references comum.T_USUARIO

INSERT INTO comum.t_tenant(id_tenant, dat_criacao, dat_ultima_alteracao, num_versao, txt_nm_tenant, txt_nm_esquema)	VALUES (nextval('comum.ID_TENANT_SEQ'), current_timestamp, current_timestamp, 0, 'TENANT RIOPOLEM', 'scm_07932968000103');
INSERT INTO comum.t_tenant(id_tenant, dat_criacao, dat_ultima_alteracao, num_versao, txt_nm_tenant, txt_nm_esquema)	VALUES (nextval('comum.ID_TENANT_SEQ'), current_timestamp, current_timestamp, 0, 'TENANT DDX', 'scm_78570595000108');

INSERT INTO comum.t_usuario(num_cpf, dat_criacao, dat_ultima_alteracao, num_versao, txt_nome) VALUES ('03210392463', current_timestamp, current_timestamp, 0, 'marcelo daniel da silva sales');
INSERT INTO comum.t_usuario(num_cpf, dat_criacao, dat_ultima_alteracao, num_versao, txt_nome) VALUES ('12345678909', current_timestamp, current_timestamp, 0, 'usuario A');
INSERT INTO comum.t_usuario(num_cpf, dat_criacao, dat_ultima_alteracao, num_versao, txt_nome) VALUES ('90987654321', current_timestamp, current_timestamp, 0, 'usuario B');

INSERT INTO comum.t_usuario_tenant(num_cpf, id_tenant) VALUES ('03210392463', 1);
INSERT INTO comum.t_usuario_tenant(num_cpf, id_tenant) VALUES ('03210392463', 2);
INSERT INTO comum.t_usuario_tenant(num_cpf, id_tenant) VALUES ('12345678909', 1);
INSERT INTO comum.t_usuario_tenant(num_cpf, id_tenant) VALUES ('90987654321', 2);

SET SCHEMA 'scm_07932968000103';

create sequence SEQ_ID_AGENDAMENTO start 1 increment 1;
create sequence SEQ_ID_CERTIFICADO start 1 increment 1;
create sequence SEQ_ID_CONTRATO start 1 increment 1;
create sequence SEQ_ID_DOCUMENTO start 1 increment 1;
create sequence SEQ_ID_EVENTO_NSU start 1 increment 1;
create sequence SEQ_ID_LOG_EVENTO start 1 increment 1;
create sequence SEQ_ID_LOTE start 1 increment 1;
create table T_CERTIFICADO (ID_CERTIFICADO int8 not null, DAT_CRIACAO timestamp without time zone not null, DAT_ULTIMA_ALTERACAO timestamp without time zone not null, NUM_VERSAO INTEGER DEFAULT 0 not null, BIN_ARQUIVO bytea not null, DT_VENCIMENTO timestamp, TXT_SENHA varchar(300) not null, primary key (ID_CERTIFICADO));
create table T_CONTRATO (ID_CONTRATO int8 not null, DAT_CRIACAO timestamp without time zone not null, DAT_ULTIMA_ALTERACAO timestamp without time zone not null, NUM_VERSAO INTEGER DEFAULT 0 not null, COD_CNPJ varchar(14), DT_VALIDADE timestamp not null, COD_TENANT int8 not null, primary key (ID_CONTRATO));
create table T_DOCUMENTO_FISCAL (ID_DOCUMENTO int8 not null, DAT_CRIACAO timestamp without time zone not null, DAT_ULTIMA_ALTERACAO timestamp without time zone not null, NUM_VERSAO INTEGER DEFAULT 0 not null, XML_DOCUMENTO XML not null, NUM_DOCUMENTO varchar(50) not null, ID_TP_DOCUMENTO int4 not null, COD_CNPJ varchar(18), primary key (ID_DOCUMENTO));
create table T_EMPRESA (COD_CNPJ varchar(18) not null, DAT_CRIACAO timestamp without time zone not null, DAT_ULTIMA_ALTERACAO timestamp without time zone not null, NUM_VERSAO INTEGER DEFAULT 0 not null, TXT_NOME varchar(300) not null, SIG_UF varchar(255) not null, DT_ULTT_NSU timestamp without time zone default CURRENT_TIMESTAMP, NUM_ULT_NSU int8, COD_AGENDAMENTO_SEFAZ int4, COD_CERTIFICADO int8, COD_CONTRATO int8 not null, primary key (COD_CNPJ));
create table T_EMPRESA_AGENDAMENTO (ID_AGENDAMENTO int4 not null, DAT_CRIACAO timestamp without time zone not null, DAT_ULTIMA_ALTERACAO timestamp without time zone not null, NUM_VERSAO INTEGER DEFAULT 0 not null, IND_BLOQUEIO_SEFAZ boolean, DT_CADASTRO_AGENDAMENTO timestamp not null, IND_AGENDAMENTO_EM_EXEC boolean, DT_PROXIMA_EXECUCAO timestamp not null, TXT_AGENDAMENTO varchar(500), primary key (ID_AGENDAMENTO));
create table T_EVENTO_NSU (ID_SEQ_EVENTO_NSU int8 not null, DAT_CRIACAO timestamp without time zone not null, DAT_ULTIMA_ALTERACAO timestamp without time zone not null, NUM_VERSAO INTEGER DEFAULT 0 not null, COD_CNPJ varchar(255), DT_NSU timestamp without time zone not null, ID_NSU int8, TXT_OBSERVACAO varchar(500), TP_SCHEMA varchar(255), primary key (ID_SEQ_EVENTO_NSU));
create table T_LOG_EVENTO_NOTIF (ID_SEQ_LOG_EVENTO int8 not null, DAT_CRIACAO timestamp without time zone not null, DAT_ULTIMA_ALTERACAO timestamp without time zone not null, NUM_VERSAO INTEGER DEFAULT 0 not null, COD_UF_ORGAO int4, COD_STATUS int4, TXT_CHAVE_NFE varchar(44), DT_REGISTRO timestamp, COD_PROTOCOLO varchar(15), NUM_SEQ_EVENTO int4, IND_TP_AMBIENTE int4, COD_TP_EVENTO int4, TXT_VERSAO_APP varchar(100), TXT_EVENTO varchar(255), TXT_MOTIVO varchar(255), primary key (ID_SEQ_LOG_EVENTO));
create table T_LOTE_EVENTO (ID_LOTE int4 not null, DAT_CRIACAO timestamp without time zone not null, DAT_ULTIMA_ALTERACAO timestamp without time zone not null, NUM_VERSAO INTEGER DEFAULT 0 not null, COD_CNPJ varchar(18) not null, primary key (ID_LOTE));
create index IDX_CNPJ on T_CONTRATO (COD_CNPJ);
alter table T_CONTRATO add constraint UNQ_COD_CNPJ unique (COD_CNPJ);
create index IDX_DOC_NUM_DOC on T_DOCUMENTO_FISCAL (NUM_DOCUMENTO);
create index IDX_NM_EMPRESA on T_EMPRESA (TXT_NOME);
alter table T_EVENTO_NSU add constraint UNQ_EMPRESA_NSU unique (COD_CNPJ, ID_NSU);
alter table T_DOCUMENTO_FISCAL add constraint FK_DOC_EMPRESA foreign key (COD_CNPJ) references T_EMPRESA;
alter table T_EMPRESA add constraint FK_AGENDAMENTO foreign key (COD_AGENDAMENTO_SEFAZ) references T_EMPRESA_AGENDAMENTO;
alter table T_EMPRESA add constraint FK_CERTIFICADO foreign key (COD_CERTIFICADO) references T_CERTIFICADO;
alter table T_EMPRESA add constraint FK_COD_CONTRATO foreign key (COD_CONTRATO) references T_CONTRATO;
alter table T_EVENTO_NSU add constraint FK_EMPRESA_NSU foreign key (COD_CNPJ) references T_EMPRESA;
alter table T_LOTE_EVENTO add constraint FK_LOTE_EVENTO_EMPRESA foreign key (COD_CNPJ) references T_EMPRESA;

SET SCHEMA 'scm_78570595000108';

create sequence SEQ_ID_AGENDAMENTO start 1 increment 1;
create sequence SEQ_ID_CERTIFICADO start 1 increment 1;
create sequence SEQ_ID_CONTRATO start 1 increment 1;
create sequence SEQ_ID_DOCUMENTO start 1 increment 1;
create sequence SEQ_ID_EVENTO_NSU start 1 increment 1;
create sequence SEQ_ID_LOG_EVENTO start 1 increment 1;
create sequence SEQ_ID_LOTE start 1 increment 1;
create table T_CERTIFICADO (ID_CERTIFICADO int8 not null, DAT_CRIACAO timestamp without time zone not null, DAT_ULTIMA_ALTERACAO timestamp without time zone not null, NUM_VERSAO INTEGER DEFAULT 0 not null, BIN_ARQUIVO bytea not null, DT_VENCIMENTO timestamp, TXT_SENHA varchar(300) not null, primary key (ID_CERTIFICADO));
create table T_CONTRATO (ID_CONTRATO int8 not null, DAT_CRIACAO timestamp without time zone not null, DAT_ULTIMA_ALTERACAO timestamp without time zone not null, NUM_VERSAO INTEGER DEFAULT 0 not null, COD_CNPJ varchar(14), DT_VALIDADE timestamp not null, COD_TENANT int8 not null, primary key (ID_CONTRATO));
create table T_DOCUMENTO_FISCAL (ID_DOCUMENTO int8 not null, DAT_CRIACAO timestamp without time zone not null, DAT_ULTIMA_ALTERACAO timestamp without time zone not null, NUM_VERSAO INTEGER DEFAULT 0 not null, XML_DOCUMENTO XML not null, NUM_DOCUMENTO varchar(50) not null, ID_TP_DOCUMENTO int4 not null, COD_CNPJ varchar(18), primary key (ID_DOCUMENTO));
create table T_EMPRESA (COD_CNPJ varchar(18) not null, DAT_CRIACAO timestamp without time zone not null, DAT_ULTIMA_ALTERACAO timestamp without time zone not null, NUM_VERSAO INTEGER DEFAULT 0 not null, TXT_NOME varchar(300) not null, SIG_UF varchar(255) not null, DT_ULTT_NSU timestamp without time zone default CURRENT_TIMESTAMP, NUM_ULT_NSU int8, COD_AGENDAMENTO_SEFAZ int4, COD_CERTIFICADO int8, COD_CONTRATO int8 not null, primary key (COD_CNPJ));
create table T_EMPRESA_AGENDAMENTO (ID_AGENDAMENTO int4 not null, DAT_CRIACAO timestamp without time zone not null, DAT_ULTIMA_ALTERACAO timestamp without time zone not null, NUM_VERSAO INTEGER DEFAULT 0 not null, IND_BLOQUEIO_SEFAZ boolean, DT_CADASTRO_AGENDAMENTO timestamp not null, IND_AGENDAMENTO_EM_EXEC boolean, DT_PROXIMA_EXECUCAO timestamp not null, TXT_AGENDAMENTO varchar(500), primary key (ID_AGENDAMENTO));
create table T_EVENTO_NSU (ID_SEQ_EVENTO_NSU int8 not null, DAT_CRIACAO timestamp without time zone not null, DAT_ULTIMA_ALTERACAO timestamp without time zone not null, NUM_VERSAO INTEGER DEFAULT 0 not null, COD_CNPJ varchar(255), DT_NSU timestamp without time zone not null, ID_NSU int8, TXT_OBSERVACAO varchar(500), TP_SCHEMA varchar(255), primary key (ID_SEQ_EVENTO_NSU));
create table T_LOG_EVENTO_NOTIF (ID_SEQ_LOG_EVENTO int8 not null, DAT_CRIACAO timestamp without time zone not null, DAT_ULTIMA_ALTERACAO timestamp without time zone not null, NUM_VERSAO INTEGER DEFAULT 0 not null, COD_UF_ORGAO int4, COD_STATUS int4, TXT_CHAVE_NFE varchar(44), DT_REGISTRO timestamp, COD_PROTOCOLO varchar(15), NUM_SEQ_EVENTO int4, IND_TP_AMBIENTE int4, COD_TP_EVENTO int4, TXT_VERSAO_APP varchar(100), TXT_EVENTO varchar(255), TXT_MOTIVO varchar(255), primary key (ID_SEQ_LOG_EVENTO));
create table T_LOTE_EVENTO (ID_LOTE int4 not null, DAT_CRIACAO timestamp without time zone not null, DAT_ULTIMA_ALTERACAO timestamp without time zone not null, NUM_VERSAO INTEGER DEFAULT 0 not null, COD_CNPJ varchar(18) not null, primary key (ID_LOTE));
create index IDX_CNPJ on T_CONTRATO (COD_CNPJ);
alter table T_CONTRATO add constraint UNQ_COD_CNPJ unique (COD_CNPJ);
create index IDX_DOC_NUM_DOC on T_DOCUMENTO_FISCAL (NUM_DOCUMENTO);
create index IDX_NM_EMPRESA on T_EMPRESA (TXT_NOME);
alter table T_EVENTO_NSU add constraint UNQ_EMPRESA_NSU unique (COD_CNPJ, ID_NSU);
alter table T_DOCUMENTO_FISCAL add constraint FK_DOC_EMPRESA foreign key (COD_CNPJ) references T_EMPRESA;
alter table T_EMPRESA add constraint FK_AGENDAMENTO foreign key (COD_AGENDAMENTO_SEFAZ) references T_EMPRESA_AGENDAMENTO;
alter table T_EMPRESA add constraint FK_CERTIFICADO foreign key (COD_CERTIFICADO) references T_CERTIFICADO;
alter table T_EMPRESA add constraint FK_COD_CONTRATO foreign key (COD_CONTRATO) references T_CONTRATO;
alter table T_EVENTO_NSU add constraint FK_EMPRESA_NSU foreign key (COD_CNPJ) references T_EMPRESA;
alter table T_LOTE_EVENTO add constraint FK_LOTE_EVENTO_EMPRESA foreign key (COD_CNPJ) references T_EMPRESA;