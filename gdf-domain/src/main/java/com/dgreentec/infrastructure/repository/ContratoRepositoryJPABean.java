package com.dgreentec.infrastructure.repository;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.dgreentec.domain.model.Contrato;
import com.dgreentec.domain.repository.ContratoRepository;
import com.dgreentec.domain.repository.filter.FiltroContrato;
import com.dgreentec.infrastructure.persistence.GenericModelRepository;
import com.dgreentec.infrastructure.persistence.pagination.PageEntityList;
import com.dgreentec.infrastructure.persistence.pagination.PagedList;

@ApplicationScoped
public class ContratoRepositoryJPABean extends GenericModelRepository<Contrato> implements ContratoRepository {

	private static final long serialVersionUID = 5861655554133114627L;

	public PagedList<Contrato> consultar(FiltroContrato filtro) {

		CriteriaBuilder cb = createCriteriaBuilder();
		CriteriaQuery<Contrato> cq = cb.createQuery(Contrato.class);
		Root<Contrato> root = cq.from(Contrato.class);
		cq.select(root);
		// order
		configureOrder(cb, cq, root, filtro);
		// filter
		filtro.configurarBusca(cb, cq, root);
		// pagination
		TypedQuery<Contrato> tq = createTypedQuery(cq, filtro, true);

		// count criteria
		CriteriaBuilder cbc = createCriteriaBuilder();
		CriteriaQuery<Long> qc = cbc.createQuery(Long.class);
		Root<Contrato> rootCount = qc.from(Contrato.class);
		qc.select(cbc.count(rootCount));
		filtro.configurarBusca(cbc, qc, rootCount);

		List<Contrato> resultList = tq.getResultList();
		Long count = processCountQuery(qc);

		return new PageEntityList<>(resultList, count);
	}
}
