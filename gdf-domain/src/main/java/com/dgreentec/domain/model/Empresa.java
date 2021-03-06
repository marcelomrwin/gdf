package com.dgreentec.domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
	@NotNull(message = "Empresa deve possuir um contrato obrigatoriamente")
	@JoinColumn(name = "COD_CONTRATO", foreignKey = @ForeignKey(name = "FK_COD_CONTRATO"))
	private Contrato contrato;

	@NotNull
	@Column(nullable = false, name = "SIG_UF")
	@Convert(converter = UFConverter.class)
	private UFEnum uf;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "COD_CERTIFICADO", foreignKey = @ForeignKey(name = "FK_CERTIFICADO"))
	private Certificado certificado;

	@Embedded
	private UltimoEventoNSU ultimoNSU;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "COD_AGENDAMENTO_SEFAZ", foreignKey = @ForeignKey(name = "FK_AGENDAMENTO"))
	private AgendamentoSefaz agendamentoSefaz = new AgendamentoSefaz();

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "COD_CNPJ", foreignKey = @ForeignKey(name = "FK_EMPRESA_NSU"))
	private List<EventoNSU> nsus = new ArrayList<>();

	public boolean executarEventoManifesto() {
		return (agendamentoSefaz.getProximaExecucao().isBefore(LocalDateTime.now())
				|| agendamentoSefaz.getProximaExecucao().isEqual(LocalDateTime.now()));
	}

	public void agendarEventoSefaz(TimeUnit unit, long value) {
		this.agendamentoSefaz.setEmExecucao(false);
		LocalDateTime next = LocalDateTime.now();
		switch (unit) {
		case SECONDS: {
			next = next.plusSeconds(value);
			break;
		}
		case MINUTES: {
			next = next.plusMinutes(value);
			break;
		}
		case HOURS: {
			next = next.plusHours(value);
			break;
		}
		default:
			throw new RuntimeException("Não é possível agendar para a unidade " + unit);
		}

		this.agendamentoSefaz.setProximaExecucao(next);
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

		public Builder comAgendamento(AgendamentoSefaz pAgendamento) {
			this.entity.setAgendamentoSefaz(pAgendamento);
			return this;
		}

	}

	public List<EventoNSU> getNsus() {
		return nsus;
	}

	public void setNsus(List<EventoNSU> nsus) {
		this.nsus = nsus;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((agendamentoSefaz == null) ? 0 : agendamentoSefaz.hashCode());
		result = prime * result + ((certificado == null) ? 0 : certificado.hashCode());
		result = prime * result + ((cnpj == null) ? 0 : cnpj.hashCode());
		result = prime * result + ((contrato == null) ? 0 : contrato.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((nsus == null) ? 0 : nsus.hashCode());
		result = prime * result + ((uf == null) ? 0 : uf.hashCode());
		result = prime * result + ((ultimoNSU == null) ? 0 : ultimoNSU.hashCode());
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
		if (!(obj instanceof Empresa)) {
			return false;
		}
		Empresa other = (Empresa) obj;
		if (agendamentoSefaz == null) {
			if (other.agendamentoSefaz != null) {
				return false;
			}
		} else if (!agendamentoSefaz.equals(other.agendamentoSefaz)) {
			return false;
		}
		if (certificado == null) {
			if (other.certificado != null) {
				return false;
			}
		} else if (!certificado.equals(other.certificado)) {
			return false;
		}
		if (cnpj == null) {
			if (other.cnpj != null) {
				return false;
			}
		} else if (!cnpj.equals(other.cnpj)) {
			return false;
		}
		if (contrato == null) {
			if (other.contrato != null) {
				return false;
			}
		} else if (!contrato.equals(other.contrato)) {
			return false;
		}
		if (nome == null) {
			if (other.nome != null) {
				return false;
			}
		} else if (!nome.equals(other.nome)) {
			return false;
		}
		if (nsus == null) {
			if (other.nsus != null) {
				return false;
			}
		} else if (!nsus.equals(other.nsus)) {
			return false;
		}
		if (uf != other.uf) {
			return false;
		}
		if (ultimoNSU == null) {
			if (other.ultimoNSU != null) {
				return false;
			}
		} else if (!ultimoNSU.equals(other.ultimoNSU)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final int maxLen = 10;
		StringBuilder builder2 = new StringBuilder();
		builder2.append("Empresa [");
		if (cnpj != null)
			builder2.append("cnpj=").append(cnpj).append(", ");
		if (nome != null)
			builder2.append("nome=").append(nome).append(", ");
		if (contrato != null)
			builder2.append("contrato=").append(contrato).append(", ");
		if (uf != null)
			builder2.append("uf=").append(uf).append(", ");
		if (certificado != null)
			builder2.append("certificado=").append(certificado).append(", ");
		if (ultimoNSU != null)
			builder2.append("ultimoNSU=").append(ultimoNSU).append(", ");
		if (agendamentoSefaz != null)
			builder2.append("agendamentoSefaz=").append(agendamentoSefaz).append(", ");
		if (nsus != null)
			builder2.append("nsus=").append(nsus.subList(0, Math.min(nsus.size(), maxLen))).append(", ");
		if (version != null)
			builder2.append("version=").append(version);
		builder2.append("]");
		return builder2.toString();
	}

	public AgendamentoSefaz getAgendamentoSefaz() {
		return agendamentoSefaz;
	}

	public void setAgendamentoSefaz(AgendamentoSefaz agendamentoSefaz) {
		this.agendamentoSefaz = agendamentoSefaz;
	}

}
