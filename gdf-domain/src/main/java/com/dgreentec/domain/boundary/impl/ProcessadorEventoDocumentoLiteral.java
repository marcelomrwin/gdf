package com.dgreentec.domain.boundary.impl;

import javax.enterprise.util.AnnotationLiteral;

import com.dgreentec.domain.model.TipoAmbienteEnum;

public class ProcessadorEventoDocumentoLiteral extends AnnotationLiteral<ProcessadorEventoDocumento> implements ProcessadorEventoDocumento {

	private static final long serialVersionUID = 8759615828097805991L;

	private String cnpj;

	private long nsu;

	private TipoAmbienteEnum amb;

	public ProcessadorEventoDocumentoLiteral(String cnpj, long nsu, TipoAmbienteEnum amb) {
		this.cnpj = cnpj;
		this.nsu = nsu;
		this.amb = amb;
	}

	@Override
	public String cnpj() {
		return cnpj;
	}

	@Override
	public long ultimoNSU() {
		return nsu;
	}

	@Override
	public TipoAmbienteEnum ambiente() {
		return this.amb;
	}

}
