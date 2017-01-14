package com.dgreentec.domain.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.validator.constraints.br.CNPJ;

import com.dgreentec.infrastructure.model.AbstractEntityBuilder;
import com.dgreentec.infrastructure.model.AbstractEntityVersion;

@Entity
@Table(name = "T_CONTRATO", schema = "comum", indexes = { @Index(name = "IDX_CNPJ", columnList = "COD_CNPJ") })
@Cacheable
public class Contrato extends AbstractEntityVersion {

	private static final long serialVersionUID = -7508503257018177408L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID_CONTRATO")
	@SequenceGenerator(name = "SEQ_ID_CONTRATO", sequenceName = "SEQ_ID_CONTRATO", allocationSize = 1)
	@Column(name = "ID_CONTRATO")
	private Long idContrato;

	@CNPJ
	@Column(name = "COD_CNPJ", length = 18)
	private String cnpj;

	@OneToMany(mappedBy = "contrato",fetch=FetchType.EAGER)
	private List<Empresa> empresas = new ArrayList<>();

	public String getCNPJSemFormatacao() {
		if (!stringValida(cnpj))
			throw new IllegalArgumentException("CNPJ NÃO PODE SER NULO OU VAZIO");
		return cnpj.replaceAll("[^0-9]", "").trim();
	}

	public void adicionarEmpresa(Empresa empresa) {
		empresa.setContrato(this);
		this.empresas.add(empresa);
	}

	public void removerEmpresa(Empresa empresa) {
		empresa.setContrato(null);
		this.empresas.remove(empresa);
	}

	public Long getIdContrato() {
		return idContrato;
	}

	public void setIdContrato(Long idContrato) {
		this.idContrato = idContrato;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public List<Empresa> getEmpresas() {
		return empresas;
	}

	public void setEmpresas(List<Empresa> empresas) {
		this.empresas = empresas;
	}

	public static class Builder extends AbstractEntityBuilder<Contrato> {

		public Builder comIdContrato(Long pIdContrato) {
			this.entity.setIdContrato(pIdContrato);
			return this;
		}

		public Builder comCnpj(String pCnpj) {
			this.entity.setCnpj(pCnpj);
			return this;
		}

	}

}
