package com.dgreentec.domain.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Embeddable
public class BloqueioSefaz implements Serializable {

	private static final long serialVersionUID = 2458069693783821498L;

	@NotNull
	@Column(name = "DT_EXPIRACAO")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtExpiracao;

	@NotNull
	@Column(name = "DT_CADASTRO_BLOQ")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtCadastroBloqueio;

	@Column(name = "TXT_BLOQUEIO", length = 500)
	private String textoBloqueio;

	public Date getDtExpiracao() {
		return dtExpiracao;
	}

	public void setDtExpiracao(Date dtExpiracao) {
		this.dtExpiracao = dtExpiracao;
	}

	public Date getDtCadastroBloqueio() {
		return dtCadastroBloqueio;
	}

	public void setDtCadastroBloqueio(Date dtCadastroBloqueio) {
		this.dtCadastroBloqueio = dtCadastroBloqueio;
	}

	public String getTextoBloqueio() {
		return textoBloqueio;
	}

	public void setTextoBloqueio(String textoBloqueio) {
		this.textoBloqueio = textoBloqueio;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dtCadastroBloqueio == null) ? 0 : dtCadastroBloqueio.hashCode());
		result = prime * result + ((dtExpiracao == null) ? 0 : dtExpiracao.hashCode());
		result = prime * result + ((textoBloqueio == null) ? 0 : textoBloqueio.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof BloqueioSefaz)) {
			return false;
		}
		BloqueioSefaz other = (BloqueioSefaz) obj;
		if (dtCadastroBloqueio == null) {
			if (other.dtCadastroBloqueio != null) {
				return false;
			}
		} else if (!dtCadastroBloqueio.equals(other.dtCadastroBloqueio)) {
			return false;
		}
		if (dtExpiracao == null) {
			if (other.dtExpiracao != null) {
				return false;
			}
		} else if (!dtExpiracao.equals(other.dtExpiracao)) {
			return false;
		}
		if (textoBloqueio == null) {
			if (other.textoBloqueio != null) {
				return false;
			}
		} else if (!textoBloqueio.equals(other.textoBloqueio)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BloqueioSefaz [");
		if (dtExpiracao != null)
			builder.append("dtExpiracao=").append(dtExpiracao).append(", ");
		if (dtCadastroBloqueio != null)
			builder.append("dtCadastroBloqueio=").append(dtCadastroBloqueio).append(", ");
		if (textoBloqueio != null)
			builder.append("textoBloqueio=").append(textoBloqueio);
		builder.append("]");
		return builder.toString();
	}

}
