package com.dgreentec.domain.boundary.impl.eventodocumento;

import javax.enterprise.util.AnnotationLiteral;

import com.dgreentec.domain.model.TipoAmbienteEnum;

public class ProcessadorEventoDocumentoLiteral extends AnnotationLiteral<ProcessadorEventoDocumento> implements ProcessadorEventoDocumento {

	private static final long serialVersionUID = 8759615828097805991L;

	private long idTenant;

	private String cnpj;

	private TipoAmbienteEnum amb;

	public ProcessadorEventoDocumentoLiteral(long idTenant, String cnpj, TipoAmbienteEnum amb) {
		this.idTenant = idTenant;
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
	public long idTenant() {
		return idTenant;
	}

}
