package com.dgreentec.infrastructure.repository;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.dgreentec.domain.model.Empresa;
import com.dgreentec.domain.model.Empresa_;
import com.dgreentec.domain.model.UltimoEventoNSU;
import com.dgreentec.domain.repository.EmpresaRepository;
import com.dgreentec.domain.repository.filter.FiltroEmpresa;
import com.dgreentec.infrastructure.persistence.GenericModelRepository;
import com.dgreentec.infrastructure.persistence.pagination.PageEntityList;
import com.dgreentec.infrastructure.persistence.pagination.PagedList;

@ApplicationScoped
public class EmpresaRepositoryJPABean extends GenericModelRepository<Empresa> implements EmpresaRepository {

	private static final long serialVersionUID = 3753683844072497918L;

	@Override
	public UltimoEventoNSU consultarUltimoNSUParaEmpresa(String cnpj) {
		CriteriaBuilder cb = createCriteriaBuilder();
		CriteriaQuery<UltimoEventoNSU> cq = cb.createQuery(UltimoEventoNSU.class);
		Root<Empresa> root = cq.from(Empresa.class);
		cq.select(root.get(Empresa_.ultimoNSU));
		cq.where(cb.equal(root.get(Empresa_.cnpj), cnpj));
		TypedQuery<UltimoEventoNSU> tq = entityManager.get().createQuery(cq);
		UltimoEventoNSU ultimoNSU = new UltimoEventoNSU();
		try {
			ultimoNSU = tq.getSingleResult();
		} catch (NoResultException nre) {
			logger.warn("No result");
		}
		if (ultimoNSU == null)
			ultimoNSU = new UltimoEventoNSU();

		return ultimoNSU;
	}

	public PagedList<Empresa> consultar(FiltroEmpresa filtro) {

		CriteriaBuilder cb = createCriteriaBuilder();
		CriteriaQuery<Empresa> cq = cb.createQuery(Empresa.class);
		Root<Empresa> root = cq.from(Empresa.class);
		cq.select(root);
		// order
		configureOrder(cb, cq, root, filtro);
		// filter
		filtro.configurarBusca(cb, cq, root);
		// pagination
		TypedQuery<Empresa> tq = createTypedQuery(cq, filtro, true);

		// count criteria
		CriteriaBuilder cbc = createCriteriaBuilder();
		CriteriaQuery<Long> qc = cbc.createQuery(Long.class);
		Root<Empresa> rootCount = qc.from(Empresa.class);
		qc.select(cbc.count(rootCount));
		filtro.configurarBusca(cbc, qc, rootCount);

		List<Empresa> resultList = tq.getResultList();
		Long count = processCountQuery(qc);

		return new PageEntityList<>(resultList, count);
	}

}
