package com.dgreentec.domain.repository.filter;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.dgreentec.domain.model.Contrato;
import com.dgreentec.domain.model.Contrato_;
import com.dgreentec.infrastructure.persistence.filter.FiltroAbstrato;

@SuppressWarnings({ "rawtypes" })
public class FiltroContrato extends FiltroAbstrato<Contrato> {

	private static final long serialVersionUID = -847988449376846930L;

	private String cnpj;

	private boolean cnpjAlterado;

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		updateProperty(Contrato_.CNPJ, cnpj);
	}

	public boolean isCnpjAlterado() {
		return cnpjAlterado;
	}

	public boolean isFiltroAlterado() {
		return isCnpjAlterado();
	}

	protected void limpar() {
		setCnpj(null);
	}

	public Path<?> findPath(String field, Root<Contrato> root, Join... collection) {
		Path<?> path = root.get(Contrato_.idContrato);
		if (field != null) {
			switch (field) {
			case Contrato_.IDCONTRATO:
				path = root.get(Contrato_.idContrato);
				break;
			case Contrato_.CNPJ:
				path = root.get(Contrato_.cnpj);
				break;
			}
		}
		return path;
	}

	protected Predicate configureWhereClause(CriteriaBuilder criteriaBuilder, Root<Contrato> root, Join... joins) {
		List<Predicate> ands = new ArrayList<>();

		if (isCnpjAlterado()) {
			ands.add(criteriaBuilder.equal(root.get(Contrato_.cnpj), getCnpj()));
		}

		return criteriaBuilder.and(ands.toArray(new Predicate[ands.size()]));
	}

}
