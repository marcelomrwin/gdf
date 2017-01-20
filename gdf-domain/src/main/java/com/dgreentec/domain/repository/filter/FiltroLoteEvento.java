package com.dgreentec.domain.repository.filter;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.dgreentec.domain.model.Empresa;
import com.dgreentec.domain.model.LoteEvento;
import com.dgreentec.domain.model.LoteEvento_;
import com.dgreentec.infrastructure.persistence.filter.FiltroAbstrato;

@SuppressWarnings({ "rawtypes" })
public class FiltroLoteEvento extends FiltroAbstrato<LoteEvento> {

	private static final long serialVersionUID = 3380021557716463624L;

	private Integer idLoteEvento;

	private boolean idLoteEventoAlterado;

	public Integer getIdLoteEvento() {
		return idLoteEvento;
	}

	public void setIdLoteEvento(Integer idLoteEvento) {
		updateProperty(LoteEvento_.IDLOTEEVENTO, idLoteEvento);
	}

	public boolean isIdLoteEventoAlterado() {
		return idLoteEventoAlterado;
	}

	private Empresa empresa;

	private boolean empresaAlterado;

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		updateProperty(LoteEvento_.EMPRESA, empresa);
	}

	public boolean isEmpresaAlterado() {
		return empresaAlterado;
	}

	public boolean isFiltroAlterado() {
		return isIdLoteEventoAlterado() || isEmpresaAlterado();
	}

	protected void limpar() {
		setIdLoteEvento(null);
		setEmpresa(null);
	}

	public Path<?> findPath(String field, Root<LoteEvento> root, Join... collection) {
		Path<?> path = root.get(LoteEvento_.idLoteEvento);
		if (field != null) {
			switch (field) {
			case LoteEvento_.IDLOTEEVENTO:
				path = root.get(LoteEvento_.idLoteEvento);
				break;
			case LoteEvento_.EMPRESA:
				path = root.get(LoteEvento_.empresa);
				break;
			}
		}
		return path;
	}

	protected Predicate configureWhereClause(CriteriaBuilder criteriaBuilder, Root<LoteEvento> root, Join... joins) {
		List<Predicate> ands = new ArrayList<>();

		if (isIdLoteEventoAlterado()) {
			ands.add(criteriaBuilder.equal(root.get(LoteEvento_.idLoteEvento), getIdLoteEvento()));
		}
		if (isEmpresaAlterado()) {
			ands.add(criteriaBuilder.equal(root.get(LoteEvento_.empresa), getEmpresa()));
		}
		return criteriaBuilder.and(ands.toArray(new Predicate[ands.size()]));
	}

}
