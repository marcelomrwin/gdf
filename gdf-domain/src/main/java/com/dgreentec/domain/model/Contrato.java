package com.dgreentec.domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.br.CNPJ;

import com.dgreentec.infrastructure.model.AbstractEntityBuilder;
import com.dgreentec.infrastructure.model.AbstractEntityVersion;
import com.dgreentec.infrastructure.util.CPFCNPJUtil;

@Entity
@Table(name = "T_CONTRATO", indexes = {
		@Index(name = "IDX_CNPJ", columnList = "COD_CNPJ") }, uniqueConstraints = @UniqueConstraint(name = "UNQ_CONTRATO_CNPJ", columnNames = "COD_CNPJ"))
@Cacheable
public class Contrato extends AbstractEntityVersion {

	private static final long serialVersionUID = -7508503257018177408L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID_CONTRATO")
	@SequenceGenerator(name = "SEQ_ID_CONTRATO", sequenceName = "SEQ_ID_CONTRATO", allocationSize = 1)
	@Column(name = "ID_CONTRATO")
	private Long idContrato;

	@CNPJ
	@Column(name = "COD_CNPJ", length = 14, unique = true)
	private String cnpj;

	@OneToMany(mappedBy = "contrato", fetch = FetchType.EAGER)
	private List<Empresa> empresas = new ArrayList<>();

	@ManyToOne
	@JoinColumn(name = "COD_TENANT", foreignKey = @ForeignKey(name = "FK_TENANT_CONTRATO"))
	@NotNull
	private Tenant tenant;

	@Column(name = "DT_VALIDADE")
	@NotNull
	private LocalDateTime validade;

	public String getCNPJSemFormatacao() {
		if (!stringValida(cnpj))
			throw new IllegalArgumentException("CNPJ NÃO PODE SER NULO OU VAZIO");
		return cnpj.replaceAll("[^0-9]", "").trim();
	}

	public String getCNPJFormatado() {
		if (!stringValida(cnpj))
			throw new IllegalArgumentException("CNPJ NÃO PODE SER NULO OU VAZIO");

		return CPFCNPJUtil.formatCPForCNPJ(cnpj);
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

		public Builder comTenant(Tenant pTenant) {
			this.entity.setTenant(pTenant);
			return this;
		}

		public Builder comValidade(LocalDateTime time) {
			this.entity.setValidade(time);
			return this;
		}

	}

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((cnpj == null) ? 0 : cnpj.hashCode());
		result = prime * result + ((empresas == null) ? 0 : empresas.hashCode());
		result = prime * result + ((idContrato == null) ? 0 : idContrato.hashCode());
		result = prime * result + ((tenant == null) ? 0 : tenant.hashCode());
		result = prime * result + ((validade == null) ? 0 : validade.hashCode());
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
		if (!(obj instanceof Contrato)) {
			return false;
		}
		Contrato other = (Contrato) obj;
		if (cnpj == null) {
			if (other.cnpj != null) {
				return false;
			}
		} else if (!cnpj.equals(other.cnpj)) {
			return false;
		}
		if (empresas == null) {
			if (other.empresas != null) {
				return false;
			}
		} else if (!empresas.equals(other.empresas)) {
			return false;
		}
		if (idContrato == null) {
			if (other.idContrato != null) {
				return false;
			}
		} else if (!idContrato.equals(other.idContrato)) {
			return false;
		}
		if (tenant == null) {
			if (other.tenant != null) {
				return false;
			}
		} else if (!tenant.equals(other.tenant)) {
			return false;
		}
		if (validade == null) {
			if (other.validade != null) {
				return false;
			}
		} else if (!validade.equals(other.validade)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final int maxLen = 10;
		StringBuilder builder2 = new StringBuilder();
		builder2.append("Contrato [");
		if (idContrato != null)
			builder2.append("idContrato=").append(idContrato).append(", ");
		if (cnpj != null)
			builder2.append("cnpj=").append(cnpj).append(", ");
		if (empresas != null)
			builder2.append("empresas=").append(empresas.subList(0, Math.min(empresas.size(), maxLen))).append(", ");
		if (tenant != null)
			builder2.append("tenant=").append(tenant).append(", ");
		if (validade != null)
			builder2.append("validade=").append(validade).append(", ");
		if (version != null)
			builder2.append("version=").append(version).append(", ");
		if (dataCriacao != null)
			builder2.append("dataCriacao=").append(dataCriacao).append(", ");
		if (dataUltimaAlteracao != null)
			builder2.append("dataUltimaAlteracao=").append(dataUltimaAlteracao);
		builder2.append("]");
		return builder2.toString();
	}

	public LocalDateTime getValidade() {
		return validade;
	}

	public void setValidade(LocalDateTime validade) {
		this.validade = validade;
	}

}
