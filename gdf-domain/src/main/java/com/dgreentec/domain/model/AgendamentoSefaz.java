package com.dgreentec.domain.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.dgreentec.infrastructure.model.AbstractEntityVersion;

@Entity
@Table(name = "T_EMPRESA_AGENDAMENTO")
public class AgendamentoSefaz extends AbstractEntityVersion {

	public AgendamentoSefaz() {
		super();
	}

	public AgendamentoSefaz(LocalDateTime proximaExecucao, LocalDateTime dtCadastroAgendamento, String textoAgendamento) {
		super();
		this.proximaExecucao = proximaExecucao;
		this.dtCadastroAgendamento = dtCadastroAgendamento;
		this.textoAgendamento = textoAgendamento;
	}

	private static final long serialVersionUID = 2458069693783821498L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID_AGENDAMENTO")
	@SequenceGenerator(name = "SEQ_ID_AGENDAMENTO", sequenceName = "SEQ_ID_AGENDAMENTO", allocationSize = 1)
	@Column(name = "ID_AGENDAMENTO")
	private Integer idAgendamento;

	@NotNull
	@Column(name = "DT_PROXIMA_EXECUCAO")
	private LocalDateTime proximaExecucao = LocalDateTime.now();

	@NotNull
	@Column(name = "DT_CADASTRO_AGENDAMENTO")
	private LocalDateTime dtCadastroAgendamento = LocalDateTime.now();

	@Column(name = "TXT_AGENDAMENTO", length = 500)
	private String textoAgendamento = "Agendamento normal";

	@Column(name = "IND_AGENDAMENTO_EM_EXEC")
	private Boolean emExecucao = Boolean.FALSE;

	@Column(name = "IND_BLOQUEIO_SEFAZ")
	private Boolean bloqueio = Boolean.FALSE;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((bloqueio == null) ? 0 : bloqueio.hashCode());
		result = prime * result + ((dtCadastroAgendamento == null) ? 0 : dtCadastroAgendamento.hashCode());
		result = prime * result + ((emExecucao == null) ? 0 : emExecucao.hashCode());
		result = prime * result + ((idAgendamento == null) ? 0 : idAgendamento.hashCode());
		result = prime * result + ((proximaExecucao == null) ? 0 : proximaExecucao.hashCode());
		result = prime * result + ((textoAgendamento == null) ? 0 : textoAgendamento.hashCode());
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
		if (!(obj instanceof AgendamentoSefaz)) {
			return false;
		}
		AgendamentoSefaz other = (AgendamentoSefaz) obj;
		if (bloqueio == null) {
			if (other.bloqueio != null) {
				return false;
			}
		} else if (!bloqueio.equals(other.bloqueio)) {
			return false;
		}
		if (dtCadastroAgendamento == null) {
			if (other.dtCadastroAgendamento != null) {
				return false;
			}
		} else if (!dtCadastroAgendamento.equals(other.dtCadastroAgendamento)) {
			return false;
		}
		if (emExecucao == null) {
			if (other.emExecucao != null) {
				return false;
			}
		} else if (!emExecucao.equals(other.emExecucao)) {
			return false;
		}
		if (idAgendamento == null) {
			if (other.idAgendamento != null) {
				return false;
			}
		} else if (!idAgendamento.equals(other.idAgendamento)) {
			return false;
		}
		if (proximaExecucao == null) {
			if (other.proximaExecucao != null) {
				return false;
			}
		} else if (!proximaExecucao.equals(other.proximaExecucao)) {
			return false;
		}
		if (textoAgendamento == null) {
			if (other.textoAgendamento != null) {
				return false;
			}
		} else if (!textoAgendamento.equals(other.textoAgendamento)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AgendamentoSefaz [");
		if (idAgendamento != null)
			builder.append("idAgendamento=").append(idAgendamento).append(", ");
		if (proximaExecucao != null)
			builder.append("proximaExecucao=").append(proximaExecucao).append(", ");
		if (dtCadastroAgendamento != null)
			builder.append("dtCadastroAgendamento=").append(dtCadastroAgendamento).append(", ");
		if (textoAgendamento != null)
			builder.append("textoAgendamento=").append(textoAgendamento).append(", ");
		if (emExecucao != null)
			builder.append("emExecucao=").append(emExecucao).append(", ");
		if (bloqueio != null)
			builder.append("bloqueio=").append(bloqueio).append(", ");
		if (version != null)
			builder.append("version=").append(version);
		builder.append("]");
		return builder.toString();
	}

	public Integer getIdAgendamento() {
		return idAgendamento;
	}

	public void setIdAgendamento(Integer idAgendamento) {
		this.idAgendamento = idAgendamento;
	}

	public LocalDateTime getProximaExecucao() {
		return proximaExecucao;
	}

	public void setProximaExecucao(LocalDateTime proximaExecucao) {
		this.proximaExecucao = proximaExecucao;
	}

	public LocalDateTime getDtCadastroAgendamento() {
		return dtCadastroAgendamento;
	}

	public void setDtCadastroAgendamento(LocalDateTime dtCadastroAgendamento) {
		this.dtCadastroAgendamento = dtCadastroAgendamento;
	}

	public String getTextoAgendamento() {
		return textoAgendamento;
	}

	public void setTextoAgendamento(String textoAgendamento) {
		this.textoAgendamento = textoAgendamento;
	}

	public Boolean getEmExecucao() {
		return emExecucao;
	}

	public void setEmExecucao(Boolean emExecucao) {
		this.emExecucao = emExecucao;
	}

	public Boolean getBloqueio() {
		return bloqueio;
	}

	public void setBloqueio(Boolean bloqueio) {
		this.bloqueio = bloqueio;
	}

}
