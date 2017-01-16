package com.dgreentec.domain.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.dgreentec.infrastructure.model.AbstractEntityVersion;
import com.dgreentec.infrastructure.model.SchemaEnum;
import com.dgreentec.infrastructure.persistence.converter.EventoSchemaConverter;

@Entity
@Table(name = "T_EVENTO_NSU")
public class EventoNSU extends AbstractEntityVersion {

	private static final long serialVersionUID = 7592541899670910173L;

	@Id
	@Column(name = "ID_NSU")
	private Long idNsu;

	@Column(name = "DT_NSU", columnDefinition = "timestamp without time zone default CURRENT_TIMESTAMP", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtNSU;

	@Column(name = "TP_SCHEMA")
	@Convert(converter = EventoSchemaConverter.class)
	private SchemaEnum schema;

	@Column(name = "TXT_OBSERVACAO", length = 500)
	private String observacao;

	public Long getIdNsu() {
		return idNsu;
	}

	public void setIdNsu(Long idNsu) {
		this.idNsu = idNsu;
	}

	public Date getDtNSU() {
		return dtNSU;
	}

	public void setDtNSU(Date dtNSU) {
		this.dtNSU = dtNSU;
	}

	public SchemaEnum getSchema() {
		return schema;
	}

	public void setSchema(SchemaEnum schema) {
		this.schema = schema;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
}
