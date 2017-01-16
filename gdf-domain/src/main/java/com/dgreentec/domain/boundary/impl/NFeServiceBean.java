package com.dgreentec.domain.boundary.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.annotation.Resource;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.dgreentec.domain.boundary.api.NFeService;
import com.dgreentec.domain.model.Contrato;
import com.dgreentec.domain.model.DocumentoFiscal;
import com.dgreentec.domain.model.Empresa;
import com.dgreentec.domain.model.EventoDocumento;
import com.dgreentec.domain.model.EventoDocumentoResponse;
import com.dgreentec.domain.model.TipoAmbienteEnum;
import com.dgreentec.domain.model.TipoDocumentoEnum;
import com.dgreentec.domain.repository.EmpresaRepository;
import com.dgreentec.domain.xsd.procEventoNFe_v100.ProcEventoNFe;
import com.dgreentec.domain.xsd.procNFe_v310.NfeProc;
import com.dgreentec.domain.xsd.resEvento_v101.ResEvento;
import com.dgreentec.domain.xsd.resNFe_v101.ResNFe;
import com.dgreentec.infrastructure.exception.NfeException;
import com.dgreentec.infrastructure.util.JaxbUtils;

@Stateless
public class NFeServiceBean implements NFeService {

	@Inject
	protected Logger logger;

	@Resource(name = "java:comp/DefaultManagedExecutorService")
	private ManagedExecutorService executor;

	@Inject
	private EmpresaRepository empresaRepository;

	@Inject
	@Any
	Instance<ProcessadorEventoDocumentoEmpresa> processadorEventoInstance;

	@Override
	@Asynchronous
	public void processarEventosPorContrato(final Contrato contrato, TipoAmbienteEnum ambiente) throws NfeException {

		List<Empresa> empresas = contrato.getEmpresas();

		for (final Empresa empresa : empresas) {
			try {
				processarEventosPorEmpresa(contrato, empresa, ambiente);
			} catch (Throwable e) {
				throw new NfeException(e);
			}
		}

	}

	private Future<EventoDocumentoResponse> scheduleProcess(Long idContrato, String pCNPJ, TipoAmbienteEnum ambiente) {
		ProcessadorEventoDocumentoEmpresa processador = processadorEventoInstance
				.select(new ProcessadorEventoDocumentoLiteral(idContrato, pCNPJ, ambiente)).get();

		Future<EventoDocumentoResponse> future = executor.submit(processador);
		return future;
	}

	@Asynchronous
	@Override
	public void processarEventosPorEmpresa(final Contrato contrato, Empresa empresa, TipoAmbienteEnum ambiente) throws NfeException {
		EventoDocumentoResponse eventosDaEmpresa = null;
		do {
			Future<EventoDocumentoResponse> future = scheduleProcess(contrato.getIdContrato(), empresa.getCnpj(), ambiente);
			try {
				eventosDaEmpresa = future.get();
			} catch (InterruptedException | ExecutionException e) {
				throw new NfeException(e);
			}

			List<ResNFe> resumos = new ArrayList<>();

			//processa os eventos da empresa
			List<EventoDocumento> eventos = eventosDaEmpresa.getEventos();
			for (EventoDocumento evDoc : eventos) {
				switch (evDoc.getSchema()) {
				case RESNFE: {
					// resumo de NFe, representam notas que foram emitidas para o cnpj mas que não foram manifestadas pelo destinatário.
					ResNFe resNFe = (ResNFe) evDoc.getJaxbObject();
					// os resumos de nfe devem ser manifestados. Manifestar conhecimento da NFe para permitir o download futuramente.
					resumos.add(resNFe);
					break;
				}
				case NFEPROC: {
					// resultado do processamento da NFe. Contém a nota e assinatura. Tag raiz nfeProc
					NfeProc nfeProc = (NfeProc) evDoc.getJaxbObject();
					// guardar a NFe na base/filesystem ...
					DocumentoFiscal doc = new DocumentoFiscal(TipoDocumentoEnum.NFE, evDoc.getXml(),
							nfeProc.getNFe().getInfNFe().getIde().getNNF(), empresa);

					break;
				}
				case RESEVENTO: {
					// resumo de evento
					ResEvento resEvento = (ResEvento) evDoc.getJaxbObject();
					break;
				}
				case PROCEVENTONFE: {
					// resultado do processamento de um evento
					ProcEventoNFe procEventoNFe = (ProcEventoNFe) evDoc.getJaxbObject();
					break;
				}
				}

			}

		} while (eventosDaEmpresa.possuiEventoRestante());

	}

	@Override
	public void processarEventoDocumentoParaEmpresa(Contrato contrato, Empresa empresa, EventoDocumento evDoc) {

	}

}
