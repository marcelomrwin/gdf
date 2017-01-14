package com.dgreentec.domain.boundary.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.dgreentec.domain.boundary.api.NFeService;
import com.dgreentec.domain.model.Contrato;
import com.dgreentec.domain.model.Empresa;
import com.dgreentec.domain.model.EventoDocumento;
import com.dgreentec.domain.model.EventoNSU;
import com.dgreentec.domain.model.TipoAmbienteEnum;
import com.dgreentec.domain.repository.EmpresaRepository;
import com.dgreentec.infrastructure.exception.NfeException;

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
	public List<EventoDocumento> consultarEventosPorContrato(final Contrato contrato, TipoAmbienteEnum ambiente) throws NfeException {

		List<Empresa> empresas = contrato.getEmpresas();
		List<EventoDocumento> documentos = new LinkedList<>();
		List<Future<List<EventoDocumento>>> futures = new ArrayList<>();
		try {
			for (final Empresa empresa : empresas) {
				EventoNSU ultimoNSU = empresaRepository.consultarUltimoNSUParaEmpresa(empresa.getCnpj());
				ProcessadorEventoDocumentoEmpresa processador =  processadorEventoInstance.select(new ProcessadorEventoDocumentoLiteral(empresa.getCnpj(),ultimoNSU.getUltimoNSU(),ambiente)).get();  //new ProcessadorEventoDocumentoEmpresa(empresa, ultimoNSU.getUltimoNSU(),ambiente);

				Future<List<EventoDocumento>> future = executor.submit(processador);
				futures.add(future);
			}

			for (Future<List<EventoDocumento>> future : futures) {
				List<EventoDocumento> eventosDaEmpresa = future.get();// FIXME o que fazer com as exceções?
				documentos.addAll(eventosDaEmpresa);
			}

		} catch (Exception e) {
			throw new NfeException(e);
		}
		return documentos;
	}

}
