package com.dgreentec.domain.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Embeddable
public class UltimoEventoNSU implements Serializable {

	private static final long serialVersionUID = -438604231300141056L;

	@Column(name = "DT_ULTT_NSU", columnDefinition = "timestamp without time zone default CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataUltimoNSU = new Date();

	@Column(name = "NUM_ULT_NSU")
	private Long ultimoNSU = 0L;

	public Date getDataUltimoNSU() {
		return dataUltimoNSU;
	}

	public void setDataUltimoNSU(Date dataUltimoNSU) {
		this.dataUltimoNSU = dataUltimoNSU;
	}

	public Long getUltimoNSU() {
		return ultimoNSU;
	}

	public void setUltimoNSU(Long ultimoNSU) {
		this.ultimoNSU = ultimoNSU;
	}
}
