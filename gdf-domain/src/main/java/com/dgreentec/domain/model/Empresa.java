package com.dgreentec.domain.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.br.CNPJ;

import com.dgreentec.infrastructure.model.AbstractEntityBuilder;
import com.dgreentec.infrastructure.model.AbstractEntityVersion;
import com.dgreentec.infrastructure.persistence.converter.UFConverter;

@Entity
@Table(name = "T_EMPRESA", indexes = { @Index(name = "IDX_NM_EMPRESA", columnList = "TXT_NOME") })
@Cacheable
public class Empresa extends AbstractEntityVersion {

	private static final long serialVersionUID = -5912485206783624737L;

	@Id
	@CNPJ
	@Column(name = "COD_CNPJ", length = 18)
	private String cnpj;

	@NotEmpty
	@Column(length = 300, nullable = false, name = "TXT_NOME")
	private String nome;

	@ManyToOne
	@NotNull(message="Empresa deve possuir um contrato obrigatoriamente")
	private Contrato contrato;

	@NotNull
	@Column(nullable = false, name = "SIG_UF")
	@Convert(converter = UFConverter.class)
	private UFEnum uf;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	private Certificado certificado;

	@Embedded
	private UltimoEventoNSU ultimoNSU;

	@Embedded
	private BloqueioSefaz bloqueioSefaz;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "COD_CNPJ", foreignKey = @ForeignKey(name = "FK_EMPRESA_NSU"))
	private List<EventoNSU> nsus = new ArrayList<>();

	public boolean existeBloqueioParaEvento() {
		if (bloqueioSefaz != null) {
			return bloqueioSefaz.getDtExpiracao().before(new Date());
		}
		return false;
	}

	public void adicionarNSU(EventoNSU evento) {
		this.nsus.add(evento);
	}

	public void removerNSU(EventoNSU evento) {
		this.nsus.remove(evento);
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Contrato getContrato() {
		return contrato;
	}

	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
	}

	public UFEnum getUf() {
		return uf;
	}

	public void setUf(UFEnum uf) {
		this.uf = uf;
	}

	public Certificado getCertificado() {
		return certificado;
	}

	public void setCertificado(Certificado certificado) {
		this.certificado = certificado;
	}

	public UltimoEventoNSU getUltimoNSU() {
		if (ultimoNSU == null)
			return new UltimoEventoNSU();

		return ultimoNSU;
	}

	public void setUltimoNSU(UltimoEventoNSU ultimoNSU) {
		this.ultimoNSU = ultimoNSU;
	}

	public static class Builder extends AbstractEntityBuilder<Empresa> {

		public Builder comCertificado(Certificado pCertificado) {
			this.entity.setCertificado(pCertificado);
			return this;
		}

		public Builder comContrato(Contrato pContrato) {
			this.entity.setContrato(pContrato);
			return this;
		}

		public Builder comNome(String pNome) {
			this.entity.setNome(pNome);
			return this;
		}

		public Builder comCnpj(String pCnpj) {
			this.entity.setCnpj(pCnpj);
			return this;
		}

		public Builder comUltimoNSU(UltimoEventoNSU pUltimoNSU) {
			this.entity.setUltimoNSU(pUltimoNSU);
			return this;
		}

		public Builder comUf(UFEnum pUf) {
			this.entity.setUf(pUf);
			return this;
		}

	}

	public BloqueioSefaz getBloqueioSefaz() {
		return bloqueioSefaz;
	}

	public void setBloqueioSefaz(BloqueioSefaz bloqueioSefaz) {
		this.bloqueioSefaz = bloqueioSefaz;
	}

	public List<EventoNSU> getNsus() {
		return nsus;
	}

	public void setNsus(List<EventoNSU> nsus) {
		this.nsus = nsus;
	}

}
