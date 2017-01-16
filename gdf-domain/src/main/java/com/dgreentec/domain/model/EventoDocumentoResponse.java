package com.dgreentec.domain.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;

public class EventoDocumentoResponse implements Serializable {

	private static final long serialVersionUID = -5503253370433434425L;

	private Long ultimoNSu;

	private Long maxNSu;

	private List<EventoDocumento> eventos;

	public boolean possuiEventoRestante() {
		return ultimoNSu < maxNSu;
	}

	public Long getUltimoNSu() {
		return ultimoNSu;
	}

	public void setUltimoNSu(String ultimoNSu) {
		this.ultimoNSu = NumberUtils.toLong(ultimoNSu);
	}

	public Long getMaxNSu() {
		return maxNSu;
	}

	public void setMaxNSu(String maxNSu) {
		this.maxNSu = NumberUtils.toLong(maxNSu);
	}

	public List<EventoDocumento> getEventos() {
		return eventos;
	}

	public void setEventos(List<EventoDocumento> eventos) {
		this.eventos = eventos;
	}
}
