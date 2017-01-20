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

}
