alter table comum.T_USUARIO_TENANT drop constraint FK_TENANT_USUARIO
alter table comum.T_USUARIO_TENANT drop constraint FK_USUARIO_TENANT
drop table if exists comum.T_TENANT cascade
drop table if exists comum.T_USUARIO cascade
drop table if exists comum.T_USUARIO_TENANT cascade
drop sequence comum.ID_TENANT_SEQ
alter table T_CONTRATO drop constraint FK_TENANT_CONTRATO
alter table T_DOCUMENTO_FISCAL drop constraint FK_DOC_EMPRESA
alter table T_EMPRESA drop constraint FK_AGENDAMENTO
alter table T_EMPRESA drop constraint FK_CERTIFICADO
alter table T_EMPRESA drop constraint FK_COD_CONTRATO
alter table T_EVENTO_NSU drop constraint FK_EMPRESA_NSU
alter table T_LOTE_EVENTO drop constraint FK_LOTE_EVENTO_EMPRESA
drop table if exists T_CERTIFICADO cascade
drop table if exists T_CONTRATO cascade
drop table if exists T_DOCUMENTO_FISCAL cascade
drop table if exists T_EMPRESA cascade
drop table if exists T_EMPRESA_AGENDAMENTO cascade
drop table if exists T_EVENTO_NSU cascade
drop table if exists T_LOG_EVENTO_NOTIF cascade
drop table if exists T_LOTE_EVENTO cascade
drop sequence SEQ_ID_AGENDAMENTO
drop sequence SEQ_ID_CERTIFICADO
drop sequence SEQ_ID_CONTRATO
drop sequence SEQ_ID_DOCUMENTO
drop sequence SEQ_ID_EVENTO_NSU
drop sequence SEQ_ID_LOG_EVENTO
drop sequence SEQ_ID_LOTE
