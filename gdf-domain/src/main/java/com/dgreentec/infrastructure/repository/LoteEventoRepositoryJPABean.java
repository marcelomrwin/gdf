package com.dgreentec.infrastructure.repository;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.dgreentec.domain.model.LoteEvento;
import com.dgreentec.domain.repository.LoteEventoRepository;
import com.dgreentec.domain.repository.filter.FiltroLoteEvento;
import com.dgreentec.infrastructure.persistence.GenericModelRepository;
import com.dgreentec.infrastructure.persistence.pagination.PageEntityList;
import com.dgreentec.infrastructure.persistence.pagination.PagedList;

@ApplicationScoped
public class LoteEventoRepositoryJPABean extends GenericModelRepository<LoteEvento> implements LoteEventoRepository {

	private static final long serialVersionUID = -3330330438674344513L;

	public PagedList<LoteEvento> consultar(FiltroLoteEvento filtro) {

		CriteriaBuilder cb = createCriteriaBuilder();
		CriteriaQuery<LoteEvento> cq = cb.createQuery(LoteEvento.class);
		Root<LoteEvento> root = cq.from(LoteEvento.class);
		cq.select(root);
// order
		configureOrder(cb, cq, root, filtro);
// filter
		filtro.configurarBusca(cb, cq, root);
// pagination
		TypedQuery<LoteEvento> tq = createTypedQuery(cq, filtro, true);

// count criteria
		CriteriaBuilder cbc = createCriteriaBuilder();
		CriteriaQuery<Long> qc = cbc.createQuery(Long.class);
		Root<LoteEvento> rootCount = qc.from(LoteEvento.class);
		qc.select(cbc.count(rootCount));
		filtro.configurarBusca(cbc, qc, rootCount);

		List<LoteEvento> resultList = tq.getResultList();
		Long count = processCountQuery(qc);

		return new PageEntityList<>(resultList, count);
	}
}
