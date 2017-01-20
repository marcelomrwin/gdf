package com.dgreentec.domain.boundary.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.dgreentec.domain.boundary.api.LoteEventoService;
import com.dgreentec.domain.model.Contrato;
import com.dgreentec.domain.model.LoteEvento;
import com.dgreentec.domain.repository.LoteEventoRepository;
import com.dgreentec.domain.repository.filter.FiltroLoteEvento;
import com.dgreentec.infrastructure.persistence.pagination.PagedList;
import com.dgreentec.infrastructure.service.boundary.AbstractBoundary;

@Stateless
public class LoteEventoBoundary extends AbstractBoundary implements LoteEventoService {

	@Inject
	private LoteEventoRepository loteEventoRepository;

	public PagedList<LoteEvento> consultarLoteEventos(Contrato contrato, FiltroLoteEvento filtro) {
		return loteEventoRepository.consultar(filtro);
	}

	public LoteEvento adicionarLoteEvento(Contrato contrato, LoteEvento pLoteEvento) {
		return loteEventoRepository.adicionar(pLoteEvento);
	}

	public LoteEvento atualizarLoteEvento(Contrato contrato, LoteEvento pLoteEvento) {
		return loteEventoRepository.atualizar(pLoteEvento);
	}

	public void removerLoteEvento(Contrato contrato, LoteEvento pLoteEvento) {
		loteEventoRepository.excluir(pLoteEvento);
	}

	public LoteEvento consultarLoteEventoPorIdLoteEvento(Contrato contrato, Integer idLoteEvento) {
		return loteEventoRepository.consultarPorChave(idLoteEvento);
	}

}
