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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataUltimoNSU == null) ? 0 : dataUltimoNSU.hashCode());
		result = prime * result + ((ultimoNSU == null) ? 0 : ultimoNSU.hashCode());
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
		if (!(obj instanceof UltimoEventoNSU)) {
			return false;
		}
		UltimoEventoNSU other = (UltimoEventoNSU) obj;
		if (dataUltimoNSU == null) {
			if (other.dataUltimoNSU != null) {
				return false;
			}
		} else if (!dataUltimoNSU.equals(other.dataUltimoNSU)) {
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
		StringBuilder builder = new StringBuilder();
		builder.append("UltimoEventoNSU [");
		if (dataUltimoNSU != null)
			builder.append("dataUltimoNSU=").append(dataUltimoNSU).append(", ");
		if (ultimoNSU != null)
			builder.append("ultimoNSU=").append(ultimoNSU);
		builder.append("]");
		return builder.toString();
	}
}
