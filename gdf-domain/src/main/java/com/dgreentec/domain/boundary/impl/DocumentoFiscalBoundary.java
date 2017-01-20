package com.dgreentec.domain.boundary.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.dgreentec.domain.boundary.api.DocumentoFiscalService;
import com.dgreentec.domain.model.Contrato;
import com.dgreentec.domain.model.DocumentoFiscal;
import com.dgreentec.domain.repository.DocumentoFiscalRepository;
import com.dgreentec.domain.repository.filter.FiltroDocumentoFiscal;
import com.dgreentec.infrastructure.persistence.pagination.PagedList;
import com.dgreentec.infrastructure.service.boundary.AbstractBoundary;

@Stateless
public class DocumentoFiscalBoundary extends AbstractBoundary implements DocumentoFiscalService {

	@Inject
	private DocumentoFiscalRepository documentoFiscalRepository;

	public PagedList<DocumentoFiscal> consultarDocumentoFiscals(Contrato contrato, FiltroDocumentoFiscal filtro) {
		return documentoFiscalRepository.consultar(filtro);
	}

	public DocumentoFiscal adicionarDocumentoFiscal(Contrato contrato, DocumentoFiscal pDocumentoFiscal) {
		DocumentoFiscal documento = documentoFiscalRepository.adicionar(pDocumentoFiscal);
		
		//após salvar incluir o documento numa estrutura de arquivos, amazon s3, ntfs, pasta no servidor ou afins
		
		return documento;
	}

	public DocumentoFiscal atualizarDocumentoFiscal(Contrato contrato, DocumentoFiscal pDocumentoFiscal) {
		return documentoFiscalRepository.atualizar(pDocumentoFiscal);
	}

	public void removerDocumentoFiscal(Contrato contrato, DocumentoFiscal pDocumentoFiscal) {
		documentoFiscalRepository.excluir(pDocumentoFiscal);
	}

	public DocumentoFiscal consultarDocumentoFiscalPorIdDocumento(Contrato contrato, Long idDocumento) {
		return documentoFiscalRepository.consultarPorChave(idDocumento);
	}

}
