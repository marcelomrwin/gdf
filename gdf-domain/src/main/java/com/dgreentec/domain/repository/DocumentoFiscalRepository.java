package com.dgreentec.domain.repository;


import com.dgreentec.domain.model.DocumentoFiscal;
import com.dgreentec.domain.repository.filter.FiltroDocumentoFiscal;
import com.dgreentec.infrastructure.persistence.ModelRepositoryJPA;
import com.dgreentec.infrastructure.persistence.pagination.PagedList;

public interface DocumentoFiscalRepository extends ModelRepositoryJPA<DocumentoFiscal> {

	PagedList<DocumentoFiscal> consultar(FiltroDocumentoFiscal filtro);

}
