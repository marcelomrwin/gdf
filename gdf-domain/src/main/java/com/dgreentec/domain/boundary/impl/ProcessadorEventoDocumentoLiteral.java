package com.dgreentec.domain.boundary.impl;

import javax.enterprise.util.AnnotationLiteral;

import com.dgreentec.domain.model.TipoAmbienteEnum;

public class ProcessadorEventoDocumentoLiteral extends AnnotationLiteral<ProcessadorEventoDocumento> implements ProcessadorEventoDocumento {

	private static final long serialVersionUID = 8759615828097805991L;

	private long idContrato;

	private String cnpj;

	private TipoAmbienteEnum amb;

	public ProcessadorEventoDocumentoLiteral(long idContrato, String cnpj, TipoAmbienteEnum amb) {
		this.idContrato = idContrato;
		this.cnpj = cnpj;
		this.amb = amb;
	}

	@Override
	public String cnpj() {
		return cnpj;
	}

	@Override
	public TipoAmbienteEnum ambiente() {
		return this.amb;
	}

	@Override
	public long idContrato() {
		return idContrato;
	}

}
