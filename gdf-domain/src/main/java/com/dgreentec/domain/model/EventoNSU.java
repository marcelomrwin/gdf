package com.dgreentec.domain.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.dgreentec.infrastructure.model.AbstractEntityVersion;
import com.dgreentec.infrastructure.model.SchemaEnum;
import com.dgreentec.infrastructure.persistence.converter.EventoSchemaConverter;

@Entity
@Table(name = "T_EVENTO_NSU", uniqueConstraints = @UniqueConstraint(columnNames = { "COD_CNPJ", "ID_NSU" }, name = "UNQ_EMPRESA_NSU"))
public class EventoNSU extends AbstractEntityVersion {

	public EventoNSU() {
		super();
	}

	private static final long serialVersionUID = 7592541899670910173L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EVENTO_NSU_SEQ_GEN")
	@SequenceGenerator(name = "EVENTO_NSU_SEQ_GEN", sequenceName = "SEQ_ID_EVENTO_NSU", allocationSize = 1)
	@Column(name = "ID_SEQ_EVENTO_NSU", updatable = false)
	private Long idEventoNsu;

	@Column(name = "ID_NSU")
	private Long idNsu;

	@Column(name = "DT_NSU", columnDefinition = "timestamp without time zone", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtNSU = new Date();

	@Column(name = "TP_SCHEMA")
	@Convert(converter = EventoSchemaConverter.class)
	private SchemaEnum schema;

	@Column(name = "TXT_OBSERVACAO", length = 500)
	private String observacao;

	@ManyToOne
	@JoinColumn(name = "COD_CNPJ", foreignKey = @ForeignKey(name = "FK_EVENTO_EMPRESA"))
	@NotNull
	private Empresa empresa;

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

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((dtNSU == null) ? 0 : dtNSU.hashCode());
		result = prime * result + ((empresa == null) ? 0 : empresa.hashCode());
		result = prime * result + ((idEventoNsu == null) ? 0 : idEventoNsu.hashCode());
		result = prime * result + ((idNsu == null) ? 0 : idNsu.hashCode());
		result = prime * result + ((observacao == null) ? 0 : observacao.hashCode());
		result = prime * result + ((schema == null) ? 0 : schema.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof EventoNSU)) {
			return false;
		}
		EventoNSU other = (EventoNSU) obj;
		if (dtNSU == null) {
			if (other.dtNSU != null) {
				return false;
			}
		} else if (!dtNSU.equals(other.dtNSU)) {
			return false;
		}
		if (empresa == null) {
			if (other.empresa != null) {
				return false;
			}
		} else if (!empresa.equals(other.empresa)) {
			return false;
		}
		if (idEventoNsu == null) {
			if (other.idEventoNsu != null) {
				return false;
			}
		} else if (!idEventoNsu.equals(other.idEventoNsu)) {
			return false;
		}
		if (idNsu == null) {
			if (other.idNsu != null) {
				return false;
			}
		} else if (!idNsu.equals(other.idNsu)) {
			return false;
		}
		if (observacao == null) {
			if (other.observacao != null) {
				return false;
			}
		} else if (!observacao.equals(other.observacao)) {
			return false;
		}
		if (schema != other.schema) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("EventoNSU [");
		if (idEventoNsu != null)
			builder.append("idEventoNsu=").append(idEventoNsu).append(", ");
		if (idNsu != null)
			builder.append("idNsu=").append(idNsu).append(", ");
		if (dtNSU != null)
			builder.append("dtNSU=").append(dtNSU).append(", ");
		if (schema != null)
			builder.append("schema=").append(schema).append(", ");
		if (observacao != null)
			builder.append("observacao=").append(observacao);
		builder.append("]");
		return builder.toString();
	}
}
