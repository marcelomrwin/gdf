package com.dgreentec.domain.boundary.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

import com.dgreentec.domain.boundary.api.DocumentoFiscalService;
import com.dgreentec.domain.model.DocumentoFiscal;
import com.dgreentec.domain.model.Tenant;
import com.dgreentec.domain.repository.DocumentoFiscalRepository;
import com.dgreentec.domain.repository.filter.FiltroDocumentoFiscal;
import com.dgreentec.infrastructure.interceptor.EJBTransactionInterceptor;
import com.dgreentec.infrastructure.persistence.pagination.PagedList;
import com.dgreentec.infrastructure.repository.multitenant.TenantInterceptor;
import com.dgreentec.infrastructure.service.boundary.AbstractBoundary;

@Stateless
@Interceptors({ TenantInterceptor.class, EJBTransactionInterceptor.class })
public class DocumentoFiscalBoundary extends AbstractBoundary implements DocumentoFiscalService {

	@Inject
	private DocumentoFiscalRepository documentoFiscalRepository;

	public PagedList<DocumentoFiscal> consultarDocumentoFiscals(Tenant tenant, FiltroDocumentoFiscal filtro) {
		return documentoFiscalRepository.consultar(filtro);
	}

	public DocumentoFiscal adicionarDocumentoFiscal(Tenant tenant, DocumentoFiscal pDocumentoFiscal) {
		DocumentoFiscal documento = documentoFiscalRepository.adicionar(pDocumentoFiscal);

		//após salvar incluir o documento numa estrutura de arquivos, amazon s3, ntfs, pasta no servidor ou afins

		return documento;
	}

	public DocumentoFiscal atualizarDocumentoFiscal(Tenant tenant, DocumentoFiscal pDocumentoFiscal) {
		return documentoFiscalRepository.atualizar(pDocumentoFiscal);
	}

	public void removerDocumentoFiscal(Tenant tenant, DocumentoFiscal pDocumentoFiscal) {
		documentoFiscalRepository.excluir(pDocumentoFiscal);
	}

	public DocumentoFiscal consultarDocumentoFiscalPorIdDocumento(Tenant tenant, Long idDocumento) {
		return documentoFiscalRepository.consultarPorChave(idDocumento);
	}

}
