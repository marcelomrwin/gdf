package com.dgreentec.infrastructure.repository;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.dgreentec.domain.model.DocumentoFiscal;
import com.dgreentec.domain.repository.DocumentoFiscalRepository;
import com.dgreentec.domain.repository.filter.FiltroDocumentoFiscal;
import com.dgreentec.infrastructure.persistence.GenericModelRepository;
import com.dgreentec.infrastructure.persistence.pagination.PageEntityList;
import com.dgreentec.infrastructure.persistence.pagination.PagedList;

@ApplicationScoped
public class DocumentoFiscalRepositoryJPABean extends GenericModelRepository<DocumentoFiscal> implements DocumentoFiscalRepository {

	private static final long serialVersionUID = -7354439226343203437L;

	public PagedList<DocumentoFiscal> consultar(FiltroDocumentoFiscal filtro) {

		CriteriaBuilder cb = createCriteriaBuilder();
		CriteriaQuery<DocumentoFiscal> cq = cb.createQuery(DocumentoFiscal.class);
		Root<DocumentoFiscal> root = cq.from(DocumentoFiscal.class);
		cq.select(root);
// order
		configureOrder(cb, cq, root, filtro);
// filter
		filtro.configurarBusca(cb, cq, root);
// pagination
		TypedQuery<DocumentoFiscal> tq = createTypedQuery(cq, filtro, true);

// count criteria
		CriteriaBuilder cbc = createCriteriaBuilder();
		CriteriaQuery<Long> qc = cbc.createQuery(Long.class);
		Root<DocumentoFiscal> rootCount = qc.from(DocumentoFiscal.class);
		qc.select(cbc.count(rootCount));
		filtro.configurarBusca(cbc, qc, rootCount);

		List<DocumentoFiscal> resultList = tq.getResultList();
		Long count = processCountQuery(qc);

		return new PageEntityList<>(resultList, count);
	}
}
