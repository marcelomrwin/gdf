package com.dgreentec.domain.boundary.api;

import javax.ejb.Local;

import com.dgreentec.domain.model.Contrato;
import com.dgreentec.domain.model.DocumentoFiscal;
import com.dgreentec.domain.repository.filter.FiltroDocumentoFiscal;
import com.dgreentec.infrastructure.persistence.pagination.PagedList;

@Local
public interface DocumentoFiscalService {

	PagedList<DocumentoFiscal> consultarDocumentoFiscals(Contrato contrato, FiltroDocumentoFiscal filtro);

	DocumentoFiscal adicionarDocumentoFiscal(Contrato contrato, DocumentoFiscal pDocumentoFiscal);

	DocumentoFiscal atualizarDocumentoFiscal(Contrato contrato, DocumentoFiscal pDocumentoFiscal);

	void removerDocumentoFiscal(Contrato contrato, DocumentoFiscal pDocumentoFiscal);

	DocumentoFiscal consultarDocumentoFiscalPorIdDocumento(Contrato contrato, Long idDocumento);

}
