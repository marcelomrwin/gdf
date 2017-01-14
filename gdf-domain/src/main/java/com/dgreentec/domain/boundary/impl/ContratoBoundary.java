package com.dgreentec.domain.boundary.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.dgreentec.domain.boundary.api.ContratoService;
import com.dgreentec.domain.model.Contrato;
import com.dgreentec.domain.repository.ContratoRepository;
import com.dgreentec.domain.repository.filter.FiltroContrato;
import com.dgreentec.infrastructure.persistence.pagination.PagedList;
import com.dgreentec.infrastructure.service.boundary.AbstractBoundary;

@Stateless
public class ContratoBoundary extends AbstractBoundary implements ContratoService {

	@Inject
	private ContratoRepository contratoRepository;

	public PagedList<Contrato> consultarContratos(FiltroContrato filtro) {
		return contratoRepository.consultar(filtro);
	}

	public Contrato adicionarContrato(Contrato pContrato) {
		return contratoRepository.adicionar(pContrato);
	}

	public Contrato atualizarContrato(Contrato pContrato) {
		return contratoRepository.atualizar(pContrato);
	}

	public void removerContrato(Contrato pContrato) {
		contratoRepository.excluir(pContrato);
	}

	public Contrato consultarContratoPorIdContrato(Long idContrato) {
		return contratoRepository.consultarPorChave(idContrato);
	}

}
