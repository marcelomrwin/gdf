package com.dgreentec.infrastructure.repository;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.dgreentec.domain.model.Tenant;
import com.dgreentec.domain.repository.TenantRepository;
import com.dgreentec.domain.repository.filter.FiltroTenant;
import com.dgreentec.infrastructure.persistence.GenericModelRepository;
import com.dgreentec.infrastructure.persistence.pagination.PageEntityList;
import com.dgreentec.infrastructure.persistence.pagination.PagedList;

@ApplicationScoped
public class TenantRepositoryJPABean extends GenericModelRepository<Tenant> implements TenantRepository {

	private static final long serialVersionUID = 5192679636416738642L;

	public PagedList<Tenant> consultar(FiltroTenant filtro) {

		CriteriaBuilder cb = createCriteriaBuilder();
		CriteriaQuery<Tenant> cq = cb.createQuery(Tenant.class);
		Root<Tenant> root = cq.from(Tenant.class);
		cq.select(root);
		// order
		configureOrder(cb, cq, root, filtro);
		// filter
		filtro.configurarBusca(cb, cq, root);
		// pagination
		TypedQuery<Tenant> tq = createTypedQuery(cq, filtro, true);

		// count criteria
		CriteriaBuilder cbc = createCriteriaBuilder();
		CriteriaQuery<Long> qc = cbc.createQuery(Long.class);
		Root<Tenant> rootCount = qc.from(Tenant.class);
		qc.select(cbc.count(rootCount));
		filtro.configurarBusca(cbc, qc, rootCount);

		List<Tenant> resultList = tq.getResultList();
		Long count = processCountQuery(qc);

		return new PageEntityList<>(resultList, count);
	}
}
