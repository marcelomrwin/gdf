package com.dgreentec.infrastructure.repository;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.dgreentec.domain.model.Usuario;
import com.dgreentec.domain.repository.UsuarioRepository;
import com.dgreentec.domain.repository.filter.FiltroUsuario;
import com.dgreentec.infrastructure.persistence.GenericModelRepository;
import com.dgreentec.infrastructure.persistence.pagination.PageEntityList;
import com.dgreentec.infrastructure.persistence.pagination.PagedList;

@ApplicationScoped
public class UsuarioRepositoryJPABean extends GenericModelRepository<Usuario> implements UsuarioRepository {

	private static final long serialVersionUID = -831904473841199821L;

	public PagedList<Usuario> consultar(FiltroUsuario filtro) {

		CriteriaBuilder cb = createCriteriaBuilder();
		CriteriaQuery<Usuario> cq = cb.createQuery(Usuario.class);
		Root<Usuario> root = cq.from(Usuario.class);
		cq.select(root);
		// order
		configureOrder(cb, cq, root, filtro);
		// filter
		filtro.configurarBusca(cb, cq, root);
		// pagination
		TypedQuery<Usuario> tq = createTypedQuery(cq, filtro, true);

		// count criteria
		CriteriaBuilder cbc = createCriteriaBuilder();
		CriteriaQuery<Long> qc = cbc.createQuery(Long.class);
		Root<Usuario> rootCount = qc.from(Usuario.class);
		qc.select(cbc.count(rootCount));
		filtro.configurarBusca(cbc, qc, rootCount);

		List<Usuario> resultList = tq.getResultList();
		Long count = processCountQuery(qc);

		return new PageEntityList<>(resultList, count);
	}
}
