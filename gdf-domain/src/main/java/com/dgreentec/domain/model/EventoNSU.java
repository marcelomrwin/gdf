package com.dgreentec.domain.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import com.dgreentec.infrastructure.model.AbstractEntityVersion;
import com.dgreentec.infrastructure.model.SchemaEnum;
import com.dgreentec.infrastructure.persistence.converter.EventoSchemaConverter;

@Entity
@Table(name = "T_EVENTO_NSU", uniqueConstraints = @UniqueConstraint(columnNames = { "COD_CNPJ", "ID_NSU" }, name = "UNQ_EMPRESA_NSU"))
public class EventoNSU extends AbstractEntityVersion {

	public EventoNSU() {
		super();
	}

	public EventoNSU(Long idNsu, String codCNPJ, Date dtNSU, SchemaEnum schema) {
		super();
		this.idNsu = idNsu;
		this.codCNPJ = codCNPJ;
		this.dtNSU = dtNSU;
		this.schema = schema;
	}

	private static final long serialVersionUID = 7592541899670910173L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EVENTO_NSU_SEQ_GEN")
	@SequenceGenerator(name = "EVENTO_NSU_SEQ_GEN", sequenceName = "SEQ_ID_EVENTO_NSU", allocationSize = 1)
	@Column(name = "ID_SEQ_EVENTO_NSU", updatable = false)
	private Long idEventoNsu;

	@Column(name = "ID_NSU")
	private Long idNsu;

	@Column(name = "COD_CNPJ")
	private String codCNPJ;

	@Column(name = "DT_NSU", columnDefinition = "timestamp without time zone", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtNSU = new Date();

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

	public Long getIdEventoNsu() {
		return idEventoNsu;
	}

	public void setIdEventoNsu(Long idEventoNsu) {
		this.idEventoNsu = idEventoNsu;
	}

	public String getCodCNPJ() {
		return codCNPJ;
	}

	public void setCodCNPJ(String codCNPJ) {
		this.codCNPJ = codCNPJ;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((codCNPJ == null) ? 0 : codCNPJ.hashCode());
		result = prime * result + ((dtNSU == null) ? 0 : dtNSU.hashCode());
		result = prime * result + ((idEventoNsu == null) ? 0 : idEventoNsu.hashCode());
		result = prime * result + ((idNsu == null) ? 0 : idNsu.hashCode());
		result = prime * result + ((observacao == null) ? 0 : observacao.hashCode());
		result = prime * result + ((schema == null) ? 0 : schema.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		EventoNSU other = (EventoNSU) obj;
		if (codCNPJ == null) {
			if (other.codCNPJ != null)
				return false;
		} else if (!codCNPJ.equals(other.codCNPJ))
			return false;
		if (dtNSU == null) {
			if (other.dtNSU != null)
				return false;
		} else if (!dtNSU.equals(other.dtNSU))
			return false;
		if (idEventoNsu == null) {
			if (other.idEventoNsu != null)
				return false;
		} else if (!idEventoNsu.equals(other.idEventoNsu))
			return false;
		if (idNsu == null) {
			if (other.idNsu != null)
				return false;
		} else if (!idNsu.equals(other.idNsu))
			return false;
		if (observacao == null) {
			if (other.observacao != null)
				return false;
		} else if (!observacao.equals(other.observacao))
			return false;
		if (schema != other.schema)
			return false;
		return true;
	}
}
