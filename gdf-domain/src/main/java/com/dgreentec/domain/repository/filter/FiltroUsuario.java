package com.dgreentec.domain.repository.filter;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;

import com.dgreentec.domain.model.Tenant;
import com.dgreentec.domain.model.Tenant_;
import com.dgreentec.domain.model.Usuario;
import com.dgreentec.domain.model.Usuario_;
import com.dgreentec.infrastructure.persistence.filter.FiltroAbstrato;

@SuppressWarnings({ "rawtypes" })
public class FiltroUsuario extends FiltroAbstrato<Usuario> {

	private static final long serialVersionUID = 828229454222300543L;

	private String cpf;

	private boolean cpfAlterado;

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		updateProperty(Usuario_.CPF, cpf);
	}

	public boolean isCpfAlterado() {
		return cpfAlterado;
	}

	private String nome;

	private boolean nomeAlterado;

	private Tenant tenant;

	private boolean tenantAlterado;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		updateProperty(Usuario_.NOME, nome);
	}

	public boolean isNomeAlterado() {
		return nomeAlterado;
	}

	public boolean isFiltroAlterado() {
		return isCpfAlterado() || isNomeAlterado() || isTenantAlterado();
	}

	protected void limpar() {
		setCpf(null);
		setNome(null);
		setTenant(null);
	}

	public Path<?> findPath(String field, Root<Usuario> root, Join... collection) {
		Path<?> path = root.get(Usuario_.cpf);
		if (field != null) {
			switch (field) {
			case Usuario_.CPF:
				path = root.get(Usuario_.cpf);
				break;
			case Usuario_.NOME:
				path = root.get(Usuario_.nome);
				break;
			}
		}
		return path;
	}

	protected Predicate configureWhereClause(CriteriaBuilder criteriaBuilder, Root<Usuario> root, Join... joins) {
		List<Predicate> ands = new ArrayList<>();
		if (isCpfAlterado()) {
			ands.add(criteriaBuilder.equal(root.get(Usuario_.cpf), getCpf()));
		}
		if (isNomeAlterado()) {
			ands.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(Usuario_.nome)), "%" + getNome().toLowerCase() + "%"));
		}
		if (isTenantAlterado()) {
			SetJoin<Usuario, Tenant> joinTenants = root.join(Usuario_.tenants);
			ands.add(criteriaBuilder.equal(joinTenants.get(Tenant_.idTenant), getTenant().getIdTenant()));
		}
		return criteriaBuilder.and(ands.toArray(new Predicate[ands.size()]));
	}

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		updateProperty("tenant", tenant);
	}

	public boolean isTenantAlterado() {
		return tenantAlterado;
	}

}
