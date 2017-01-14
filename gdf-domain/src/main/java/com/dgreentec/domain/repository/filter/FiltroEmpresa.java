package com.dgreentec.domain.repository.filter;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.dgreentec.domain.model.Empresa;
import com.dgreentec.domain.model.Empresa_;
import com.dgreentec.infrastructure.persistence.filter.FiltroAbstrato;

@SuppressWarnings({ "rawtypes" })
public class FiltroEmpresa extends FiltroAbstrato<Empresa> {

	private static final long serialVersionUID = -7960693642546018436L;

	private String cnpj;

	private boolean cnpjAlterado;

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		updateProperty(Empresa_.CNPJ, cnpj);
	}

	public boolean isCnpjAlterado() {
		return cnpjAlterado;
	}

	private String nome;

	private boolean nomeAlterado;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		updateProperty(Empresa_.NOME, nome);
	}

	public boolean isNomeAlterado() {
		return nomeAlterado;
	}

	public boolean isFiltroAlterado() {
		return isCnpjAlterado() || isNomeAlterado();
	}

	protected void limpar() {
		setCnpj(null);
		setNome(null);
	}

	public Path<?> findPath(String field, Root<Empresa> root, Join... collection) {
		Path<?> path = root.get(Empresa_.cnpj);
		if (field != null) {
			switch (field) {
			case Empresa_.CNPJ:
				path = root.get(Empresa_.cnpj);
				break;
			case Empresa_.NOME:
				path = root.get(Empresa_.nome);
				break;
			case Empresa_.CONTRATO:
				path = root.get(Empresa_.contrato);
				break;
			case Empresa_.UF:
				path = root.get(Empresa_.uf);
				break;
			case Empresa_.CERTIFICADO:
				path = root.get(Empresa_.certificado);
				break;
			case Empresa_.ULTIMONSU:
				path = root.get(Empresa_.ultimoNSU);
				break;
			}
		}
		return path;
	}

	protected Predicate configureWhereClause(CriteriaBuilder criteriaBuilder, Root<Empresa> root, Join... joins) {
		List<Predicate> ands = new ArrayList<>();

		if (isCnpjAlterado()) {
			ands.add(criteriaBuilder.equal(root.get(Empresa_.cnpj), getCnpj()));
		}
		if (isNomeAlterado()) {
			ands.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(Empresa_.nome)), "%" + getNome().toLowerCase() + "%"));
		}

		return criteriaBuilder.and(ands.toArray(new Predicate[ands.size()]));
	}

}
