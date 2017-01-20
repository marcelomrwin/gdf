package com.dgreentec.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.dgreentec.infrastructure.model.AbstractEntityBuilder;
import com.dgreentec.infrastructure.model.AbstractEntityVersion;

@Entity
@Table(name = "T_LOTE_EVENTO")
public class LoteEvento extends AbstractEntityVersion {

	public LoteEvento() {
		super();
	}

	private static final long serialVersionUID = -1679231957768040084L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID_LOTE")
	@SequenceGenerator(name = "SEQ_ID_LOTE", sequenceName = "SEQ_ID_LOTE", allocationSize = 1)
	@Column(name = "ID_LOTE")
	private Integer idLoteEvento;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_CNPJ", foreignKey = @ForeignKey(name = "FK_LOTE_EVENTO_EMPRESA"))
	private Empresa empresa;

	public LoteEvento(Empresa pEmpresa) {
		this();
		this.empresa = pEmpresa;
	}

	public Integer getIdLoteEvento() {
		return idLoteEvento;
	}

	public void setIdLoteEvento(Integer idLoteEvento) {
		this.idLoteEvento = idLoteEvento;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public static class Builder extends AbstractEntityBuilder<LoteEvento> {

		public Builder comIdLoteEvento(Integer pIdLoteEvento) {
			this.entity.setIdLoteEvento(pIdLoteEvento);
			return this;
		}

		public Builder comEmpresa(Empresa pEmpresa) {
			this.entity.setEmpresa(pEmpresa);
			return this;
		}

	}
}
