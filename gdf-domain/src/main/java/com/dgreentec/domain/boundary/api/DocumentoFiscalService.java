package com.dgreentec.domain.boundary.api;

import javax.ejb.Local;

import com.dgreentec.domain.model.DocumentoFiscal;
import com.dgreentec.domain.model.Tenant;
import com.dgreentec.domain.repository.filter.FiltroDocumentoFiscal;
import com.dgreentec.infrastructure.persistence.pagination.PagedList;

@Local
public interface DocumentoFiscalService {

	PagedList<DocumentoFiscal> consultarDocumentoFiscals(Tenant tenant, FiltroDocumentoFiscal filtro);

	DocumentoFiscal adicionarDocumentoFiscal(Tenant tenant, DocumentoFiscal pDocumentoFiscal);

	DocumentoFiscal atualizarDocumentoFiscal(Tenant tenant, DocumentoFiscal pDocumentoFiscal);

	void removerDocumentoFiscal(Tenant tenant, DocumentoFiscal pDocumentoFiscal);

	DocumentoFiscal consultarDocumentoFiscalPorIdDocumento(Tenant tenant, Long idDocumento);

}
